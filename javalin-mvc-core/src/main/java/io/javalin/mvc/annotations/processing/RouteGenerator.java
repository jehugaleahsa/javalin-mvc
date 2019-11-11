package io.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import io.javalin.http.Handler;
import io.javalin.mvc.DefaultModelBinder;
import io.javalin.mvc.api.*;
import io.javalin.mvc.JavalinHttpContext;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class RouteGenerator {
    private final ControllerSource controller;
    private final Types typeUtils;
    private final Elements elementUtils;
    private final ExecutableElement method;
    private final String methodType;
    private final String route;

    private RouteGenerator(ControllerSource controller, ExecutableElement method, String methodType, String route) {
        this.controller = controller;
        this.typeUtils = controller.getTypeUtils();
        this.elementUtils = controller.getElementUtils();
        this.method = method;
        this.methodType = methodType;
        this.route = route;
    }

    Types getTypeUtils() {
        return typeUtils;
    }

    Elements getElementUtils() {
        return elementUtils;
    }

    public static List<RouteGenerator> getGenerators(ControllerSource controller, ExecutableElement method) {
        return Stream.of(
            getGetCodeBlock(controller, method),
            getPostCodeBlock(controller, method),
            getPutCodeBlock(controller, method),
            getDeleteCodeBlock(controller, method),
            getPatchCodeBlock(controller, method),
            getHeadCodeBlock(controller, method),
            getConnectCodeBlock(controller, method),
            getOptionsCodeBlock(controller, method),
            getTraceCodeBlock(controller, method)
        ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static RouteGenerator getGetCodeBlock(ControllerSource controller, ExecutableElement method) {
        HttpGet annotation = method.getAnnotation(HttpGet.class);
        return annotation == null ? null : new RouteGenerator(controller, method, "get", annotation.route());
    }

    private static RouteGenerator getPostCodeBlock(ControllerSource controller, ExecutableElement method) {
        HttpPost annotation = method.getAnnotation(HttpPost.class);
        return annotation == null ? null : new RouteGenerator(controller, method, "post", annotation.route());
    }

    private static RouteGenerator getPutCodeBlock(ControllerSource controller, ExecutableElement method) {
        HttpPut annotation = method.getAnnotation(HttpPut.class);
        return annotation == null ? null : new RouteGenerator(controller, method, "put", annotation.route());
    }

    private static RouteGenerator getConnectCodeBlock(ControllerSource controller, ExecutableElement method) {
        HttpConnect annotation = method.getAnnotation(HttpConnect.class);
        return annotation == null ? null : new RouteGenerator(controller, method, "connect", annotation.route());
    }

    private static RouteGenerator getDeleteCodeBlock(ControllerSource controller, ExecutableElement method) {
        HttpDelete annotation = method.getAnnotation(HttpDelete.class);
        return annotation == null ? null : new RouteGenerator(controller, method, "delete", annotation.route());
    }

    private static RouteGenerator getHeadCodeBlock(ControllerSource controller, ExecutableElement method) {
        HttpHead annotation = method.getAnnotation(HttpHead.class);
        return annotation == null ? null : new RouteGenerator(controller, method, "head", annotation.route());
    }

    private static RouteGenerator getOptionsCodeBlock(ControllerSource controller, ExecutableElement method) {
        HttpOptions annotation = method.getAnnotation(HttpOptions.class);
        return annotation == null ? null : new RouteGenerator(controller, method, "options", annotation.route());
    }

    private static RouteGenerator getPatchCodeBlock(ControllerSource controller, ExecutableElement method) {
        HttpPatch annotation = method.getAnnotation(HttpPatch.class);
        return annotation == null ? null : new RouteGenerator(controller, method, "patch", annotation.route());
    }

    private static RouteGenerator getTraceCodeBlock(ControllerSource controller, ExecutableElement method) {
        HttpTrace annotation = method.getAnnotation(HttpTrace.class);
        return annotation == null ? null : new RouteGenerator(controller, method, "trace", annotation.route());
    }

    public <T extends Annotation> T findAnnotation(Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    public CodeBlock generateRoute(ContainerSource container, String app, int index) {
        CodeBlock.Builder handlerBuilder = CodeBlock.builder();
        handlerBuilder.beginControlFlow("$T handler$L = (ctx) ->", Handler.class, index);

        if (container.isFound()) {
            handlerBuilder.addStatement("$T injector = scopeFactory.get()", container.getType());
        }
        handlerBuilder.addStatement("$T wrapper = new $T(ctx)", HttpContext.class, JavalinHttpContext.class);
        handlerBuilder.addStatement("$T binder = new $T(wrapper.getRequest())", ModelBinder.class, DefaultModelBinder.class);

        Name controllerName = container.getDependencyName(controller.getType());
        if (controllerName != null) {
            handlerBuilder.addStatement("$T controller = injector.$L()", controller.getType(), controllerName);
        } else {
            handlerBuilder.addStatement("$T controller = new $T()", controller.getType(), controller.getType());
        }

        List<BeforeGenerator> beforeGenerators = BeforeGenerator.getBeforeGenerators(container, this);
        generateBeforeHandlers(handlerBuilder, "wrapper", beforeGenerators, container.isFound() ? "injector" : null);
        List<AfterGenerator> afterGenerators = AfterGenerator.getAfterGenerators(container, this);
        if (afterGenerators.size() > 0) {
            handlerBuilder.addStatement("Exception caughtException = null;");
            handlerBuilder.beginControlFlow("try");
        }
        if (hasVoidReturnType()) {
            handlerBuilder.addStatement(
                "controller.$N(" + bindParameters("ctx", "wrapper") + ")",
                method.getSimpleName());
        } else if (hasActionResultReturnType()) {
            handlerBuilder.addStatement(
                "$T result = controller.$N(" + bindParameters("ctx", "wrapper") + ")",
                ActionResult.class,
                method.getSimpleName());
            handlerBuilder.addStatement("result.execute(wrapper)");
        } else if (hasFutureActionResultReturnType()) {
            handlerBuilder.addStatement(
                "$T<?> future = controller.$N(" + bindParameters("ctx", "wrapper") + ").thenApply(r -> r.executeAsync(wrapper))",
                CompletableFuture.class,
                method.getSimpleName());
            handlerBuilder.addStatement("ctx.result(future)");
        } else if (hasFutureSimpleReturnType()) {
            handlerBuilder.addStatement(
                "$T<?> future = controller.$N(" + bindParameters("ctx", "wrapper") + ").thenApply(p -> new $T(p).executeAsync(wrapper))",
                CompletableFuture.class,
                method.getSimpleName(),
                JsonResult.class);
            handlerBuilder.addStatement("ctx.result(future)");
        } else {
            handlerBuilder.addStatement(
                "$T result = controller.$N(" + bindParameters("ctx", "wrapper") + ")",
                method.getReturnType(),
                method.getSimpleName());
            handlerBuilder.addStatement("new $T(result).execute(wrapper)", JsonResult.class);
        }
        if (afterGenerators.size() > 0) {
            handlerBuilder.nextControlFlow("catch (Exception exception)");
            handlerBuilder.addStatement("caughtException = exception");
            handlerBuilder.endControlFlow();
            generateAfterHandlers(handlerBuilder, "wrapper", "caughtException", afterGenerators, container.isFound() ? "injector" : null);
        }
        handlerBuilder.endControlFlow();
        handlerBuilder.addStatement("");

        handlerBuilder.addStatement(
            "handler$L = $T.moveDocumentationFromAnnotationToHandler($T.class, $S, handler$L)",
            index,
            io.javalin.plugin.openapi.dsl.OpenApiBuilder.class,
            controller.getType(),
            method.getSimpleName(),
            index);

        return CodeBlock.builder()
            .add(handlerBuilder.build())
            .addStatement("$N.$L($S, handler$L)", app, methodType, route, index)
            .build();
    }

    private static void generateBeforeHandlers(
            CodeBlock.Builder routeBuilder,
            String contextName,
            List<BeforeGenerator> generators,
            String injectorName) {
        for (BeforeGenerator generator : generators) {
            generator.generateBefore(routeBuilder, injectorName, contextName);
        }
    }

    private boolean hasVoidReturnType() {
        TypeMirror returnType = method.getReturnType();
        return returnType.getKind() == TypeKind.VOID;
    }

    private boolean hasActionResultReturnType() {
        TypeMirror returnType = method.getReturnType();
        TypeMirror resultType = elementUtils.getTypeElement(ActionResult.class.getCanonicalName()).asType();
        return typeUtils.isSubtype(returnType, resultType);
    }

    private boolean hasFutureActionResultReturnType() {
        TypeMirror returnType = method.getReturnType();
        TypeMirror resultType = elementUtils.getTypeElement(ActionResult.class.getCanonicalName()).asType();
        TypeElement futureType = elementUtils.getTypeElement(CompletableFuture.class.getCanonicalName());
        DeclaredType futureResultType = typeUtils.getDeclaredType(futureType, resultType);
        return typeUtils.isSubtype(returnType, futureResultType);
    }

    private boolean hasFutureSimpleReturnType() {
        TypeMirror returnType = typeUtils.erasure(method.getReturnType());
        TypeMirror futureType = typeUtils.erasure(
            elementUtils.getTypeElement(CompletableFuture.class.getCanonicalName()).asType());
        return typeUtils.isSubtype(returnType, futureType);
    }

    private String bindParameters(String context, String wrapper) {
        String[] arguments = method.getParameters().stream()
                .map(p -> ParameterGenerator.getParameterGenerator(this, p))
                .map(g -> g.generateParameter(context, wrapper))
                .toArray(String[]::new);
        return String.join(", ", arguments);
    }

    private static void generateAfterHandlers(
            CodeBlock.Builder routeBuilder,
            String contextName,
            String exceptionName,
            List<AfterGenerator> generators,
            String injectorName) {
        for (AfterGenerator generator : generators) {
            generator.generateAfter(routeBuilder, injectorName, contextName, exceptionName);
        }
        routeBuilder.beginControlFlow("if (caughtException != null)")
                .addStatement("throw caughtException")
                .endControlFlow();
    }
}
