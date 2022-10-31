package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.truncon.javalin.mvc.ControllerRegistry;
import com.truncon.javalin.mvc.api.Injector;
import io.javalin.Javalin;

import jakarta.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ControllerRegistryGenerator {
    public static final String APP_NAME = "app";
    public static final String REGISTRY_PACKAGE_NAME = "com.truncon.javalin.mvc";
    public static final String REGISTRY_CLASS_NAME = "JavalinControllerRegistry";
    public static final String SCOPE_FACTORY_NAME = "scopeFactory";

    private final ContainerSource container;
    private final List<ControllerSource> controllers;
    private final List<WsControllerSource> wsControllers;
    private final Map<String, ConverterBuilder> converterLookup;

    public ControllerRegistryGenerator(
            ContainerSource container,
            List<ControllerSource> controllers,
            List<WsControllerSource> wsControllers,
            List<ConverterBuilder> converters) {
        this.container = container;
        this.controllers = controllers;
        this.wsControllers = wsControllers;
        this.converterLookup = converters.stream()
            .collect(Collectors.toMap(ConverterBuilder::getName, c -> c));
    }

    public void generateRegistry(Filer filer) throws IOException, ProcessingException {
        TypeSpec.Builder registryTypeBuilder = TypeSpec.classBuilder(REGISTRY_CLASS_NAME)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(ControllerRegistry.class);

        AnnotationSpec generatedAnnotation = AnnotationSpec.builder(Generated.class)
            .addMember("value", "$S", ControllerProcessor.class.getCanonicalName())
            .build();
        registryTypeBuilder.addAnnotation(generatedAnnotation);

        switch (container.getContainerType()) {
            case DAGGER: {
                TypeName factoryType = ParameterizedTypeName.get(
                    ClassName.get(Supplier.class),
                    TypeName.get(container.getInjectorType()));
                FieldSpec scopeFactoryField = FieldSpec.builder(
                    factoryType,
                    SCOPE_FACTORY_NAME,
                    Modifier.PRIVATE,
                    Modifier.FINAL
                ).build();
                registryTypeBuilder.addField(scopeFactoryField);

                MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(factoryType, SCOPE_FACTORY_NAME)
                    .addStatement("this.$N = $N", SCOPE_FACTORY_NAME, SCOPE_FACTORY_NAME)
                    .build();
                registryTypeBuilder.addMethod(constructor);
                break;
            }
            case RUNTIME: {
                TypeName factoryType = ParameterizedTypeName.get(
                    ClassName.get(Supplier.class),
                    TypeName.get(Injector.class));
                FieldSpec scopeFactoryField = FieldSpec.builder(
                    factoryType,
                    SCOPE_FACTORY_NAME,
                    Modifier.PRIVATE,
                    Modifier.FINAL
                ).build();
                registryTypeBuilder.addField(scopeFactoryField);

                MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(factoryType, SCOPE_FACTORY_NAME)
                    .addStatement("this.$N = $N", SCOPE_FACTORY_NAME, SCOPE_FACTORY_NAME)
                    .build();
                registryTypeBuilder.addMethod(constructor);
                break;
            }
            default: {
                MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PUBLIC)
                    .build();
                registryTypeBuilder.addMethod(constructor);
                break;
            }
        }

        HelperMethodBuilder helperBuilder = new HelperMethodBuilder(container, converterLookup, registryTypeBuilder);
        MethodSpec register = MethodSpec.methodBuilder("register")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Javalin.class, APP_NAME)
            .addCode(createHttpRouteHandlers(helperBuilder))
            .addCode(createWsRouteHandlers(helperBuilder))
            .build();
        registryTypeBuilder.addMethod(register);

        TypeSpec registryType = registryTypeBuilder.build();
        JavaFile registryFile = JavaFile.builder(REGISTRY_PACKAGE_NAME, registryType)
            .indent("    ")
            .build();
        TypeElement[] sources = Stream.concat(
            controllers.stream().map(ControllerSource::getType),
            wsControllers.stream().map(WsControllerSource::getType)
        ).toArray(TypeElement[]::new);
        JavaFileObject file = filer.createSourceFile(REGISTRY_PACKAGE_NAME + "." + REGISTRY_CLASS_NAME, sources);
        try (Writer writer = file.openWriter()) {
            registryFile.writeTo(writer);
        }
    }

    private CodeBlock createHttpRouteHandlers(HelperMethodBuilder helperBuilder) {
        MutableInt index = new MutableInt();
        Collection<RouteGenerator> generators = controllers.stream()
            .map(ControllerSource::getRouteGenerators)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        detectDuplicateRoutes(generators);
        return generators.stream()
            .map(g -> g.generateRouteHandler(index.getAndIncrement(), helperBuilder, converterLookup))
            .collect(CodeBlock.joining(""));
    }

    private static void detectDuplicateRoutes(Collection<RouteGenerator> generators) {
        Map<RouteKey, Set<RouteGenerator>> pairs = new HashMap<>();
        for (RouteGenerator generator : generators) {
            RouteKey pair = RouteKey.of(generator.getMethodType(), generator.getRoute());
            pairs.computeIfAbsent(pair, k -> new HashSet<>()).add(generator);
        }
        Map<RouteKey, Set<RouteGenerator>> duplicates = new HashMap<>();
        for (Map.Entry<RouteKey, Set<RouteGenerator>> pair: pairs.entrySet()) {
            if (pair.getValue().size() > 1) {
                duplicates.put(pair.getKey(), pair.getValue());
            }
        }
        if (!duplicates.isEmpty()) {
            List<ProcessingException> exceptions = new ArrayList<>(duplicates.size());
            for (Map.Entry<RouteKey, Set<RouteGenerator>> pair : duplicates.entrySet()) {
                String methods = pair.getValue().stream()
                    .map(RouteGenerator::getQualifiedMethodName)
                    .collect(Collectors.joining(", "));
                String subMessage = "Multiple handlers exist for: "
                    + pair.getKey().getMethodType().toUpperCase()
                    + " "
                    + pair.getKey().getRoute()
                    + ". Implementations found: "
                    + methods
                    + ".";
                List<Element> elements = new ArrayList<>();
                for (RouteGenerator generator : pair.getValue()) {
                     elements.add(generator.getMethodElement());
                }
                exceptions.add(new ProcessingException(subMessage, elements.toArray(new Element[0])));
            }
            throw new ProcessingMultiException(exceptions.toArray(new ProcessingException[0]));
        }
    }

    private CodeBlock createWsRouteHandlers(HelperMethodBuilder helperBuilder) throws ProcessingException {
        MutableInt index = new MutableInt();
        return wsControllers.stream()
            .map(s -> s.generateRouteHandler(index.getAndIncrement(), helperBuilder, converterLookup))
            .filter(Objects::nonNull)
            .collect(CodeBlock.joining(""));
    }

    private static final class RouteKey {
        private final String methodType;
        private final String route;

        private RouteKey(String methodType, String route) {
            this.methodType = methodType;
            this.route = route;
        }

        public static RouteKey of(String methodType, String route) {
            return new RouteKey(methodType, route);
        }

        public String getMethodType() {
            return methodType;
        }

        public String getRoute() {
            return route;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            RouteKey routeKey = (RouteKey) o;
            return methodType.equals(routeKey.methodType) && route.equals(routeKey.route);
        }

        @Override
        public int hashCode() {
            return Objects.hash(methodType, route);
        }
    }
}
