package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.ws.*;
import com.truncon.javalin.mvc.ws.*;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
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
        Set<? extends Element> controllerElements = environment.getElementsAnnotatedWith(WsController.class);
        checkControllerElements(controllerElements);
        return controllerElements.stream()
            .map(e -> (TypeElement)e)
            .map(e -> new WsControllerSource(typeUtils, elementUtils, e))
            .collect(Collectors.toList());
    }

    private static void checkControllerElements(Set<? extends Element> elements) throws ProcessingException {
        Element[] badElements = elements.stream()
            .filter(e -> e.getKind() != ElementKind.CLASS)
            .toArray(Element[]::new);
        if (badElements.length > 0) {
            throw new ProcessingException("WsController annotations can only be applied to classes.", badElements);
        }
    }

    public CodeBlock generateEndpoint(String app, HelperMethodBuilder helperBuilder) throws ProcessingException {
        ExecutableElement connectMethod = getAnnotatedMethod(WsConnect.class);
        ExecutableElement disconnectMethod = getAnnotatedMethod(WsDisconnect.class);
        ExecutableElement errorMethod = getAnnotatedMethod(WsError.class);
        ExecutableElement messageMethod = getAnnotatedMethod(WsMessage.class);
        ExecutableElement binaryMessageMethod = getAnnotatedMethod(WsBinaryMessage.class);
        if (connectMethod == null
            && disconnectMethod == null
            && errorMethod == null
            && messageMethod == null
            && binaryMessageMethod == null) {
            return null;
        }

        CodeBlock.Builder handlerBuilder = CodeBlock.builder();
        handlerBuilder.beginControlFlow("$N.ws($S, (ws) ->", app, getRoute());

        addOnConnectHandler(handlerBuilder, connectMethod, helperBuilder);
        addOnDisconnectHandler(handlerBuilder, disconnectMethod, helperBuilder);
        addOnErrorHandler(handlerBuilder, errorMethod, helperBuilder);
        addOnMessageHandler(handlerBuilder, messageMethod, helperBuilder);
        addOnBinaryMessageHandler(handlerBuilder, binaryMessageMethod, helperBuilder);

        handlerBuilder.endControlFlow(")");
        return handlerBuilder.build();
    }

    private <A extends Annotation> ExecutableElement getAnnotatedMethod(Class<A> annotationType) throws ProcessingException {
        List<ExecutableElement> methods = controllerElement.getEnclosedElements().stream()
            .filter(e -> e.getKind() == ElementKind.METHOD)
            .filter(e -> e.getAnnotation(annotationType) != null)
            .map(e -> (ExecutableElement)e)
            .collect(Collectors.toList());
        if (methods.isEmpty()) {
            return null;
        } else if (methods.size() > 1) {
            String message = "Only a single method can be annotated with the "
                + annotationType.getCanonicalName()
                + " annotation.";
            throw new ProcessingException(message, controllerElement);
        } else {
            return methods.get(0);
        }
    }

    private String getRoute() {
        WsController route = controllerElement.getAnnotation(WsController.class);
        return route.route();
    }

    private void addOnConnectHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder) {
        addHandler(
            handlerBuilder,
            "onConnect",
            WsConnectContext.class,
            JavalinWsConnectContext.class,
            method,
            helperBuilder);
    }

    private void addOnDisconnectHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder) {
        addHandler(
            handlerBuilder,
            "onClose",
            WsDisconnectContext.class,
            JavalinWsDisconnectContext.class,
            method,
            helperBuilder);
    }

    private void addOnErrorHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder) {
        addHandler(
            handlerBuilder,
            "onError",
            WsErrorContext.class,
            JavalinWsErrorContext.class,
            method,
            helperBuilder);
    }

    private void addOnMessageHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder) {
        addHandler(
            handlerBuilder,
            "onMessage",
            WsMessageContext.class,
            JavalinWsMessageContext.class,
            method,
            helperBuilder);
    }

    private void addOnBinaryMessageHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder) {
        addHandler(
            handlerBuilder,
            "onBinaryMessage",
            WsBinaryMessageContext.class,
            JavalinWsBinaryMessageContext.class,
            method,
            helperBuilder);
    }

    private void addHandler(
            CodeBlock.Builder handlerBuilder,
            String javalinHandler,
            Class<?> contextInterface,
            Class<?> contextImpl,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder) {
        if (method == null) {
            return;
        }
        final String context = "ctx";
        handlerBuilder.beginControlFlow("ws.$N(($N) ->", javalinHandler, context);
        final String wrapper = "context";
        handlerBuilder.addStatement("$T $N = new $T($N)", contextInterface, wrapper, contextImpl, context);
        addController(helperBuilder.getContainer(), handlerBuilder);
        String parameters = ParameterGenerator.bindWsParameters(
                method,
                context,
                contextInterface,
                wrapper,
                helperBuilder);
        MethodUtils methodUtils = new MethodUtils(typeUtils, elementUtils);
        if (methodUtils.hasVoidReturnType(method)) {
            handlerBuilder.addStatement("controller.$N(" + parameters + ")", method.getSimpleName());
        } else if (methodUtils.hasWsActionResultReturnType(method)) {
            handlerBuilder.addStatement(
                "$T result = controller.$N(" + parameters + ")",
                WsActionResult.class,
                method.getSimpleName());
            handlerBuilder.addStatement("result.execute($N)", wrapper);
        } else if (methodUtils.hasFutureWsActionResultReturnType(method)) {
            handlerBuilder.addStatement(
                "controller.$N(" + parameters + ").thenApply(r -> r.execute($N))",
                method.getSimpleName(),
                wrapper);
        } else if (methodUtils.hasFutureSimpleReturnType(method)) {
            handlerBuilder.addStatement(
                "controller.$N(" + parameters + ").thenApply(p -> new $T(p).execute($N))",
                method.getSimpleName(),
                WsJsonResult.class,
                wrapper);
        } else {
            handlerBuilder.addStatement(
                "$T result = controller.$N(" + parameters + ")",
                method.getReturnType(),
                method.getSimpleName());
            handlerBuilder.addStatement("new $T(result).execute($N)", WsJsonResult.class, wrapper);
        }
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
