package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsBinaryMessage;
import com.truncon.javalin.mvc.api.ws.WsBinaryMessageContext;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsDisconnectContext;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;
import com.truncon.javalin.mvc.ws.JavalinWsBinaryMessageContext;
import com.truncon.javalin.mvc.ws.JavalinWsConnectContext;
import com.truncon.javalin.mvc.ws.JavalinWsDisconnectContext;
import com.truncon.javalin.mvc.ws.JavalinWsErrorContext;
import com.truncon.javalin.mvc.ws.JavalinWsMessageContext;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

final class WsControllerSource {
    private final ContainerSource container;
    private final TypeUtils typeUtils;
    private final TypeElement controllerElement;

    private WsControllerSource(ContainerSource container, TypeElement controllerElement) {
        this.container = container;
        this.typeUtils = container.getTypeUtils();
        this.controllerElement = controllerElement;
    }

    public static List<WsControllerSource> getWsControllers(ContainerSource container, RoundEnvironment environment) throws ProcessingException {
        Set<? extends Element> controllerElements = environment.getElementsAnnotatedWith(WsController.class);
        checkControllerElements(controllerElements);
        return controllerElements.stream()
            .map(e -> (TypeElement) e)
            .map(e -> new WsControllerSource(container, e))
            .collect(Collectors.toList());
    }

    private static void checkControllerElements(Set<? extends Element> elements) throws ProcessingException {
        Element[] badElements = elements.stream()
            .filter(e -> e.getKind() != ElementKind.CLASS)
            .toArray(Element[]::new);
        if (badElements.length > 0) {
            throw new ProcessingException("WsController annotations can only be applied to classes.", badElements);
        }
        ProcessingException[] exceptions = elements.stream()
            .collect(Collectors.groupingBy(WsControllerSource::getRoute))
            .entrySet().stream()
            .filter(e -> e.getValue().size() > 1)
            .map(e -> new ProcessingException(
                "Multiple WebSocket controllers have the same route: " + e.getKey() + ".",
                e.getValue().toArray(new Element[0]))
            )
            .toArray(ProcessingException[]::new);
        if (exceptions.length > 0) {
            throw new ProcessingMultiException(exceptions);
        }
    }

    public CodeBlock generateRouteHandler(
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) throws ProcessingException {
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
        handlerBuilder.beginControlFlow("$N.ws($S, (ws) ->", ControllerRegistryGenerator.APP_NAME, getRoute());

        addOnConnectHandler(handlerBuilder, connectMethod, helperBuilder, converterLookup);
        addOnDisconnectHandler(handlerBuilder, disconnectMethod, helperBuilder, converterLookup);
        addOnErrorHandler(handlerBuilder, errorMethod, helperBuilder, converterLookup);
        addOnMessageHandler(handlerBuilder, messageMethod, helperBuilder, converterLookup);
        addOnBinaryMessageHandler(handlerBuilder, binaryMessageMethod, helperBuilder, converterLookup);

        handlerBuilder.endControlFlow(")");
        return handlerBuilder.build();
    }

    private <A extends Annotation> ExecutableElement getAnnotatedMethod(Class<A> annotationType) throws ProcessingException {
        List<ExecutableElement> methods = controllerElement.getEnclosedElements().stream()
            .filter(e -> e.getKind() == ElementKind.METHOD)
            .filter(e -> e.getAnnotation(annotationType) != null)
            .map(e -> (ExecutableElement) e)
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
        return getRoute(controllerElement);
    }

    private static String getRoute(Element element) {
        WsController annotation = element.getAnnotation(WsController.class);
        return annotation.route();
    }

    private void addOnConnectHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        addHandler(
            handlerBuilder,
            "onConnect",
            WsConnectContext.class,
            JavalinWsConnectContext.class,
            method,
            helperBuilder,
            converterLookup);
    }

    private void addOnDisconnectHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        addHandler(
            handlerBuilder,
            "onClose",
            WsDisconnectContext.class,
            JavalinWsDisconnectContext.class,
            method,
            helperBuilder,
            converterLookup);
    }

    private void addOnErrorHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        addHandler(
            handlerBuilder,
            "onError",
            WsErrorContext.class,
            JavalinWsErrorContext.class,
            method,
            helperBuilder,
            converterLookup);
    }

    private void addOnMessageHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        addHandler(
            handlerBuilder,
            "onMessage",
            WsMessageContext.class,
            JavalinWsMessageContext.class,
            method,
            helperBuilder,
            converterLookup);
    }

    private void addOnBinaryMessageHandler(
            CodeBlock.Builder handlerBuilder,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        addHandler(
            handlerBuilder,
            "onBinaryMessage",
            WsBinaryMessageContext.class,
            JavalinWsBinaryMessageContext.class,
            method,
            helperBuilder,
            converterLookup);
    }

    private void addHandler(
            CodeBlock.Builder handlerBuilder,
            String javalinHandler,
            Class<? extends WsContext> contextInterface,
            Class<?> contextImpl,
            ExecutableElement method,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        if (method == null) {
            return;
        }
        final String context = "ctx";
        handlerBuilder.beginControlFlow("ws.$N(($N) ->", javalinHandler, context);
        final String wrapper = "context";
        handlerBuilder.addStatement("$T $N = new $T(this.jsonMapper, $N)", contextInterface, wrapper, contextImpl, context);

        CodeBlock.Builder restBuilder = CodeBlock.builder();

        boolean injectorNeeded = addController(helperBuilder.getContainer(), restBuilder);

        List<WsBeforeGenerator> beforeGenerators = WsBeforeGenerator.getBeforeGenerators(container, method);
        List<WsAfterGenerator> afterGenerators = WsAfterGenerator.getAfterGenerators(container, method);
        String injector = "injector";
        boolean beforeInjectorNeeded = generateBeforeHandlers(
            restBuilder,
            wrapper,
            beforeGenerators,
            container.isFound() ? injector : null);
        injectorNeeded |= beforeInjectorNeeded;
        if (!afterGenerators.isEmpty()) {
            restBuilder.addStatement("Exception caughtException = null;");
            restBuilder.beginControlFlow("try");
        }

        ParameterResult parameterResult = ParameterGenerator.bindWsParameters(
            method,
            context,
            contextInterface,
            wrapper,
            injector,
            helperBuilder,
            converterLookup);
        MethodUtils methodUtils = new MethodUtils(typeUtils);
        if (methodUtils.hasVoidReturnType(method)) {
            restBuilder.addStatement("controller.$N(" + parameterResult.getArgumentList() + ")", method.getSimpleName());
        } else if (methodUtils.hasWsActionResultReturnType(method)) {
            restBuilder.addStatement(
                "$T result = controller.$N(" + parameterResult.getArgumentList() + ")",
                WsActionResult.class,
                method.getSimpleName());
            restBuilder.addStatement("result.execute($N)", wrapper);
        } else if (methodUtils.hasFutureWsActionResultReturnType(method)) {
            restBuilder.addStatement(
                "controller.$N(" + parameterResult.getArgumentList() + ").thenApply(r -> r.execute($N))",
                method.getSimpleName(),
                wrapper);
        } else if (methodUtils.hasFutureSimpleReturnType(method)) {
            restBuilder.addStatement(
                "controller.$N(" + parameterResult.getArgumentList() + ").thenApply(p -> new $T(p).execute($N))",
                method.getSimpleName(),
                WsJsonResult.class,
                wrapper);
        } else {
            restBuilder.addStatement(
                "$T result = controller.$N(" + parameterResult.getArgumentList() + ")",
                method.getReturnType(),
                method.getSimpleName());
            restBuilder.addStatement("new $T(result).execute($N)", WsJsonResult.class, wrapper);
        }
        injectorNeeded |= parameterResult.isInjectorNeeded();

        if (!afterGenerators.isEmpty()) {
            restBuilder.nextControlFlow("catch (Exception exception)");
            restBuilder.addStatement("caughtException = exception");
            restBuilder.endControlFlow();
            boolean afterInjectorNeeded = generateAfterHandlers(
                restBuilder,
                wrapper,
                "caughtException",
                afterGenerators,
                container.isFound() ? "injector" : null);
            injectorNeeded |= afterInjectorNeeded;
        }

        // only create injector if needed
        if (injectorNeeded) {
            handlerBuilder.addStatement("$T injector = $N.get()", container.getType(), ControllerRegistryGenerator.SCOPE_FACTORY_NAME);
        }
        handlerBuilder.add(restBuilder.build());

        handlerBuilder.endControlFlow(")");
    }

    private boolean addController(ContainerSource container, CodeBlock.Builder handlerBuilder) {
        Name controllerName = container.getDependencyName(controllerElement);
        if (container.isFound() && controllerName != null) {
            handlerBuilder.addStatement("$T controller = injector.$L()", controllerElement, controllerName);
            return true;
        } else {
            handlerBuilder.addStatement("$T controller = new $T()", controllerElement, controllerElement);
            return false;
        }
    }

    private boolean generateBeforeHandlers(
            CodeBlock.Builder handlerBuilder,
            String contextName,
            List<WsBeforeGenerator> generators,
            String injectorName) {
        boolean injectorNeeded = false;
        for (WsBeforeGenerator generator : generators) {
            boolean beforeInjectorNeeded = generator.generateBefore(handlerBuilder, injectorName, contextName);
            injectorNeeded |= beforeInjectorNeeded;
        }
        return injectorNeeded;
    }

    private boolean generateAfterHandlers(
            CodeBlock.Builder handlerBuilder,
            String contextName,
            String exceptionName,
            List<WsAfterGenerator> generators,
            String injectorName) {
        boolean injectorNeeded = false;
        for (WsAfterGenerator generator : generators) {
            boolean afterInjectorNeeded = generator.generateAfter(
                handlerBuilder,
                injectorName,
                contextName,
                exceptionName);
            injectorNeeded |= afterInjectorNeeded;
        }
        handlerBuilder.beginControlFlow("if (caughtException != null)")
            .addStatement("throw caughtException")
            .endControlFlow();
        return injectorNeeded;
    }
}
