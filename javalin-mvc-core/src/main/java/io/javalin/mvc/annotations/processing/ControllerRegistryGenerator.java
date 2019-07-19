package io.javalin.mvc.annotations.processing;

import com.squareup.javapoet.*;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiPlugin;

import javax.annotation.Generated;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

final class ControllerRegistryGenerator {
    private final ContainerSource container;
    private final List<ControllerSource> controllers;

    public ControllerRegistryGenerator(ContainerSource container, List<ControllerSource> controllers) {
        this.container = container;
        this.controllers = controllers;
    }

    public void generateRoutes(Filer filer) throws IOException {
        TypeSpec.Builder registryTypeBuilder = TypeSpec.classBuilder("ControllerRegistry")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        AnnotationSpec generatedAnnotation = AnnotationSpec.builder(Generated.class)
            .addMember("value", "$S", ControllerProcessor.class.getCanonicalName())
            .build();
        registryTypeBuilder.addAnnotation(generatedAnnotation);

        FieldSpec scopeFactoryField = FieldSpec.builder(Function.class, "scopeFactory", Modifier.PRIVATE, Modifier.FINAL).build();
        registryTypeBuilder.addField(scopeFactoryField);

        MethodSpec constructor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Function.class, "scopeFactory")
            .addStatement("this.scopeFactory = scopeFactory")
            .build();
        registryTypeBuilder.addMethod(constructor);

        MethodSpec isOpenApiPluginRegistered = MethodSpec.methodBuilder("isOpenApiPluginRegistered")
            .returns(boolean.class)
            .addModifiers(Modifier.PRIVATE)
            .addParameter(Javalin.class, "app", Modifier.FINAL)
            .addCode(CodeBlock.builder()
                .beginControlFlow("try")
                .addStatement("app.config.getPlugin($T.class)", OpenApiPlugin.class)
                .addStatement("return true")
                .nextControlFlow("catch ($T ex)", Throwable.class)
                .addStatement("return false")
                .endControlFlow()
                .build())
            .build();
        registryTypeBuilder.addMethod(isOpenApiPluginRegistered);

        MethodSpec register = MethodSpec.methodBuilder("register")
            .addModifiers(Modifier.PUBLIC)
            .addParameter(Javalin.class, "app", Modifier.FINAL)
            .addStatement("$T hasOpenApi = isOpenApiPluginRegistered(app)", boolean.class)
            .addCode(createActionMethods("app", "hasOpenApi"))
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

    private CodeBlock createActionMethods(String app, String hasOpenApi) {
        AtomicInteger index = new AtomicInteger();
        return controllers.stream()
            .flatMap(r -> r.getRouteGenerators().stream())
            .map(g -> g.generateRoute(container, app, hasOpenApi, index.getAndIncrement()))
            .collect(CodeBlock.joining("\n"));
    }
}
