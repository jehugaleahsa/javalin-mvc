package io.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import io.javalin.mvc.api.Before;
import io.javalin.mvc.api.BeforeContainer;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class BeforeGenerator {
    private final ContainerSource container;
    private final Before annotation;

    private BeforeGenerator(ContainerSource container, Before annotation) {
        this.container = container;
        this.annotation = annotation;
    }

    public static List<BeforeGenerator> getBeforeGenerators(ContainerSource container, RouteGenerator route) {
        List<Before> handlers = new ArrayList<>();
        Before single = route.findAnnotation(Before.class);
        if (single != null) {
            handlers.add(single);
        }
        BeforeContainer multiple = route.findAnnotation(BeforeContainer.class);
        if (multiple != null) {
            for (Before before : multiple.value()) {
                handlers.add(before);
            }
        }
        return handlers.stream().map(h -> new BeforeGenerator(container, h)).collect(Collectors.toList());
    }

    public void generateBefore(
            CodeBlock.Builder routeBuilder,
            String injectorName,
            String contextName) {
        Name handlerGetter = injectorName == null ? null : getHandlerGetter();
        String arguments = getArguments();
        if (handlerGetter == null) {
            routeBuilder.beginControlFlow(
                    "if (!new $T().executeBefore($L, $L))",
                    getTypeMirror(),
                    contextName,
                    arguments)
                    .addStatement("return")
                    .endControlFlow();
        } else {
            routeBuilder.beginControlFlow(
                    "if (!$L.$L().executeBefore($L, $L))",
                    injectorName,
                    handlerGetter,
                    contextName,
                    arguments)
                    .addStatement("return")
                    .endControlFlow();
        }
    }

    private Name getHandlerGetter() {
        TypeMirror handlerType = getTypeMirror();
        TypeElement handlerTypeElement = (TypeElement)container.getTypeUtils().asElement(handlerType);
        return container.getDependencyName(handlerTypeElement);
    }

    private TypeMirror getTypeMirror() {
        try {
            annotation.handler();
        } catch (MirroredTypeException exception) {
            return exception.getTypeMirror();
        }
        return  null;
    }

    private String getArguments() {
        String[] arguments = annotation.arguments();
        String[] quotedArguments = Arrays.stream(arguments).map(s -> "\"" + s + "\"").toArray(String[]::new);
        return "new String[] { " + String.join(", ", quotedArguments) + " }";
    }
}
