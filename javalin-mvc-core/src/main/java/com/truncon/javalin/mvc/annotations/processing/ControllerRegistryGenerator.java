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
import io.javalin.Javalin;
import org.apache.commons.lang3.tuple.ImmutablePair;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.stream.Collectors;

final class ControllerRegistryGenerator {
    private final ContainerSource container;
    private final List<ControllerSource> controllers;
    private final List<WsControllerSource> wsControllers;

    public ControllerRegistryGenerator(
            ContainerSource container,
            List<ControllerSource> controllers,
            List<WsControllerSource> wsControllers) {
        this.container = container;
        this.controllers = controllers;
        this.wsControllers = wsControllers;
    }

    public void generateRoutes(Filer filer) throws IOException, ProcessingException {
        TypeSpec.Builder registryTypeBuilder = TypeSpec.classBuilder("JavalinControllerRegistry")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(ControllerRegistry.class);

        AnnotationSpec generatedAnnotation = AnnotationSpec.builder(Generated.class)
            .addMember("value", "$S", ControllerProcessor.class.getCanonicalName())
            .build();
        registryTypeBuilder.addAnnotation(generatedAnnotation);

        TypeName factoryType = ParameterizedTypeName.get(
            ClassName.get(Supplier.class),
            TypeName.get(container.getType()));
        FieldSpec scopeFactoryField = FieldSpec.builder(factoryType, "scopeFactory", Modifier.PRIVATE, Modifier.FINAL).build();
        registryTypeBuilder.addField(scopeFactoryField);

        MethodSpec constructor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(factoryType, "scopeFactory")
            .addStatement("this.scopeFactory = scopeFactory")
            .build();
        registryTypeBuilder.addMethod(constructor);

        final String APP_NAME = "app";
        MethodSpec register = MethodSpec.methodBuilder("register")
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Javalin.class, APP_NAME, Modifier.FINAL)
            .addCode(createActionMethods(APP_NAME))
            .addCode(createWsEndpoints(APP_NAME))
            .build();
        registryTypeBuilder.addMethod(register);

        TypeSpec registryType = registryTypeBuilder.build();
        JavaFile registryFile = JavaFile.builder("com.truncon.javalin.mvc", registryType)
            .indent("    ")
            .build();
        JavaFileObject file = filer.createSourceFile("com.truncon.javalin.mvc.JavalinControllerRegistry");
        try (Writer writer = file.openWriter()) {
            registryFile.writeTo(writer);
        }
    }

    private CodeBlock createActionMethods(String app) {
        AtomicInteger index = new AtomicInteger();
        Collection<RouteGenerator> generators = controllers.stream()
            .map(ControllerSource::getRouteGenerators)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());
        detectDuplicateRoutes(generators);
        return generators.stream()
            .map(g -> g.generateRoute(container, app, index.getAndIncrement()))
            .collect(CodeBlock.joining("\n"));
    }

    private static void detectDuplicateRoutes(Collection<RouteGenerator> generators) {
        Map<ImmutablePair<String, String>, Set<RouteGenerator>> pairs = new HashMap<>();
        for (RouteGenerator generator : generators) {
            ImmutablePair<String, String> pair = ImmutablePair.of(generator.getMethodType(), generator.getRoute());
            pairs.computeIfAbsent(pair, k -> new HashSet<>()).add(generator);
        }
        Map<ImmutablePair<String, String>, Set<RouteGenerator>> duplicates = new HashMap<>();
        for (Map.Entry<ImmutablePair<String, String>, Set<RouteGenerator>> pair: pairs.entrySet()) {
            if (pair.getValue().size() > 1) {
                duplicates.put(pair.getKey(), pair.getValue());
            }
        }
        if (!duplicates.isEmpty()) {
            List<ProcessingException> exceptions = new ArrayList<>(duplicates.size());
            List<String> messages = new ArrayList<>();
            for (Map.Entry<ImmutablePair<String, String>, Set<RouteGenerator>> pair : duplicates.entrySet()) {
                String methods = pair.getValue().stream().map(RouteGenerator::getQualifiedMethodName).collect(Collectors.joining(", "));
                String subMessage = "Multiple handlers exist for: "
                    + pair.getKey().getLeft().toUpperCase() // Method
                    + " "
                    + pair.getKey().getRight() // Route
                    + ". Implementations found: "
                    + methods
                    + ".";
                messages.add(subMessage);
                List<Element> elements = new ArrayList<>();
                for (RouteGenerator generator : pair.getValue()) {
                     elements.add(generator.getMethodElement());
                }
                exceptions.add(new ProcessingException(subMessage, elements.toArray(new Element[0])));
            }
            String message = String.join("\n", messages);
            throw new ProcessingMultiException(message, exceptions.toArray(new ProcessingException[0]));
        }
    }

    private CodeBlock createWsEndpoints(String app) throws ProcessingException {
        return wsControllers.stream()
            .map(s -> s.generateEndpoint(container, app))
            .filter(Objects::nonNull)
            .collect(CodeBlock.joining("\n"));
    }
}
