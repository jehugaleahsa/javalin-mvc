package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.*;
import com.truncon.javalin.mvc.api.ws.*;
import io.javalin.http.Context;
import org.apache.commons.lang3.StringUtils;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.Map;

final class ParameterGenerator {
    private final VariableElement parameter;

    private ParameterGenerator(VariableElement parameter) {
        this.parameter = parameter;
    }

    public static String bindParameters(
            ExecutableElement method,
            String context,
            String wrapper,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        String[] arguments = method.getParameters().stream()
            .map(ParameterGenerator::new)
            .map(g -> g.generateParameter(context, wrapper, helperBuilder, converterLookup))
            .toArray(String[]::new);
        return String.join(", ", arguments);
    }

    public static String bindWsParameters(
            ExecutableElement method,
            String context,
            Class<? extends WsContext> wrapperType,
            String wrapper,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        String[] arguments = method.getParameters().stream()
                .map(ParameterGenerator::new)
                .map(g -> g.generateWsParameter(context, wrapperType, wrapper, helperBuilder, converterLookup))
                .toArray(String[]::new);
        return String.join(", ", arguments);
    }

    public String generateParameter(
            String context,
            String wrapper,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        TypeMirror parameterType = parameter.asType();
        TypeUtils typeUtils = helperBuilder.getContainer().getTypeUtils();
        String nonBinderParameter = getNonBinderParameter(context, wrapper, typeUtils, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            return nonBinderParameter;
        }
        String parameterName = getParameterName();
        ValueSource valueSource = getValueSource(parameter);

        String converterName = getConverterName();
        ConverterBuilder converter = converterLookup.get(converterName);
        if (converter == null) {
            if (converterName != null) {
                String message = "No converter named '"
                    + converterName
                    + "' exists.";
                throw new ProcessingException(message, parameter);
            }
        } else if (!converter.isExpectedType(parameterType)) {
            String message = "The conversion method '"
                + converterName
                + "' does not return a type compatible with '"
                + parameterType.toString()
                + "'.";
            throw new ProcessingException(message, parameter);
        } else {
            ContainerSource container = helperBuilder.getContainer();
            if (converter.hasContextOrRequestType(HttpContext.class)) {
                return converter.getConverterCall(container, wrapper, parameterName, valueSource).toString();
            } else if (converter.hasContextOrRequestType(HttpRequest.class)) {
                String requestName = wrapper + ".getRequest()";
                return converter.getConverterCall(container, requestName, parameterName, valueSource).toString();
            } else {
                String message = "The conversion method '"
                    + converterName
                    + "' cannot be used with HTTP action methods.";
                throw new ProcessingException(message, parameter);
            }
        }

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

        ValueSource defaultBinding = HelperMethodBuilder.getDefaultFromBinding(parameter);
        if (defaultBinding != ValueSource.Any || helperBuilder.hasMemberBinding(parameter)) {
            TypeElement element = typeUtils.getTypeElement(parameterType);
            String conversionMethod = helperBuilder.addConversionMethod(element, defaultBinding);
            return CodeBlock.builder()
                .add("$N($N)", conversionMethod, wrapper)
                .build()
                .toString();
        }

        String jsonMethod = helperBuilder.addJsonMethod();
        return CodeBlock.of("$N($N, $T.class)", jsonMethod, wrapper, parameterType).toString();
    }

    private String getConverterName() {
        // Always prefer a action method parameter annotation over a type annotation.
        UseConverter parameterUsage = parameter.getAnnotation(UseConverter.class);
        if (parameterUsage != null) {
            return parameterUsage.value();
        }
        // Look to see if the type itself has a preferred conversion method.
        TypeMirror parameterType = parameter.asType();
        if (parameterType.getKind() == TypeKind.DECLARED) {
            DeclaredType declaredType = (DeclaredType) parameterType;
            Element element = declaredType.asElement();
            UseConverter declaredConverter = element.getAnnotation(UseConverter.class);
            if (declaredConverter != null) {
                return declaredConverter.value();
            }
        }
        UseConverter typeUsage = parameterType.getAnnotation(UseConverter.class);
        return typeUsage == null ? null : typeUsage.value();
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

    public String generateWsParameter(
            String context,
            Class<? extends WsContext> wrapperType,
            String wrapper,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        TypeMirror parameterType = parameter.asType();
        TypeUtils typeUtils = helperBuilder.getContainer().getTypeUtils();
        String nonBinderParameter = getNonBinderWsParameter(typeUtils, context, wrapperType, wrapper, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            return nonBinderParameter;
        }

        String parameterName = getParameterName();
        WsValueSource valueSource = getWsValueSource(parameter);

        String converterName = getConverterName();
        ConverterBuilder converter = converterLookup.get(converterName);
        if (converter == null) {
            if (converterName != null) {
                String message = "No converter named '"
                    + converterName
                    + "' exists.";
                throw new ProcessingException(message, parameter);
            }
        } else if (!converter.isExpectedType(parameterType)) {
            String message = "The conversion method '"
                + converterName
                + "' does not return a type compatible with '"
                + parameterType.toString()
                + "'.";
            throw new ProcessingException(message, parameter);
        } else {
            ContainerSource container = helperBuilder.getContainer();
            if (converter.hasContextOrRequestType(WsContext.class) || converter.hasContextOrRequestType(wrapperType)) {
                return converter.getConverterCall(container, wrapper, parameterName, valueSource).toString();
            } else if (converter.hasContextOrRequestType(WsRequest.class)) {
                String requestName = wrapper + ".getRequest()";
                return converter.getConverterCall(container, requestName, parameterName, valueSource).toString();
            } else {
                String message = "The conversion method '"
                    + converterName
                    + "' cannot be used with WebSocket action methods.";
                throw new ProcessingException(message, parameter);
            }
        }

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

        if (wrapperType != WsMessageContext.class) {
            // Ignore requests to bind unrecognized types to the other WebSocket methods.
            // Provide explicit cast to prevent overloads from generating ambiguity errors.
            return CodeBlock.of("($T) null", parameterType).toString();
        }

        // Lastly, assume the type should be converted using JSON.
        String jsonMethod = helperBuilder.addWsJsonMethod(wrapperType);
        return CodeBlock.of("$N($N, $T.class)", jsonMethod, wrapper, parameterType).toString();
    }

    private static String getNonBinderWsParameter(
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

    private static boolean isBuiltInWsContextType(TypeUtils typeUtils, Class<?> wrapperType, TypeMirror parameterType) {
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

    private static ValueSource getValueSource(VariableElement parameter) {
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

    private static WsValueSource getWsValueSource(VariableElement parameter) {
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
