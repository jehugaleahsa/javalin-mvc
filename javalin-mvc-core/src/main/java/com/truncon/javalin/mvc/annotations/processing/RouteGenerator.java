package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.HttpConnect;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpDelete;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.HttpHead;
import com.truncon.javalin.mvc.api.HttpOptions;
import com.truncon.javalin.mvc.api.HttpPatch;
import com.truncon.javalin.mvc.api.HttpPost;
import com.truncon.javalin.mvc.api.HttpPut;
import com.truncon.javalin.mvc.api.JsonResult;
import io.javalin.http.Handler;
import com.truncon.javalin.mvc.JavalinHttpContext;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class RouteGenerator {
    private final ControllerSource controller;
    private final TypeUtils typeUtils;
    private final ExecutableElement method;
    private final String methodType;
    private final String route;

    private RouteGenerator(ControllerSource controller, ExecutableElement method, String methodType, String route) {
        this.controller = controller;
        this.typeUtils = controller.getTypeUtils();
        this.method = method;
        this.methodType = methodType;
        this.route = route;
    }

    public String getQualifiedMethodName() {
        return controller.getControllerClassName() + "." + method.getSimpleName().toString();
    }

    public String getMethodType() {
        return methodType;
    }

    public String getRoute() {
        return route;
    }

    Element getMethodElement() {
        return method;
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
            getOptionsCodeBlock(controller, method)
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

    public <T extends Annotation> T findAnnotation(Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    public CodeBlock generateRouteHandler(
            int index,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        ContainerSource container = helperBuilder.getContainer();
        CodeBlock.Builder handlerBuilder = CodeBlock.builder();
        handlerBuilder.beginControlFlow("$T handler$L = (ctx) ->", Handler.class, index);

        // TODO - Only create an injector if it is actually needed.
        if (container.isFound()) {
            handlerBuilder.addStatement("$T injector = $N.get()", container.getType(), ControllerRegistryGenerator.SCOPE_FACTORY_NAME);
        }
        handlerBuilder.addStatement("$T wrapper = new $T(ctx)", HttpContext.class, JavalinHttpContext.class);
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
        String parameters = bindParameters("ctx", "wrapper", helperBuilder, converterLookup);
        MethodUtils methodUtils = new MethodUtils(typeUtils);
        if (methodUtils.hasVoidReturnType(method)) {
            handlerBuilder.addStatement(
                "controller.$N(" + parameters + ")",
                method.getSimpleName());
        } else if (methodUtils.hasActionResultReturnType(method)) {
            handlerBuilder.addStatement(
                "$T result = controller.$N(" + parameters + ")",
                ActionResult.class,
                method.getSimpleName());
            handlerBuilder.addStatement("result.execute(wrapper)");
        } else if (methodUtils.hasFutureActionResultReturnType(method)) {
            handlerBuilder.addStatement(
                "$T<?> future = controller.$N(" + parameters + ").thenApply(r -> r.executeAsync(wrapper))",
                CompletableFuture.class,
                method.getSimpleName());
            handlerBuilder.addStatement("ctx.result(future)");
        } else if (methodUtils.hasFutureSimpleReturnType(method)) {
            handlerBuilder.addStatement(
                "$T<?> future = controller.$N(" + parameters + ").thenApply(p -> new $T(p).executeAsync(wrapper))",
                CompletableFuture.class,
                method.getSimpleName(),
                JsonResult.class);
            handlerBuilder.addStatement("ctx.result(future)");
        } else {
            handlerBuilder.addStatement(
                "$T result = controller.$N(" + parameters + ")",
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
            .addStatement("$N.$L($S, handler$L)", ControllerRegistryGenerator.APP_NAME, methodType, route, index)
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

    private String bindParameters(
            String context,
            String wrapper,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        return ParameterGenerator.bindParameters(method, context, wrapper, helperBuilder, converterLookup);
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
