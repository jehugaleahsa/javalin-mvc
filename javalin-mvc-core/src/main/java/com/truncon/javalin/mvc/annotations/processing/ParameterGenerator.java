package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.*;
import com.truncon.javalin.mvc.api.ws.*;
import io.javalin.http.Context;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

final class ParameterGenerator {
    private final VariableElement parameter;

    private ParameterGenerator(VariableElement parameter) {
        this.parameter = parameter;
    }

    public static ParameterGenerator getParameterGenerator(VariableElement parameter) {
        return new ParameterGenerator(parameter);
    }

    public static boolean isBinderNeeded(TypeUtils typeUtils, ExecutableElement method) {
        return method.getParameters().stream()
            .map(ParameterGenerator::getParameterGenerator)
            .anyMatch(g -> g.isBinderNeeded(typeUtils));
    }

    public static String bindParameters(
            ExecutableElement method,
            String context,
            String wrapper,
            HelperMethodBuilder helperBuilder) {
        String[] arguments = method.getParameters().stream()
            .map(ParameterGenerator::getParameterGenerator)
            .map(g -> g.generateParameter(context, wrapper, helperBuilder))
            .toArray(String[]::new);
        return String.join(", ", arguments);
    }

    public static boolean isWsBinderNeeded(TypeUtils typeUtils, ExecutableElement method, Class<?> wrapperType) {
        return method.getParameters().stream()
            .map(ParameterGenerator::getParameterGenerator)
            .anyMatch(p -> p.isWsBinderNeeded(typeUtils, wrapperType));
    }

    public static String bindWsParameters(
            ExecutableElement method,
            String context,
            Class<?> wrapperType,
            String wrapper,
            HelperMethodBuilder helperBuilder) {
        String[] arguments = method.getParameters().stream()
                .map(ParameterGenerator::getParameterGenerator)
                .map(g -> g.generateWsParameter(context, wrapperType, wrapper, helperBuilder))
                .toArray(String[]::new);
        return String.join(", ", arguments);
    }

    public VariableElement getParameter() {
        return parameter;
    }

    public boolean isBinderNeeded(TypeUtils typeUtils) {
        TypeMirror parameterType = parameter.asType();
        if (typeUtils.isType(parameterType, Context.class)) {
            return false;
        } else if (typeUtils.isType(parameterType, HttpContext.class)) {
            return false;
        } else if (typeUtils.isType(parameterType, HttpRequest.class)) {
            return false;
        } else if (typeUtils.isType(parameterType, HttpResponse.class)) {
            return false;
        } else if (typeUtils.isType(parameterType, FileUpload.class)) {
            return false;
        } else {
            return true;
        }
    }

    public String generateParameter(String context, String wrapper, HelperMethodBuilder helperBuilder) {
        TypeMirror parameterType = parameter.asType();
        TypeUtils typeUtils = helperBuilder.getContainer().getTypeUtils();
        String nonBinderParameter = getNonBinderParameter(context, wrapper, typeUtils, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            return nonBinderParameter;
        }
        String parameterName = getParameterName();
        ValueSource valueSource = getValueSource(parameter);
        // First check if we can provide a simple converter for the parameter type.
        Class<?> parameterClass = helperBuilder.getParameterClass(parameterType);
        if (parameterClass != null) {
            Class<?> actualClass = parameterClass.isArray()
                ? parameterClass.getComponentType() // NOTE: We pass the component type to the helper builder, not the array type
                : parameterClass;
            String conversionMethod = helperBuilder.addConversionMethod(actualClass, parameterClass.isArray());
            if (conversionMethod != null) {
                String sourceMethod = helperBuilder.addSourceMethod(valueSource, parameterClass.isArray());
                return CodeBlock.builder()
                    .add("$N($N($N, $S))", conversionMethod, sourceMethod, wrapper, parameterName)
                    .build()
                    .toString();
            }
        }

        // Next, check if this is an object with a @From* annotation or if one of the type's
        // public members has a @From* annotation decorating it.
        ValueSource defaultBinding = HelperMethodBuilder.getDefaultFromBinding(parameter);
        if (defaultBinding != ValueSource.Any || helperBuilder.hasMemberBinding(parameter)) {
            TypeElement element = typeUtils.getTypeElement(parameterType);
            String conversionMethod = helperBuilder.addConversionMethod(element, defaultBinding);
            return CodeBlock.builder()
                .add("$N($N)", conversionMethod, wrapper)
                .build()
                .toString();
        }

        // Lastly, assume the type should be converted using JSON.
        return CodeBlock.of(
                "($T)binder.getValue($S, $T.class, $T.$L)",
                parameterType,
                parameterName,
                parameterType,
                ValueSource.class,
                valueSource).toString();
    }

    private String getNonBinderParameter(String context, String wrapper, TypeUtils typeUtils, TypeMirror parameterType) {
        if (typeUtils.isType(parameterType, Context.class)) {
            return context;
        } else if (typeUtils.isType(parameterType, HttpContext.class)) {
            return wrapper;
        } else if (typeUtils.isType(parameterType, HttpRequest.class)) {
            return wrapper + ".getRequest()";
        } else if (typeUtils.isType(parameterType, HttpResponse.class)) {
            return wrapper + ".getResponse()";
        } else if (typeUtils.isType(parameterType, FileUpload.class)) {
            return CodeBlock.of(wrapper + ".getRequest().getFile($S)", getParameterName()).toString();
        } else {
            return null;
        }
    }

    public boolean isWsBinderNeeded(TypeUtils typeUtils, Class<?> wrapperType) {
        TypeMirror parameterType = parameter.asType();
        String parameter = getNonBinderWsParameter(typeUtils, "context", wrapperType, "wrapper", parameterType);
        return StringUtils.isBlank(parameter);
    }

    public String generateWsParameter(String context, Class<?> wrapperType, String wrapper, HelperMethodBuilder helperBuilder) {
        TypeMirror parameterType = parameter.asType();
        TypeUtils typeUtils = helperBuilder.getContainer().getTypeUtils();
        String nonBinderParameter = getNonBinderWsParameter(typeUtils, context, wrapperType, wrapper, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            return nonBinderParameter;
        }

        // First check if we can provide a simple converter for the parameter type.
        String parameterName = getParameterName();
        WsValueSource valueSource = getWsValueSource(parameter);
        Class<?> parameterClass = helperBuilder.getParameterClass(parameterType);
        if (parameterClass != null) {
            Class<?> actualClass = parameterClass.isArray()
                ? parameterClass.getComponentType() // NOTE: We pass the component type to the helper builder, not the array type
                : parameterClass;
            String conversionMethod = helperBuilder.addConversionMethod(actualClass, parameterClass.isArray());
            if (conversionMethod != null) {
                String sourceMethod = helperBuilder.addSourceMethod(valueSource, wrapperType, parameterClass.isArray());
                return CodeBlock.builder()
                    .add("$N($N($N, $S))", conversionMethod, sourceMethod, wrapper, parameterName)
                    .build()
                    .toString();
            }
        }

        // Next, check if this is an object with a @From* annotation or if one of the type's
        // public members has a @From* annotation decorating it.
        WsValueSource defaultBinding = HelperMethodBuilder.getDefaultWsFromBinding(parameter);
        if (defaultBinding != WsValueSource.Any || helperBuilder.hasWsMemberBinding(parameter)) {
            TypeElement element = typeUtils.getTypeElement(parameterType);
            String conversionMethod = helperBuilder.addConversionMethod(element, defaultBinding, wrapperType);
            return CodeBlock.builder()
                .add("$N($N)", conversionMethod, wrapper)
                .build()
                .toString();
        }

        return CodeBlock.of(
                "($T)binder.getValue($S, $T.class, $T.$L)",
                parameterType,
                parameterName,
                parameterType,
                WsValueSource.class,
                valueSource).toString();
    }

    private String getNonBinderWsParameter(
            TypeUtils typeUtils,
            String context,
            Class<?> wrapperType,
            String wrapper,
            TypeMirror parameterType) {
        if (isBuiltInWsContextType(typeUtils, wrapperType, parameterType)) {
            return context;
        } else if (typeUtils.isType(parameterType, wrapperType)) {
            return wrapper;
        } else if (typeUtils.isType(parameterType, WsContext.class)) {
            return wrapper + ".getContext()";
        } else if (typeUtils.isType(parameterType, WsRequest.class)) {
            return wrapper + ".getContext().getRequest()";
        } else if (typeUtils.isType(parameterType, WsResponse.class)) {
            return wrapper + ".getContext().getResponse()";
        } else {
            return null;
        }
    }

    private boolean isBuiltInWsContextType(TypeUtils typeUtils, Class<?> wrapperType, TypeMirror parameterType) {
        if (typeUtils.isType(parameterType, io.javalin.websocket.WsContext.class)) {
            return true;
        } else if (wrapperType.equals(WsConnectContext.class)
            && typeUtils.isType(parameterType, io.javalin.websocket.WsConnectContext.class)) {
            return true;
        } else if (wrapperType.equals(WsDisconnectContext.class)
            && typeUtils.isType(parameterType, io.javalin.websocket.WsCloseContext.class)) {
            return true;
        } else if (wrapperType.equals(WsErrorContext.class)
            && typeUtils.isType(parameterType, io.javalin.websocket.WsErrorContext.class)) {
            return true;
        } else if (wrapperType.equals(WsMessageContext.class)
            && typeUtils.isType(parameterType, io.javalin.websocket.WsMessageContext.class)) {
            return true;
        } else if (wrapperType.equals(WsBinaryMessageContext.class)
            && typeUtils.isType(parameterType, io.javalin.websocket.WsBinaryMessageContext.class)) {
            return true;
        } else {
            return false;
        }
    }

    public String getParameterName() {
        Named annotation = parameter.getAnnotation(Named.class);
        if (annotation == null) {
            return parameter.getSimpleName().toString();
        }
        String annotatedName = annotation.value();
        return annotatedName.trim();
    }

    private ValueSource getValueSource(VariableElement parameter) {
        if (parameter.getAnnotation(FromHeader.class) != null) {
            return ValueSource.Header;
        }
        if (parameter.getAnnotation(FromCookie.class) != null) {
            return ValueSource.Cookie;
        }
        if (parameter.getAnnotation(FromPath.class) != null) {
            return ValueSource.Path;
        }
        if (parameter.getAnnotation(FromQuery.class) != null) {
            return ValueSource.QueryString;
        }
        if (parameter.getAnnotation(FromForm.class) != null) {
            return ValueSource.FormData;
        }
        return ValueSource.Any;
    }

    private WsValueSource getWsValueSource(VariableElement parameter) {
        if (parameter.getAnnotation(FromMessage.class) != null) {
            return WsValueSource.Message;
        }
        if (parameter.getAnnotation(FromHeader.class) != null) {
            return WsValueSource.Header;
        }
        if (parameter.getAnnotation(FromCookie.class) != null) {
            return WsValueSource.Cookie;
        }
        if (parameter.getAnnotation(FromPath.class) != null) {
            return WsValueSource.Path;
        }
        if (parameter.getAnnotation(FromQuery.class) != null) {
            return WsValueSource.QueryString;
        }
        return WsValueSource.Any;
    }
}
