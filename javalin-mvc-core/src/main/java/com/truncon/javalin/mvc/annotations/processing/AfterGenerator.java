package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.After;
import com.truncon.javalin.mvc.api.AfterContainer;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class AfterGenerator {
    private final ContainerSource container;
    private final After annotation;

    private AfterGenerator(ContainerSource container, After annotation) {
        this.container = container;
        this.annotation = annotation;
    }

    public static List<AfterGenerator> getAfterGenerators(ContainerSource container, RouteGenerator route) {
        List<After> handlers = new ArrayList<>();
        After single = route.findAnnotation(After.class);
        if (single != null) {
            handlers.add(single);
        }
        AfterContainer multiple = route.findAnnotation(AfterContainer.class);
        if (multiple != null) {
            handlers.addAll(Arrays.asList(multiple.value()));
        }
        return handlers.stream().map(h -> new AfterGenerator(container, h)).collect(Collectors.toList());
    }

    public void generateAfter(
            CodeBlock.Builder routeBuilder,
            String injectorName,
            String contextName,
            String exceptionName) {
        Name handlerGetter = injectorName == null ? null : getHandlerGetter();
        String arguments = getArguments();
        if (handlerGetter == null) {
            routeBuilder.addStatement(
                    "$L = new $T().executeAfter($L, $L, $L)",
                    exceptionName,
                    getTypeMirror(),
                    contextName,
                    arguments,
                    exceptionName);
        } else {
            routeBuilder.addStatement(
                    "$L = $L.$L().executeAfter($L, $L, $L)",
                    exceptionName,
                    injectorName,
                    handlerGetter,
                    contextName,
                    arguments,
                    exceptionName);
        }
    }

    private Name getHandlerGetter() {
        TypeMirror handlerType = getTypeMirror();
        TypeElement handlerTypeElement = container.getTypeUtils().getTypeElement(handlerType);
        return container.getDependencyName(handlerTypeElement);
    }

    private TypeMirror getTypeMirror() {
        try {
            annotation.handler();
        } catch (MirroredTypeException exception) {
            return exception.getTypeMirror();
        }
        return null;
    }

    private String getArguments() {
        String[] arguments = annotation.arguments();
        String[] quotedArguments = Arrays.stream(arguments).map(s -> "\"" + s + "\"").toArray(String[]::new);
        return "new String[] { " + String.join(", ", quotedArguments) + " }";
    }
}
