package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.ws.WsBefore;
import com.truncon.javalin.mvc.api.ws.WsBeforeActionContext;
import com.truncon.javalin.mvc.api.ws.WsBeforeContainer;
import com.truncon.javalin.mvc.ws.JavalinWsBeforeActionContext;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class WsBeforeGenerator {
    private final ContainerSource container;
    private final WsBefore annotation;

    private WsBeforeGenerator(ContainerSource container, WsBefore annotation) {
        this.container = container;
        this.annotation = annotation;
    }

    public static List<WsBeforeGenerator> getBeforeGenerators(ContainerSource container, ExecutableElement method) {
        List<WsBefore> handlers = new ArrayList<>();
        WsBefore single = method.getAnnotation(WsBefore.class);
        if (single != null) {
            handlers.add(single);
        }
        WsBeforeContainer multiple = method.getAnnotation(WsBeforeContainer.class);
        if (multiple != null) {
            handlers.addAll(Arrays.asList(multiple.value()));
        }
        return handlers.stream().map(h -> new WsBeforeGenerator(container, h)).collect(Collectors.toList());
    }

    public boolean generateBefore(
            CodeBlock.Builder routeBuilder,
            String injectorName,
            String contextName,
            int index) {
        String arguments = getArguments();
        InjectionResult result = container.getInstanceCall(getTypeMirror(), injectorName);
        String handlerContextName = "beforeContext" + index;
        routeBuilder.addStatement(
            "$T $N = new $T($N, $L)",
            WsBeforeActionContext.class,
            handlerContextName,
            JavalinWsBeforeActionContext.class,
            contextName,
            arguments
        );
        routeBuilder.addStatement("$L.executeBefore($N)", result.getInstanceCall(), handlerContextName);
        routeBuilder.beginControlFlow("if ($N.isCancelled())", handlerContextName)
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
