package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.JavalinAfterActionContext;
import com.truncon.javalin.mvc.api.After;
import com.truncon.javalin.mvc.api.AfterActionContext;
import com.truncon.javalin.mvc.api.AfterContainer;

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
            CodeBlock.Builder handlerBuilder,
            String injectorName,
            String contextName,
            String exceptionName,
            int index) {
        String arguments = getArguments();
        InjectionResult result = container.getInstanceCall(getTypeMirror(), injectorName);
        String handlerContextName = "afterContext" + index;
        handlerBuilder.addStatement(
            "$T $N = new $T($N, $L, $N, handled)",
            AfterActionContext.class,
            handlerContextName,
            JavalinAfterActionContext.class,
            contextName,
            arguments,
            exceptionName
        );
        handlerBuilder.addStatement("$L.executeAfter($N)", result.getInstanceCall(), handlerContextName);
        handlerBuilder.addStatement("$N = $N.getException()", exceptionName, handlerContextName);
        handlerBuilder.addStatement("handled = $N.isHandled()", handlerContextName);
        return result.isInjectorNeeded();
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
