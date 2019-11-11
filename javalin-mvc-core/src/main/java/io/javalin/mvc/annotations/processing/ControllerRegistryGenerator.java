package io.javalin.mvc.annotations.processing;

import com.squareup.javapoet.*;
import io.javalin.Javalin;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

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

    public void generateRoutes(Filer filer) throws IOException {
        TypeSpec.Builder registryTypeBuilder = TypeSpec.classBuilder("ControllerRegistry")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

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
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Javalin.class, APP_NAME, Modifier.FINAL)
            .addCode(createActionMethods(APP_NAME))
            .addCode(createWsEndpoints(APP_NAME))
            .build();
        registryTypeBuilder.addMethod(register);

        TypeSpec registryType = registryTypeBuilder.build();
        JavaFile registryFile = JavaFile.builder("io.javalin.mvc", registryType)
            .indent("    ")
            .build();
        JavaFileObject file = filer.createSourceFile("io.javalin.mvc.ControllerRegistry");
        try (Writer writer = file.openWriter()) {
            registryFile.writeTo(writer);
        }
    }

    private CodeBlock createActionMethods(String app) {
        AtomicInteger index = new AtomicInteger();
        return controllers.stream()
            .flatMap(r -> r.getRouteGenerators().stream())
            .map(g -> g.generateRoute(container, app, index.getAndIncrement()))
            .collect(CodeBlock.joining("\n"));
    }

    private CodeBlock createWsEndpoints(String app) {
        return wsControllers.stream()
            .map(s -> s.generateEndpoint(container, app))
            .collect(CodeBlock.joining("\n"));
    }
}
