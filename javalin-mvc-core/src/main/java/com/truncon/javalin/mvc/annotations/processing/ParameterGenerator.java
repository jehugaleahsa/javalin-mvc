package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.ConversionUtils;
import com.truncon.javalin.mvc.api.*;
import com.truncon.javalin.mvc.api.ws.*;
import io.javalin.http.Context;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

final class ParameterGenerator {
    private final Types typeUtils;
    private final Elements elementUtils;
    private final VariableElement parameter;

    private ParameterGenerator(Types typeUtils, Elements elementUtils, VariableElement parameter) {
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.parameter = parameter;
    }

    public static ParameterGenerator getParameterGenerator(Types types, Elements elements, VariableElement parameter) {
        return new ParameterGenerator(types, elements, parameter);
    }

    public static boolean isBinderNeeded(Types typeUtils, Elements elementUtils, ExecutableElement method) {
        return method.getParameters().stream()
            .map(p -> ParameterGenerator.getParameterGenerator(typeUtils, elementUtils, p))
            .anyMatch(ParameterGenerator::isBinderNeeded);
    }

    public static String bindParameters(
            Types typeUtils,
            Elements elementUtils,
            ExecutableElement method,
            String context,
            String wrapper) {
        String[] arguments = method.getParameters().stream()
            .map(p -> ParameterGenerator.getParameterGenerator(typeUtils, elementUtils, p))
            .map(g -> g.generateParameter(context, wrapper))
            .toArray(String[]::new);
        return String.join(", ", arguments);
    }

    public static boolean isWsBinderNeeded(
            Types typeUtils,
            Elements elementUtils,
            ExecutableElement method,
            Class<?> wrapperType) {
        return method.getParameters().stream()
            .map(p -> ParameterGenerator.getParameterGenerator(typeUtils, elementUtils, p))
            .anyMatch(p -> p.isWsBinderNeeded(wrapperType));
    }

    public static String bindWsParameters(
            Types typeUtils,
            Elements elementUtils,
            ExecutableElement method,
            String context,
            Class<?> wrapperType,
            String wrapper) {
        String[] arguments = method.getParameters().stream()
                .map(p -> ParameterGenerator.getParameterGenerator(typeUtils, elementUtils, p))
                .map(g -> g.generateWsParameter(context, wrapperType, wrapper))
                .toArray(String[]::new);
        return String.join(", ", arguments);
    }

    public VariableElement getParameter() {
        return parameter;
    }

    public boolean isBinderNeeded() {
        TypeMirror parameterType = parameter.asType();
        String parameter = getNonBinderParameter("context", "wrapper", parameterType);
        return StringUtils.isBlank(parameter);
    }

    public String generateParameter(String context, String wrapper) {
        TypeMirror parameterType = parameter.asType();
        String nonBinderParameter = getNonBinderParameter(context, wrapper, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            return nonBinderParameter;
        }
        String parameterName = getParameterName();
        if (isType(parameterType, FileUpload.class)) {
            return CodeBlock.of(wrapper + ".getRequest().getFile($S)", parameterName).toString();
        }
        ValueSource valueSource = getValueSource(parameter);
        for (Class<?> parameterClass : ConversionUtils.SUPPORTED_TYPES) {
            if (isType(parameterType, parameterClass)) {
                return bindParameter(parameterName, parameterClass, valueSource);
            } else {
                Class<?> arrayClass = getArrayClass(parameterClass);
                if (isType(parameterType, arrayClass)) {
                    return bindParameter(parameterName, arrayClass, valueSource);
                }
            }
        }
        return CodeBlock.of(
                "($T)binder.getValue($S, $T.class, $T.$L)",
                parameterType,
                parameterName,
                parameterType,
                ValueSource.class,
                valueSource).toString();
    }

    private String getNonBinderParameter(String context, String wrapper, TypeMirror parameterType) {
        if (isType(parameterType, Context.class)) {
            return context;
        } else if (isType(parameterType, HttpContext.class)) {
            return wrapper;
        } else if (isType(parameterType, HttpRequest.class)) {
            return wrapper + ".getRequest()";
        } else if (isType(parameterType, HttpResponse.class)) {
            return wrapper + ".getResponse()";
        } else {
            return null;
        }
    }

    public boolean isWsBinderNeeded(Class<?> wrapperType) {
        TypeMirror parameterType = parameter.asType();
        String parameter = getNonBinderWsParameter("context", wrapperType, "wrapper", parameterType);
        return StringUtils.isBlank(parameter);
    }

    public String generateWsParameter(String context, Class<?> wrapperType, String wrapper) {
        TypeMirror parameterType = parameter.asType();
        String nonBinderParameter = getNonBinderWsParameter(context, wrapperType, wrapper, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            return nonBinderParameter;
        }
        String parameterName = getParameterName();
        WsValueSource valueSource = getWsValueSource(parameter);
        for (Class<?> parameterClass : ConversionUtils.SUPPORTED_TYPES) {
            if (isType(parameterType, parameterClass)) {
                return bindWsParameter(parameterName, parameterClass, valueSource);
            } else {
                Class<?> arrayClass = getArrayClass(parameterClass);
                if (isType(parameterType, arrayClass)) {
                    return bindWsParameter(parameterName, arrayClass, valueSource);
                }
            }
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
            String context,
            Class<?> wrapperType,
            String wrapper,
            TypeMirror parameterType) {
        if (isBuiltInWsContextType(wrapperType, parameterType)) {
            return context;
        } else if (isType(parameterType, wrapperType)) {
            return wrapper;
        } else if (isType(parameterType, WsContext.class)) {
            return wrapper + ".getContext()";
        } else if (isType(parameterType, WsRequest.class)) {
            return wrapper + ".getContext().getRequest()";
        } else if (isType(parameterType, WsResponse.class)) {
            return wrapper + ".getContext().getResponse()";
        } else {
            return null;
        }
    }

    private boolean isBuiltInWsContextType(Class<?> wrapperType, TypeMirror parameterType) {
        if (isType(parameterType, io.javalin.websocket.WsContext.class)) {
            return true;
        } else if (wrapperType.equals(WsConnectContext.class)
                && isType(parameterType, io.javalin.websocket.WsConnectContext.class)) {
            return true;
        } else if (wrapperType.equals(WsDisconnectContext.class)
                && isType(parameterType, io.javalin.websocket.WsCloseContext.class)) {
            return true;
        } else if (wrapperType.equals(WsErrorContext.class)
                && isType(parameterType, io.javalin.websocket.WsErrorContext.class)) {
            return true;
        } else if (wrapperType.equals(WsMessageContext.class)
                && isType(parameterType, io.javalin.websocket.WsMessageContext.class)) {
            return true;
        } else if (wrapperType.equals(WsBinaryMessageContext.class)
                && isType(parameterType, io.javalin.websocket.WsBinaryMessageContext.class)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isType(TypeMirror parameterType, Class<?> type) {
        if (type == null) {
            return false;
        } else if (parameterType.getKind() == TypeKind.ARRAY) {
            return type.isArray() && isType(((ArrayType)parameterType).getComponentType(), type.getComponentType());
        } else {
            return !type.isArray() && typeUtils.isSameType(parameterType, elementUtils.getTypeElement(type.getCanonicalName()).asType());
        }
    }

    private Class<?> getArrayClass(Class<?> type) {
        try {
            return Class.forName("[L" + type.getCanonicalName() + ";");
        } catch (ClassNotFoundException e) {
            return null;
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

    private static String bindParameter(String parameterName, Class<?> parameterClass, ValueSource valueSource) {
        return CodeBlock.of(
                "($T)binder.getValue($S, $T.class, $T.$L)",
                parameterClass,
                parameterName,
                parameterClass,
                ValueSource.class,
                valueSource).toString();
    }

    private static String bindWsParameter(String parameterName, Class<?> parameterClass, WsValueSource valueSource) {
        return CodeBlock.of(
                "($T)binder.getValue($S, $T.class, $T.$L)",
                parameterClass,
                parameterName,
                parameterClass,
                WsValueSource.class,
                valueSource).toString();
    }
}
