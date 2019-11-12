package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.ws.*;
import com.truncon.javalin.mvc.ws.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final class WsControllerSource {
    private final Types typeUtils;
    private final Elements elementUtils;
    private final TypeElement controllerElement;

    private WsControllerSource(Types typeUtils, Elements elementUtils, TypeElement controllerElement) {
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.controllerElement = controllerElement;
    }

    public static List<WsControllerSource> getWsControllers(Types typeUtils, Elements elementUtils, RoundEnvironment environment) throws ProcessingException {
        Set<? extends Element> controllerElements = environment.getElementsAnnotatedWith(WsRoute.class);
        checkControllerElements(controllerElements);
        return controllerElements.stream()
            .map(e -> (TypeElement)e)
            .filter(e -> isWsController(typeUtils, elementUtils, e))
            .map(e -> new WsControllerSource(typeUtils, elementUtils, e))
            .collect(Collectors.toList());
    }

    private static void checkControllerElements(Set<? extends Element> elements) throws ProcessingException {
        Element[] badElements = elements.stream()
            .filter(e -> e.getKind() != ElementKind.CLASS)
            .toArray(Element[]::new);
        if (badElements.length > 0) {
            throw new ProcessingException("WsRoute annotations can only be applied to classes.", badElements);
        }
    }

    private static boolean isWsController(Types typeUtils, Elements elementUtils, TypeElement element) {
        TypeElement controllerElement = elementUtils.getTypeElement(WsController.class.getCanonicalName());
        return typeUtils.isAssignable(element.asType(), controllerElement.asType());
    }

    public CodeBlock generateEndpoint(ContainerSource container, String app) {
        CodeBlock.Builder handlerBuilder = CodeBlock.builder();
        handlerBuilder.beginControlFlow("$N.ws($S, (ws) ->", app, getRoute());

        addOnConnectHandler(container, handlerBuilder);
        addOnDisconnectHandler(container, handlerBuilder);
        addOnErrorHandler(container, handlerBuilder);
        addOnMessageHandler(container, handlerBuilder);
        addOnBinaryMessageHandler(container, handlerBuilder);

        handlerBuilder.endControlFlow(")");
        return handlerBuilder.build();
    }

    private String getRoute() {
        WsRoute route = controllerElement.getAnnotation(WsRoute.class);
        return route.route();
    }

    private void addOnConnectHandler(ContainerSource container, CodeBlock.Builder handlerBuilder) {
        addHandler(
            container,
            handlerBuilder,
            "onConnect",
            WsConnectContext.class,
            JavalinWsConnectContext.class,
            "onConnect");
    }

    private void addOnDisconnectHandler(ContainerSource container, CodeBlock.Builder handlerBuilder) {
        addHandler(
            container,
            handlerBuilder,
            "onClose",
            WsDisconnectContext.class,
            JavalinWsDisconnectContext.class,
            "onDisconnect");
    }

    private void addOnErrorHandler(ContainerSource container, CodeBlock.Builder handlerBuilder) {
        addHandler(
            container,
            handlerBuilder,
            "onError",
            WsErrorContext.class,
            JavalinWsErrorContext.class,
            "onError");
    }

    private void addOnMessageHandler(ContainerSource container, CodeBlock.Builder handlerBuilder) {
        addHandler(
            container,
            handlerBuilder,
            "onMessage",
            WsMessageContext.class,
            JavalinWsMessageContext.class,
            "onMessage");
    }

    private void addOnBinaryMessageHandler(ContainerSource container, CodeBlock.Builder handlerBuilder) {
        addHandler(
            container,
            handlerBuilder,
            "onBinaryMessage",
            WsBinaryMessageContext.class,
            JavalinWsBinaryMessageContext.class,
            "onBinaryMessage");
    }

    private void addHandler(
            ContainerSource container,
            CodeBlock.Builder handlerBuilder,
            String javalinHandler,
            Class<?> contextInterface,
            Class<?> contextImpl,
            String methodName) {
        handlerBuilder.beginControlFlow("ws.$N((ctx) ->", javalinHandler);
        handlerBuilder.addStatement("$T context = new $T(ctx)", contextInterface, contextImpl);
        addController(container, handlerBuilder);
        handlerBuilder.addStatement("controller.$N(context)", methodName);
        handlerBuilder.endControlFlow(")");
    }

    private void addController(ContainerSource container, CodeBlock.Builder handlerBuilder) {
        Name controllerName = container.getDependencyName(controllerElement);
        if (container.isFound() && controllerName != null) {
            handlerBuilder.addStatement("$T injector = scopeFactory.get()", container.getType());
            handlerBuilder.addStatement("$T controller = injector.$L()", controllerElement, controllerName);
        } else {
            handlerBuilder.addStatement("$T controller = new $T()", controllerElement, controllerElement);
        }
    }
}
