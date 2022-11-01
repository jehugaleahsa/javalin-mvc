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
import com.truncon.javalin.mvc.JavalinHttpContext;
import io.javalin.openapi.OpenApi;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
            getGetRouteGenerator(controller, method),
            getPostRouteGenerator(controller, method),
            getPutRouteGenerator(controller, method),
            getDeleteRouteGenerator(controller, method),
            getPatchRouteGenerator(controller, method),
            getHeadRouteGenerator(controller, method),
            getConnectRouteGenerator(controller, method),
            getOptionsRouteGenerator(controller, method)
        )
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private static RouteGenerator getGetRouteGenerator(ControllerSource controller, ExecutableElement method) {
        HttpGet builtin = method.getAnnotation(HttpGet.class);
        if (builtin != null) {
            return new RouteGenerator(controller, method, "get", builtin.route());
        }
        return getRouteGeneratorByStandardAnnotation(
            controller, method, jakarta.ws.rs.GET.class, "get", jakarta.ws.rs.HttpMethod.GET);
    }

    private static RouteGenerator getPostRouteGenerator(ControllerSource controller, ExecutableElement method) {
        HttpPost builtin = method.getAnnotation(HttpPost.class);
        if (builtin != null) {
            return new RouteGenerator(controller, method, "post", builtin.route());
        }
        return getRouteGeneratorByStandardAnnotation(
            controller, method, jakarta.ws.rs.POST.class, "post", jakarta.ws.rs.HttpMethod.POST);
    }

    private static RouteGenerator getPutRouteGenerator(ControllerSource controller, ExecutableElement method) {
        HttpPut builtin = method.getAnnotation(HttpPut.class);
        if (builtin != null) {
            return new RouteGenerator(controller, method, "put", builtin.route());
        }
        return getRouteGeneratorByStandardAnnotation(
            controller, method, jakarta.ws.rs.PUT.class, "put", jakarta.ws.rs.HttpMethod.PUT);
    }

    private static RouteGenerator getDeleteRouteGenerator(ControllerSource controller, ExecutableElement method) {
        HttpDelete builtin = method.getAnnotation(HttpDelete.class);
        if (builtin != null) {
            return new RouteGenerator(controller, method, "delete", builtin.route());
        }
        return getRouteGeneratorByStandardAnnotation(
            controller, method, jakarta.ws.rs.DELETE.class, "delete", jakarta.ws.rs.HttpMethod.DELETE);
    }

    private static RouteGenerator getPatchRouteGenerator(ControllerSource controller, ExecutableElement method) {
        HttpPatch builtin = method.getAnnotation(HttpPatch.class);
        if (builtin != null) {
            return new RouteGenerator(controller, method, "patch", builtin.route());
        }
        return getRouteGeneratorByStandardAnnotation(
            controller, method, jakarta.ws.rs.PATCH.class, "patch", jakarta.ws.rs.HttpMethod.PATCH);
    }

    private static RouteGenerator getHeadRouteGenerator(ControllerSource controller, ExecutableElement method) {
        HttpHead builtin = method.getAnnotation(HttpHead.class);
        if (builtin != null) {
            return new RouteGenerator(controller, method, "head", builtin.route());
        }
        return getRouteGeneratorByStandardAnnotation(
            controller, method, jakarta.ws.rs.HEAD.class, "head", jakarta.ws.rs.HttpMethod.HEAD);
    }

    private static RouteGenerator getConnectRouteGenerator(ControllerSource controller, ExecutableElement method) {
        HttpConnect builtin = method.getAnnotation(HttpConnect.class);
        if (builtin != null) {
            return new RouteGenerator(controller, method, "connect", builtin.route());
        }
        return getRouteGeneratorForHttpMethod(controller, method, "connect", "CONNECT");
    }

    private static RouteGenerator getOptionsRouteGenerator(ControllerSource controller, ExecutableElement method) {
        HttpOptions builtin = method.getAnnotation(HttpOptions.class);
        if (builtin != null) {
            return new RouteGenerator(controller, method, "options", builtin.route());
        }
        return getRouteGeneratorByStandardAnnotation(
            controller, method, jakarta.ws.rs.OPTIONS.class, "options", jakarta.ws.rs.HttpMethod.OPTIONS);
    }

    private static RouteGenerator getRouteGeneratorByStandardAnnotation(
            ControllerSource controller,
            ExecutableElement method,
            Class<? extends Annotation> clz,
            String methodType,
            String methodValue) {
        Annotation annotation = method.getAnnotation(clz);
        if (annotation != null) {
            return getRouteGeneratorForAssociatedPath(controller, method, methodType);
        }
        return getRouteGeneratorForHttpMethod(controller, method, methodType, methodValue);
    }

    private static RouteGenerator getRouteGeneratorForHttpMethod(
            ControllerSource controller,
            ExecutableElement method,
            String methodType,
            String methodValue) {
        jakarta.ws.rs.HttpMethod httpMethod = method.getAnnotation(jakarta.ws.rs.HttpMethod.class);
        if (httpMethod != null && methodValue.equals(httpMethod.value())) {
            return getRouteGeneratorForAssociatedPath(controller, method, methodType);
        }
        return null;
    }

    private static RouteGenerator getRouteGeneratorForAssociatedPath(
            ControllerSource controller,
            ExecutableElement method,
            String methodType) {
        jakarta.ws.rs.Path path = method.getAnnotation(jakarta.ws.rs.Path.class);
        String route = path == null ? null : path.value();
        return new RouteGenerator(controller, method, methodType, route);
    }

    public <T extends Annotation> T findAnnotation(Class<T> annotationClass) {
        return method.getAnnotation(annotationClass);
    }

    public CodeBlock generateRouteHandler(
            int index,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        CodeBlock.Builder restBuilder = CodeBlock.builder();
        restBuilder.addStatement("$T wrapper = new $T(ctx)", HttpContext.class, JavalinHttpContext.class);
        ContainerSource container = helperBuilder.getContainer();
        boolean injectorNeeded = addControllerCreation(restBuilder, container);

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
                "controller.$N($L)",
                method.getSimpleName(),
                parameterResult.getArgumentList()
            );
        } else if (methodUtils.hasActionResultReturnType(method)) {
            restBuilder.addStatement(
                "$T result = controller.$N($L)",
                ActionResult.class,
                method.getSimpleName(),
                parameterResult.getArgumentList()
            );
            restBuilder.addStatement("result.execute(wrapper)");
        } else if (methodUtils.hasFutureVoidReturnType(method)) {
            restBuilder.addStatement(
                "$N.future(() -> controller.$N($L))",
                "ctx",
                method.getSimpleName(),
                parameterResult.getArgumentList()
            );
        } else if (methodUtils.hasFutureActionResultReturnType(method)) {
            restBuilder.addStatement(
                "$N.future(() -> controller.$N($L).thenAccept(r -> r.execute($N)))",
                "ctx",
                method.getSimpleName(),
                parameterResult.getArgumentList(),
                "wrapper"
            );
        } else if (methodUtils.hasFutureReturnType(method)) {
            // Since the return type can be any reference type, we must first cast to
            // Object to avoid potential compiler errors.
            restBuilder.addStatement(
                "$N.future(() -> controller.$N($L).thenAccept(r -> ((Object) r instanceof $T ? ($T)(Object) r : new $T(r)).execute($N)))",
                "ctx",
                method.getSimpleName(),
                parameterResult.getArgumentList(),
                ActionResult.class,
                ActionResult.class,
                JsonResult.class,
                "wrapper"
            );
        }  else {
            restBuilder.addStatement(
                "Object result = controller.$N($L)",
                method.getSimpleName(),
                parameterResult.getArgumentList()
            );
            restBuilder.addStatement("(result instanceof $T ? ($T) result : new $T(result)).execute($N)",
                ActionResult.class,
                ActionResult.class,
                JsonResult.class,
                "wrapper");
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
        CodeBlock.Builder handlerBuilder = CodeBlock.builder();
        if (injectorNeeded) {
            handlerBuilder.addStatement("$T injector = $N.get()", container.getInjectorType(), ControllerRegistryGenerator.SCOPE_FACTORY_NAME);
        }
        handlerBuilder.add(restBuilder.build());

        String methodName = "httpHandler" + index;
        OpenApi openApiAnnotation = method.getAnnotation(OpenApi.class);
        helperBuilder.addRouteHandler(methodName, handlerBuilder.build(), openApiAnnotation);

        return CodeBlock.builder()
            .addStatement(
                "$N.$L($S, this::$N)",
                ControllerRegistryGenerator.APP_NAME,
                methodType,
                route,
                methodName
            )
            .build();
    }

    private boolean addControllerCreation(CodeBlock.Builder restBuilder, ContainerSource container) {
        InjectionResult result = container.getInstanceCall(controller.getType().asType(), "injector");
        restBuilder.addStatement("$T controller = $L", controller.getType(), result.getInstanceCall());
        return result.isInjectorNeeded();
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
