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
import org.apache.commons.lang3.StringUtils;

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
        this.route = getFullRoute(controller, route);
    }

    private static String getFullRoute(ControllerSource controller, String route) {
        // We want to handle users including trailing slashes (/) in the prefix
        // and leading slashes in the action method route. We also need to handle
        // the possibility that only one or the other is provided.
        String prefix = controller.getPrefix();
        if (StringUtils.isBlank(prefix)) {
            return route;
        }
        if (StringUtils.isBlank(route)) {
            return prefix;
        }
        String trimmedPrefix = StringUtils.removeEnd(prefix, "/");
        String trimmedRoute = StringUtils.removeStart(route, "/");
        return trimmedPrefix + "/" + trimmedRoute;
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

        boolean injectorNeeded = false;
        CodeBlock.Builder restBuilder = CodeBlock.builder();
        restBuilder.addStatement("$T wrapper = new $T(ctx)", HttpContext.class, JavalinHttpContext.class);
        Name controllerName = container.getDependencyName(controller.getType());
        if (controllerName != null) {
            injectorNeeded = true;
            restBuilder.addStatement("$T controller = injector.$L()", controller.getType(), controllerName);
        } else {
            restBuilder.addStatement("$T controller = new $T()", controller.getType(), controller.getType());
        }

        List<BeforeGenerator> beforeGenerators = BeforeGenerator.getBeforeGenerators(container, this);
        boolean beforeInjectorNeeded = generateBeforeHandlers(
            restBuilder,
            "wrapper",
            beforeGenerators,
            container.isFound() ? "injector" : null);
        injectorNeeded |= beforeInjectorNeeded;
        List<AfterGenerator> afterGenerators = AfterGenerator.getAfterGenerators(container, this);
        if (afterGenerators.size() > 0) {
            restBuilder.addStatement("Exception caughtException = null;");
            restBuilder.beginControlFlow("try");
        }
        ParameterResult parameterResult = bindParameters("ctx", "wrapper", "injector", helperBuilder, converterLookup);
        MethodUtils methodUtils = new MethodUtils(typeUtils);
        if (methodUtils.hasVoidReturnType(method)) {
            restBuilder.addStatement(
                "controller.$N(" + parameterResult.getArgumentList() + ")",
                method.getSimpleName());
        } else if (methodUtils.hasActionResultReturnType(method)) {
            restBuilder.addStatement(
                "$T result = controller.$N(" + parameterResult.getArgumentList() + ")",
                ActionResult.class,
                method.getSimpleName());
            restBuilder.addStatement("result.execute(wrapper)");
        } else if (methodUtils.hasFutureActionResultReturnType(method)) {
            restBuilder.addStatement(
                "$T<?> future = controller.$N(" + parameterResult.getArgumentList() + ")",
                CompletableFuture.class,
                method.getSimpleName());
            restBuilder.addStatement(
                "$N.future(future, r -> (($T) r).execute($N))",
                "ctx",
                ActionResult.class,
                "wrapper");
        } else if (methodUtils.hasFutureSimpleReturnType(method)) {
            restBuilder.addStatement(
                "$T<?> future = controller.$N(" + parameterResult.getArgumentList() + ")",
                CompletableFuture.class,
                method.getSimpleName(),
                JsonResult.class);
            restBuilder.addStatement("$N.future(future)", "ctx");
        } else {
            restBuilder.addStatement(
                "$T result = controller.$N(" + parameterResult.getArgumentList() + ")",
                method.getReturnType(),
                method.getSimpleName());
            restBuilder.addStatement("new $T(result).execute($N)", JsonResult.class, "wrapper");
        }
        injectorNeeded |= parameterResult.isInjectorNeeded();

        if (afterGenerators.size() > 0) {
            restBuilder.nextControlFlow("catch (Exception exception)");
            restBuilder.addStatement("caughtException = exception");
            restBuilder.endControlFlow();
            boolean afterInjectorNeeded = generateAfterHandlers(
                restBuilder,
                "wrapper",
                "caughtException",
                afterGenerators,
                container.isFound() ? "injector" : null);
            injectorNeeded |= afterInjectorNeeded;
        }

        // Only create an injector if it is actually needed.
        if (container.isFound() && injectorNeeded) {
            handlerBuilder.addStatement("$T injector = $N.get()", container.getTypeMirror(), ControllerRegistryGenerator.SCOPE_FACTORY_NAME);
        }
        handlerBuilder.add(restBuilder.build());

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

    private static boolean generateBeforeHandlers(
            CodeBlock.Builder routeBuilder,
            String contextName,
            List<BeforeGenerator> generators,
            String injectorName) {
        boolean injectorNeeded = false;
        for (BeforeGenerator generator : generators) {
            boolean beforeInjectorNeeded = generator.generateBefore(routeBuilder, injectorName, contextName);
            injectorNeeded |= beforeInjectorNeeded;
        }
        return injectorNeeded;
    }

    private ParameterResult bindParameters(
            String context,
            String wrapper,
            String injector,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        return ParameterGenerator.bindParameters(
            method, context, wrapper, injector, helperBuilder, converterLookup);
    }

    private static boolean generateAfterHandlers(
            CodeBlock.Builder routeBuilder,
            String contextName,
            String exceptionName,
            List<AfterGenerator> generators,
            String injectorName) {
        boolean injectorNeeded = false;
        for (AfterGenerator generator : generators) {
            boolean afterInjectorNeeded = generator.generateAfter(
                routeBuilder,
                injectorName,
                contextName,
                exceptionName);
            injectorNeeded |= afterInjectorNeeded;
        }
        routeBuilder.beginControlFlow("if (caughtException != null)")
                .addStatement("throw caughtException")
                .endControlFlow();
        return injectorNeeded;
    }
}
