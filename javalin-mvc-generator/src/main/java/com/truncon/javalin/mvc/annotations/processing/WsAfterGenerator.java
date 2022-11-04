package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.ws.WsAfter;
import com.truncon.javalin.mvc.api.ws.WsAfterActionContext;
import com.truncon.javalin.mvc.api.ws.WsAfterContainer;
import com.truncon.javalin.mvc.ws.JavalinWsAfterActionContext;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

final class WsAfterGenerator {
    private final ContainerSource container;
    private final WsAfter annotation;

    private WsAfterGenerator(ContainerSource container, WsAfter annotation) {
        this.container = container;
        this.annotation = annotation;
    }

    public static List<WsAfterGenerator> getAfterGenerators(ContainerSource container, ExecutableElement method) {
        List<WsAfter> handlers = new ArrayList<>();
        WsAfter single = method.getAnnotation(WsAfter.class);
        if (single != null) {
            handlers.add(single);
        }
        WsAfterContainer multiple = method.getAnnotation(WsAfterContainer.class);
        if (multiple != null) {
            handlers.addAll(Arrays.asList(multiple.value()));
        }
        return handlers.stream().map(h -> new WsAfterGenerator(container, h)).collect(Collectors.toList());
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
            WsAfterActionContext.class,
            handlerContextName,
            JavalinWsAfterActionContext.class,
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
