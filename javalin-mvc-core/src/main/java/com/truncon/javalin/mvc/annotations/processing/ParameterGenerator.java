package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.ConversionUtils;
import com.truncon.javalin.mvc.api.*;
import com.truncon.javalin.mvc.api.ws.*;
import io.javalin.http.Context;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
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
            String wrapper,
            HelperMethodBuilder helperBuilder) {
        String[] arguments = method.getParameters().stream()
            .map(p -> ParameterGenerator.getParameterGenerator(typeUtils, elementUtils, p))
            .map(g -> g.generateParameter(context, wrapper, helperBuilder))
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
            String wrapper,
            HelperMethodBuilder helperBuilder) {
        String[] arguments = method.getParameters().stream()
                .map(p -> ParameterGenerator.getParameterGenerator(typeUtils, elementUtils, p))
                .map(g -> g.generateWsParameter(context, wrapperType, wrapper, helperBuilder))
                .toArray(String[]::new);
        return String.join(", ", arguments);
    }

    public VariableElement getParameter() {
        return parameter;
    }

    public boolean isBinderNeeded() {
        TypeMirror parameterType = parameter.asType();
        if (isType(parameterType, Context.class)) {
            return false;
        } else if (isType(parameterType, HttpContext.class)) {
            return false;
        } else if (isType(parameterType, HttpRequest.class)) {
            return false;
        } else if (isType(parameterType, HttpResponse.class)) {
            return false;
        } else if (isType(parameterType, FileUpload.class)) {
            return false;
        } else {
            return true;
        }
    }

    public String generateParameter(String context, String wrapper, HelperMethodBuilder helperBuilder) {
        TypeMirror parameterType = parameter.asType();
        String nonBinderParameter = getNonBinderParameter(context, wrapper, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            return nonBinderParameter;
        }
        String parameterName = getParameterName();
        ValueSource valueSource = getValueSource(parameter);
        // First check if we can provide a simple converter for the parameter type.
        for (Class<?> parameterClass : HelperMethodBuilder.CONVERSION_HELPER_LOOKUP.keySet()) {
            if (isType(parameterType, parameterClass)) {
                String conversionMethod = helperBuilder.addConversionMethod(parameterClass, false);
                if (conversionMethod != null) {
                    String sourceMethod = helperBuilder.addSourceMethod(valueSource, false);
                    return CodeBlock.builder()
                        .add("$N($N($N, $S))", conversionMethod, sourceMethod, wrapper, parameterName)
                        .build()
                        .toString();
                }
            } else {
                Class<?> arrayClass = getArrayClass(parameterClass);
                if (isType(parameterType, arrayClass)) {
                    // NOTE: We pass the component type to the helper builder, not the array type.
                    String conversionMethod = helperBuilder.addConversionMethod(parameterClass, true);
                    if (conversionMethod != null) {
                        String sourceMethod = helperBuilder.addSourceMethod(valueSource, true);
                        return CodeBlock.builder()
                            .add("$N($N($N, $S))", conversionMethod, sourceMethod, wrapper, parameterName)
                            .build()
                            .toString();
                    }
                }
            }
        }
        // Next, check if this is an object with a @From* annotation or if one of the type's
        // public members has a @From* annotation decorating it.

        // Lastly, assume the type should be converted using JSON.
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
        } else if (isType(parameterType, FileUpload.class)) {
            return CodeBlock.of(wrapper + ".getRequest().getFile($S)", getParameterName()).toString();
        } else {
            return null;
        }
    }

    public boolean isWsBinderNeeded(Class<?> wrapperType) {
        TypeMirror parameterType = parameter.asType();
        String parameter = getNonBinderWsParameter("context", wrapperType, "wrapper", parameterType);
        return StringUtils.isBlank(parameter);
    }

    public String generateWsParameter(String context, Class<?> wrapperType, String wrapper, HelperMethodBuilder helperBuilder) {
        TypeMirror parameterType = parameter.asType();
        String nonBinderParameter = getNonBinderWsParameter(context, wrapperType, wrapper, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            return nonBinderParameter;
        }
        String parameterName = getParameterName();
        WsValueSource valueSource = getWsValueSource(parameter);
        for (Class<?> parameterClass : HelperMethodBuilder.CONVERSION_HELPER_LOOKUP.keySet()) {
            if (isType(parameterType, parameterClass)) {
                String conversionMethod = helperBuilder.addConversionMethod(parameterClass, false);
                if (conversionMethod != null) {
                    String sourceMethod = helperBuilder.addSourceMethod(valueSource, wrapperType, false);
                    return CodeBlock.builder()
                        .add("$N($N($N, $S))", conversionMethod, sourceMethod, wrapper, parameterName)
                        .build()
                        .toString();
                }
            } else {
                Class<?> arrayClass = getArrayClass(parameterClass);
                if (isType(parameterType, arrayClass)) {
                    // NOTE: We pass the component type to the helper builder, not the array type.
                    String conversionMethod = helperBuilder.addConversionMethod(parameterClass, true);
                    if (conversionMethod != null) {
                        String sourceMethod = helperBuilder.addSourceMethod(valueSource, wrapperType, true);
                        return CodeBlock.builder()
                            .add("$N($N($N, $S))", conversionMethod, sourceMethod, wrapper, parameterName)
                            .build()
                            .toString();
                    }
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
        } else if (type.isPrimitive()) {
            Class<?> primitiveType = getPrimitiveType(parameterType);
            return primitiveType == type;
        } else if (!type.isArray()) {
            TypeElement typeElement = elementUtils.getTypeElement(type.getCanonicalName());
            if (typeElement == null) {
                return false;
            }
            TypeMirror checkType = typeElement.asType();
            return typeUtils.isSameType(parameterType, checkType);
        } else {
            return false;
        }
    }

    private static Class<?> getPrimitiveType(TypeMirror parameterType) {
        switch (parameterType.getKind()) {
            case BOOLEAN:
                return boolean.class;
            case BYTE:
                return byte.class;
            case CHAR:
                return char.class;
            case DOUBLE:
                return double.class;
            case FLOAT:
                return float.class;
            case INT:
                return int.class;
            case LONG:
                return long.class;
            case SHORT:
                return short.class;
            default:
                return void.class;
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
}
