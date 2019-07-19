package io.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import io.javalin.http.Handler;
import io.javalin.plugin.openapi.dsl.OpenApiBuilder;
import io.javalin.plugin.openapi.dsl.OpenApiDocumentation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.PathParameter;
import io.javalin.mvc.api.*;
import io.javalin.mvc.JavalinHttpContext;
import io.javalin.mvc.api.openapi.*;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
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

    public CodeBlock getRouteMethodBody(ContainerSource container, String app, String hasOpenApi, int index) {
        CodeBlock.Builder handlerBuilder = CodeBlock.builder();
        handlerBuilder.beginControlFlow("$T handler$L = (ctx) ->", Handler.class, index);

        if (container.isFound()) {
            handlerBuilder.add("@SuppressWarnings($S)\n", "unchecked");
            handlerBuilder.addStatement("$T injector = ($T)scopeFactory.apply(ctx)", container.getType(), container.getType());
            handlerBuilder.addStatement("$T wrapper = injector.$L()", HttpContext.class, container.getDependencyName(HttpContext.class));
            handlerBuilder.addStatement("$T binder = injector.$L()", ModelBinder.class, container.getDependencyName(ModelBinder.class));
        } else {
            handlerBuilder.addStatement("$T wrapper = new $T(ctx)", HttpContext.class, JavalinHttpContext.class);
            handlerBuilder.addStatement("$T binder = new $T(wrapper.getRequest())", ModelBinder.class, DefaultModelBinder.class);
        }

        Name controllerName = container.getDependencyName(controller.getTypeName());
        if (controllerName != null) {
            handlerBuilder.addStatement("$N controller = injector.$L()", controller.getTypeName(), controllerName);
        } else {
            handlerBuilder.addStatement("$N controller = new $N()", controller.getTypeName(), controller.getTypeName());
        }

        List<BeforeGenerator> beforeGenerators = BeforeGenerator.getBeforeGenerators(container, this);
        generateBeforeHandlers(handlerBuilder, "wrapper", beforeGenerators, container.isFound() ? "injector" : null);
        List<AfterGenerator> afterGenerators = AfterGenerator.getAfterGenerators(container, this);
        if (afterGenerators.size() > 0) {
            handlerBuilder.addStatement("Exception caughtException = null;");
            handlerBuilder.beginControlFlow("try");
        }
        if (hasActionResultReturnType()) {
            handlerBuilder.addStatement(
                "$T result = controller.$N(" + bindParameters("wrapper") + ")",
                ActionResult.class,
                method.getSimpleName());
            handlerBuilder.addStatement("result.execute(wrapper)");
        } else if (isFutureReturnType(method)) {
            handlerBuilder.addStatement(
                "$T<?> future = controller.$N(" + bindParameters("wrapper") + ").thenApply(r -> r.executeAsync(wrapper))",
                CompletableFuture.class,
                method.getSimpleName());
            handlerBuilder.addStatement("ctx.result(future)");
        } else {
            handlerBuilder.addStatement(
                "controller.$N(" + bindParameters("wrapper") + ")",
                method.getSimpleName());
        }
        if (afterGenerators.size() > 0) {
            handlerBuilder.nextControlFlow("catch (Exception exception)");
            handlerBuilder.addStatement("caughtException = exception");
            handlerBuilder.endControlFlow();
            generateAfterHandlers(handlerBuilder, "wrapper", "caughtException", afterGenerators, container.isFound() ? "injector" : null);
        }
        handlerBuilder.endControlFlow();
        handlerBuilder.addStatement("");

        handlerBuilder.beginControlFlow("if ($N)", hasOpenApi);
        CodeBlock.Builder docsBuilder = CodeBlock.builder()
            .add("$T docs = $T.document()", OpenApiDocumentation.class, OpenApiBuilder.class);
        appendOpenApiDocs(docsBuilder);
        handlerBuilder.addStatement(docsBuilder.build());
        handlerBuilder.addStatement("handler$L = $T.documented(docs, handler$L)", index, OpenApiBuilder.class, index);
        handlerBuilder.endControlFlow();

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

    private boolean hasActionResultReturnType() {
        TypeMirror returnType = method.getReturnType();
        if (returnType.getKind() == TypeKind.VOID) {
            return false;
        }
        TypeMirror resultType = elementUtils.getTypeElement(ActionResult.class.getCanonicalName()).asType();
        return typeUtils.isSubtype(returnType, resultType);
    }

    private boolean isFutureReturnType(ExecutableElement method) {
        TypeMirror returnType = method.getReturnType();
        if (returnType.getKind() == TypeKind.VOID) {
            return false;
        }
        TypeMirror resultType = elementUtils.getTypeElement(ActionResult.class.getCanonicalName()).asType();
        TypeElement futureType = elementUtils.getTypeElement(CompletableFuture.class.getCanonicalName());
        DeclaredType futureResultType = typeUtils.getDeclaredType(futureType, resultType);
        return typeUtils.isSameType(returnType, futureResultType);
    }

    private String bindParameters(String contextName) {
        String[] arguments = method.getParameters().stream()
                .map(p -> ParameterGenerator.getParameterGenerator(this, p))
                .map(g -> g.generateParameter(contextName))
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

    private void appendOpenApiDocs(CodeBlock.Builder docsBuilder) {
        String summary = getOpenApiSummary();
        if (!StringUtils.isBlank(summary)) {
            docsBuilder.add(".operation((op) -> { op.summary($S); })", summary);
        }
        String description = getOpenApiDescription();
        if (!StringUtils.isBlank(description)) {
            docsBuilder.add(".operation((op) -> { op.description($S); })", description);
        }
        boolean isIgnored = getOpenApiIgnored();
        if (isIgnored) {
            docsBuilder.add(".ignore()");
        }
        String[] tags = getOpenApiTags();
        for (String tag : tags) {
            docsBuilder.add(".operation((op) -> { op.addTagsItem($S); })", tag);
        }
        OpenApiParameter[] pathParams = getOpenApiPathParams();
        for (OpenApiParameter param : pathParams) {
            docsBuilder.add(".pathParam($S, $T.class, (u) -> { ", param.getName(), param.getType());
            addParamSettings(docsBuilder, "u", param.getParameter());
            docsBuilder.add("})");
        }
        OpenApiParameter[] queryParams = getOpenApiQueryParams();
        for (OpenApiParameter param : queryParams) {
            docsBuilder.add(".queryParam($S, $T.class, (u) -> { ", param.getName(), param.getType());
            addParamSettings(docsBuilder, "u", param.getParameter());
            docsBuilder.add("})");
        }
        OpenApiParameter[] headerParams = getOpenApiHeaderParams();
        for (OpenApiParameter param : headerParams) {
            docsBuilder.add(".header($S, $T.class, (u) -> { ", param.getName(), param.getType());
            addParamSettings(docsBuilder, "u", param.getParameter());
            docsBuilder.add("})");
        }
        OpenApiParameter[] cookieParams = getOpenApiCookieParams();
        for (OpenApiParameter param : cookieParams) {
            docsBuilder.add(".cookie($S, $T.class, (u) -> { ", param.getName(), param.getType());
            addParamSettings(docsBuilder, "u", param.getParameter());
            docsBuilder.add("})");
        }
        OpenApiFileUpload[] uploads = getOpenApiFileUploads();
        for (OpenApiFileUpload upload : uploads) {
            docsBuilder.add(".uploadedFile($S, (u) -> { ", upload.name());
            if (upload.required()) {
                docsBuilder.add("u.setRequired(true); ");
            } else {
                docsBuilder.add("u.setRequired(false); ");
            }
            if (!StringUtils.isBlank(upload.description())) {
                docsBuilder.add("u.setDescription($S); ", upload.description());
            }
            docsBuilder.add("})");
        }
        OpenApiResponse[] responses = getOpenApiResponses();
        for (OpenApiResponse response : responses) {
            docsBuilder.add(".result($S", response.status());
            docsBuilder.add(", $T.class", getTypeMirror(response::modelType));
            if (!StringUtils.isBlank(response.mimeType())) {
                docsBuilder.add(", $S", response.mimeType());
            }
            if (!StringUtils.isBlank(response.description())) {
                docsBuilder.add(", (u) -> { u.setDescription($S); }", response.description());
            }
            docsBuilder.add(")");
        }
        OpenApiRequestBody body = findAnnotation(OpenApiRequestBody.class);
        if (body != null) {
            docsBuilder.add(".body($T.class", getTypeMirror(body::modelType));
            if (!StringUtils.isBlank(body.mimeType())) {
                docsBuilder.add(", $S", body.mimeType());
            }
            docsBuilder.add(", (u) -> { u.setRequired($L); ", body.required());
            if (!StringUtils.isBlank(body.description())) {
                docsBuilder.add("u.setDescription($S);", body.description());
            }
            docsBuilder.add("})");
        }
    }

    private static void addParamSettings(CodeBlock.Builder docsBuilder, String updater, Parameter param) {
        if (param.getRequired()) {
            docsBuilder.add("$N.required(true); ", updater);
        } else {
            docsBuilder.add("$N.required(false); ", updater);
        }
        if (param.getAllowEmptyValue()) {
            docsBuilder.add("$N.setAllowEmptyValue(true); ", updater);
        }
        if (param.getDeprecated()) {
            docsBuilder.add("$N.deprecated(true); ", updater);
        }
        if (!StringUtils.isBlank(param.getDescription())) {
            docsBuilder.add("$N.description($S); ", updater, param.getDescription());
        }
    }

    private String getOpenApiSummary() {
        OpenApiSummary annotation = findAnnotation(OpenApiSummary.class);
        return annotation == null ? null : annotation.value();
    }

    private String getOpenApiDescription() {
        OpenApiDescription annotation = findAnnotation(OpenApiDescription.class);
        return annotation == null ? null : annotation.value();
    }

    private boolean getOpenApiIgnored() {
        OpenApiIgnore annotation = findAnnotation(OpenApiIgnore.class);
        return annotation != null;
    }

    private String[] getOpenApiTags() {
        OpenApiTags annotation = findAnnotation(OpenApiTags.class);
        return annotation == null ? new String[0] : annotation.value();
    }

    private OpenApiParameter[] getOpenApiPathParams() {
        OpenApiPathParam single = findAnnotation(OpenApiPathParam.class);
        OpenApiPathParamContainer container = findAnnotation(OpenApiPathParamContainer.class);
        OpenApiPathParam[] multiple = container == null ? new OpenApiPathParam[0] : container.value();
        return Stream.concat(Stream.of(single), Stream.of(multiple))
            .filter(Objects::nonNull)
            .map(p -> {
                OpenApiParameter parameter = new OpenApiParameter();
                parameter.setName(p.name());
                parameter.setType(getTypeMirror(p::type));
                PathParameter pathParameter = new PathParameter();
                pathParameter.setRequired(p.required());
                pathParameter.setAllowEmptyValue(p.allowEmptyValue());
                pathParameter.setDeprecated(p.deprecated());
                pathParameter.setDescription(p.description());
                parameter.setParameter(pathParameter);
                return parameter;
            })
            .toArray(OpenApiParameter[]::new);
    }

    private OpenApiParameter[] getOpenApiQueryParams() {
        OpenApiQueryParam single = findAnnotation(OpenApiQueryParam.class);
        OpenApiQueryParamContainer container = findAnnotation(OpenApiQueryParamContainer.class);
        OpenApiQueryParam[] multiple = container == null ? new OpenApiQueryParam[0] : container.value();
        return Stream.concat(Stream.of(single), Stream.of(multiple))
                .filter(Objects::nonNull)
                .map(p -> {
                    OpenApiParameter parameter = new OpenApiParameter();
                    parameter.setName(p.name());
                    parameter.setType(getTypeMirror(p::type));
                    PathParameter pathParameter = new PathParameter();
                    pathParameter.setRequired(p.required());
                    pathParameter.setAllowEmptyValue(p.allowEmptyValue());
                    pathParameter.setDeprecated(p.deprecated());
                    pathParameter.setDescription(p.description());
                    parameter.setParameter(pathParameter);
                    return parameter;
                })
                .toArray(OpenApiParameter[]::new);
    }

    private OpenApiParameter[] getOpenApiHeaderParams() {
        OpenApiHeaderParam single = findAnnotation(OpenApiHeaderParam.class);
        OpenApiHeaderParamContainer container = findAnnotation(OpenApiHeaderParamContainer.class);
        OpenApiHeaderParam[] multiple = container == null ? new OpenApiHeaderParam[0] : container.value();
        return Stream.concat(Stream.of(single), Stream.of(multiple))
                .filter(Objects::nonNull)
                .map(p -> {
                    OpenApiParameter parameter = new OpenApiParameter();
                    parameter.setName(p.name());
                    parameter.setType(getTypeMirror(p::type));
                    PathParameter pathParameter = new PathParameter();
                    pathParameter.setRequired(p.required());
                    pathParameter.setAllowEmptyValue(p.allowEmptyValue());
                    pathParameter.setDeprecated(p.deprecated());
                    pathParameter.setDescription(p.description());
                    parameter.setParameter(pathParameter);
                    return parameter;
                })
                .toArray(OpenApiParameter[]::new);
    }

    private OpenApiParameter[] getOpenApiCookieParams() {
        OpenApiCookieParam single = findAnnotation(OpenApiCookieParam.class);
        OpenApiCookieParamContainer container = findAnnotation(OpenApiCookieParamContainer.class);
        OpenApiCookieParam[] multiple = container == null ? new OpenApiCookieParam[0] : container.value();
        return Stream.concat(Stream.of(single), Stream.of(multiple))
                .filter(Objects::nonNull)
                .map(p -> {
                    OpenApiParameter parameter = new OpenApiParameter();
                    parameter.setName(p.name());
                    parameter.setType(getTypeMirror(p::type));
                    PathParameter pathParameter = new PathParameter();
                    pathParameter.setRequired(p.required());
                    pathParameter.setAllowEmptyValue(p.allowEmptyValue());
                    pathParameter.setDeprecated(p.deprecated());
                    pathParameter.setDescription(p.description());
                    parameter.setParameter(pathParameter);
                    return parameter;
                })
                .toArray(OpenApiParameter[]::new);
    }

    private OpenApiFileUpload[] getOpenApiFileUploads() {
        OpenApiFileUpload single = findAnnotation(OpenApiFileUpload.class);
        OpenApiFileUploadContainer container = findAnnotation(OpenApiFileUploadContainer.class);
        OpenApiFileUpload[] multiple = container == null ? new OpenApiFileUpload[0] : container.value();
        return Stream.concat(Stream.of(single), Stream.of(multiple))
                .filter(Objects::nonNull)
                .toArray(OpenApiFileUpload[]::new);
    }

    private OpenApiResponse[] getOpenApiResponses() {
        OpenApiResponse single = findAnnotation(OpenApiResponse.class);
        OpenApiResponseContainer container = findAnnotation(OpenApiResponseContainer.class);
        OpenApiResponse[] multiple = container == null ? new OpenApiResponse[0] : container.value();
        return Stream.concat(Stream.of(single), Stream.of(multiple))
                .filter(Objects::nonNull)
                .toArray(OpenApiResponse[]::new);
    }

    private TypeMirror getTypeMirror(Supplier<Class<?>> supplier) {
        try {
            supplier.get();
        } catch (MirroredTypeException exception) {
            return exception.getTypeMirror();
        }
        return null;
    }

    private final static class OpenApiParameter {
        private String name;
        private TypeMirror type;
        private Parameter parameter;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public TypeMirror getType() {
            return type;
        }

        public void setType(TypeMirror type) {
            this.type = type;
        }

        public Parameter getParameter() {
            return parameter;
        }

        public void setParameter(Parameter parameter) {
            this.parameter = parameter;
        }
    }
}
