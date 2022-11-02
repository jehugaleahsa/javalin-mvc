package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.JavalinBeforeActionContext;
import com.truncon.javalin.mvc.api.Before;
import com.truncon.javalin.mvc.api.BeforeActionContext;
import com.truncon.javalin.mvc.api.BeforeContainer;

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
            handlers.addAll(Arrays.asList(multiple.value()));
        }
        return handlers.stream().map(h -> new BeforeGenerator(container, h)).collect(Collectors.toList());
    }

    public boolean generateBefore(
            CodeBlock.Builder routeBuilder,
            String injectorName,
            String contextName,
            int index) {
        String arguments = getArguments();
        InjectionResult result = container.getInstanceCall(getTypeMirror(), injectorName);
        String handlerName = "beforeHandler" + index;
        routeBuilder.addStatement(
            "$T $N = new $T($N, $L)",
            BeforeActionContext.class,
            handlerName,
            JavalinBeforeActionContext.class,
            contextName,
            arguments
        );
        routeBuilder.addStatement("$L.executeBefore($N)", result.getInstanceCall(), handlerName);
        routeBuilder.beginControlFlow("if ($N.isCancelled())", handlerName)
            .addStatement("return")
            .endControlFlow();
        return result.isInjectorNeeded();
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
