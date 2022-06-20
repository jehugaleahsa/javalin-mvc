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

    public boolean generateAfter(
            CodeBlock.Builder routeBuilder,
            String injectorName,
            String contextName,
            String exceptionName) {
        String arguments = getArguments();
        Name handlerGetter = injectorName == null ? null : getHandlerGetter();
        if (container.getContainerType() == ContainerSource.Type.DAGGER && handlerGetter != null) {
            routeBuilder.addStatement(
                "$L = $L.$L().executeAfter($L, $L, $L)",
                exceptionName,
                injectorName,
                handlerGetter,
                contextName,
                arguments,
                exceptionName);
            return true;
        } else if (container.getContainerType() == ContainerSource.Type.GUICE) {
            routeBuilder.addStatement(
                "$L = $L.getInstance($T.class).executeAfter($L, $L, $L)",
                exceptionName,
                injectorName,
                getTypeMirror(),
                contextName,
                arguments,
                exceptionName);
            return true;
        } else  {
            routeBuilder.addStatement(
                "$L = new $T().executeAfter($L, $L, $L)",
                exceptionName,
                getTypeMirror(),
                contextName,
                arguments,
                exceptionName);
            return false;
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
