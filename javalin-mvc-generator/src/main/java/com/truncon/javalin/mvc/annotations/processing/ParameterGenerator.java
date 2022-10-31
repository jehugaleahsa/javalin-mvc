package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.FileUpload;
import com.truncon.javalin.mvc.api.FromBody;
import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromForm;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromJson;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.HttpResponse;
import com.truncon.javalin.mvc.api.NoBinding;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ValueSource;
import com.truncon.javalin.mvc.api.ws.FromBinary;
import com.truncon.javalin.mvc.api.ws.WsBinaryMessageContext;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsDisconnectContext;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import com.truncon.javalin.mvc.api.ws.WsResponse;
import com.truncon.javalin.mvc.api.ws.WsValueSource;
import io.javalin.http.Context;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.io.InputStream;
import java.util.Map;

final class ParameterGenerator {
    private final HelperMethodBuilder helperBuilder;
    private final VariableElement parameter;
    private String context = "context";
    private String wrapper = "wrapper";
    private String injector = "injector";

    private ParameterGenerator(HelperMethodBuilder helperBuilder, VariableElement parameter) {
        this.helperBuilder = helperBuilder;
        this.parameter = parameter;
    }

    public void setContextName(String name) {
        this.context = name;
    }

    public void setWrapperName(String name) {
        this.wrapper = name;
    }

    public void setInjectorName(String name) {
        this.injector = name;
    }

    public static ParameterResult bindParameters(
            ExecutableElement method,
            String context,
            String wrapper,
            String injector,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        ParameterResult result = new ParameterResult();
        for (VariableElement parameter : method.getParameters()) {
            ParameterGenerator generator = new ParameterGenerator(helperBuilder, parameter);
            generator.setContextName(context);
            generator.setWrapperName(wrapper);
            generator.setInjectorName(injector);
            generator.generateParameter(result, converterLookup);
        }
        return result;
    }

    public static ParameterResult bindWsParameters(
            ExecutableElement method,
            String context,
            Class<? extends WsContext> wrapperType,
            String wrapper,
            String injector,
            HelperMethodBuilder helperBuilder,
            Map<String, ConverterBuilder> converterLookup) {
        ParameterResult result = new ParameterResult();
        for (VariableElement parameter : method.getParameters()) {
            ParameterGenerator generator = new ParameterGenerator(helperBuilder, parameter);
            generator.setContextName(context);
            generator.setWrapperName(wrapper);
            generator.setInjectorName(injector);
            generator.generateWsParameter(result, wrapperType, converterLookup);
        }
        return result;
    }

    public void generateParameter(ParameterResult result, Map<String, ConverterBuilder> converterLookup) {
        TypeMirror parameterType = parameter.asType();
        if (isNotBound()) {
            String argument = CodeBlock.of("($T) null", parameterType).toString();
            result.addArgument(argument);
            return;
        }

        TypeUtils typeUtils = helperBuilder.getContainer().getTypeUtils();
        String nonBinderParameter = getNonBinderParameter(context, wrapper, typeUtils, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            result.addArgument(nonBinderParameter);
            return;
        }

        String parameterName = getParameterName();
        ValueSource valueSource = getValueSource(parameter);
        String defaultValue = HelperMethodBuilder.getDefaultValue(parameter);
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
                ConvertCallResult callResult = converter.getConverterCall(container, wrapper, parameterName, injector, valueSource);
                result.addArgument(callResult.getCall());
                result.markInjectorNeeded(callResult.isInjectorNeeded());
                return;
            } else if (converter.hasContextOrRequestType(HttpRequest.class)) {
                String requestName = wrapper + ".getRequest()";
                ConvertCallResult callResult = converter.getConverterCall(container, requestName, parameterName, injector, valueSource);
                result.addArgument(callResult.getCall());
                result.markInjectorNeeded(callResult.isInjectorNeeded());
                return;
            } else {
                String message = "The conversion method '"
                    + converterName
                    + "' cannot be used with HTTP action methods.";
                throw new ProcessingException(message, parameter);
            }
        }

        if (valueSource == ValueSource.Json) {
            String jsonMethod = helperBuilder.addJsonMethod();
            TypeMirror erased = typeUtils.erasure(parameterType);
            String argument = CodeBlock.of("$N($N, $T.class)", jsonMethod, wrapper, erased).toString();
            result.addArgument(argument);
            return;
        }

        HelperMethodBuilder.ConversionHelper conversionHelper = helperBuilder.getConversionHelper(parameterType);
        if (conversionHelper != null) {
            conversionHelper.addConversionMethod(helperBuilder);
            String sourceMethod = helperBuilder.addSourceMethod(valueSource, conversionHelper.isCollectionType(), defaultValue);
            String sourceCall = defaultValue == null
                ? CodeBlock.of("$N($N, $S)", sourceMethod, wrapper, parameterName).toString()
                : CodeBlock.of("$N($N, $S, $S)", sourceMethod, wrapper, parameterName, defaultValue).toString();
            String conversionCall = conversionHelper.getConversionCall(sourceCall);
            result.addArgument(conversionCall);
            return;
        }

        ValueSource defaultBinding = HelperMethodBuilder.getDefaultFromBinding(parameter);
        if (defaultBinding != ValueSource.Any || helperBuilder.hasMemberBinding(parameter)) {
            TypeElement element = typeUtils.getTypeElement(parameterType);
            if (element != null && typeUtils.getCollectionComponentType(parameterType) == null) {
                ConversionMethodResult methodResult = helperBuilder.addConversionMethod(element, defaultBinding);
                CodeBlock argument = methodResult.isInjectorNeeded()
                    ? CodeBlock.builder().add("$N($N, $N)", methodResult.getMethod(), wrapper, injector).build()
                    : CodeBlock.builder().add("$N($N)", methodResult.getMethod(), wrapper).build();
                result.addArgument(argument.toString());
                result.markInjectorNeeded(methodResult.isInjectorNeeded());
                return;
            }
        }

        String jsonMethod = helperBuilder.addJsonMethod();
        TypeMirror erased = typeUtils.erasure(parameterType);
        String argument = CodeBlock.of("$N($N, $T.class)", jsonMethod, wrapper, erased).toString();
        result.addArgument(argument);
    }

    private boolean isNotBound() {
        NoBinding noBinding = parameter.getAnnotation(NoBinding.class);
        return noBinding != null;
    }

    private String getConverterName() {
        // Always prefer an action method parameter annotation over a type annotation.
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
        if (typeUtils.isSameType(parameterType, Context.class)) {
            return context;
        } else if (typeUtils.isSameType(parameterType, HttpContext.class)) {
            return wrapper;
        } else if (typeUtils.isSameType(parameterType, HttpRequest.class)) {
            return wrapper + ".getRequest()";
        } else if (typeUtils.isSameType(parameterType, HttpResponse.class)) {
            return wrapper + ".getResponse()";
        } else if (typeUtils.isSameType(parameterType, FileUpload.class)) {
            return CodeBlock.of(wrapper + ".getRequest().getFile($S)", getParameterName()).toString();
        } else if (typeUtils.isSameType(parameterType, InputStream.class)) {
            return CodeBlock.of(wrapper + ".getRequest().getBodyAsInputStream()").toString();
        } else {
            return null;
        }
    }

    public void generateWsParameter(
            ParameterResult result,
            Class<? extends WsContext> wrapperType,
            Map<String, ConverterBuilder> converterLookup) {
        TypeMirror parameterType = parameter.asType();
        if (isNotBound()) {
            String argument = CodeBlock.of("($T) null", parameterType).toString();
            result.addArgument(argument);
            return;
        }
        TypeUtils typeUtils = helperBuilder.getContainer().getTypeUtils();
        String nonBinderParameter = getNonBinderWsParameter(typeUtils, context, wrapperType, wrapper, parameterType);
        if (!StringUtils.isBlank(nonBinderParameter)) {
            result.addArgument(nonBinderParameter);
            return;
        }

        String parameterName = getParameterName();
        WsValueSource valueSource = getWsValueSource(parameter);
        String defaultValue = HelperMethodBuilder.getDefaultValue(parameter);
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
                ConvertCallResult callResult = converter.getConverterCall(container, wrapper, parameterName, injector, valueSource);
                result.addArgument(callResult.getCall());
                result.markInjectorNeeded(callResult.isInjectorNeeded());
                return;
            } else if (converter.hasContextOrRequestType(WsRequest.class)) {
                String requestName = wrapper + ".getRequest()";
                ConvertCallResult callResult = converter.getConverterCall(container, requestName, parameterName, injector, valueSource);
                result.addArgument(callResult.getCall());
                result.markInjectorNeeded(callResult.isInjectorNeeded());
                return;
            } else {
                String message = "The conversion method '"
                    + converterName
                    + "' cannot be used with WebSocket action methods.";
                throw new ProcessingException(message, parameter);
            }
        }

        if (valueSource == WsValueSource.Message) {
            if (wrapperType == WsMessageContext.class) {
                // Lastly, assume the type should be converted using JSON.
                String jsonMethod = helperBuilder.addWsJsonMethod();
                TypeMirror erased = typeUtils.erasure(parameterType);
                String argument = CodeBlock.of("$N($N, $T.class)", jsonMethod, wrapper, erased).toString();
                result.addArgument(argument);
                return;
            } else if (wrapperType == WsBinaryMessageContext.class) {
                String binaryMethod = helperBuilder.addWsBinaryMethod(parameterType);
                if (binaryMethod != null) {
                    String argument = CodeBlock.of("$N($N)", binaryMethod, wrapper).toString();
                    result.addArgument(argument);
                    return;
                }
            } else {
                // Ignore requests to bind unrecognized types to the other WebSocket methods.
                // Provide explicit cast to prevent overloads from generating ambiguity errors.
                String argument = CodeBlock.of("($T) null", parameterType).toString();
                result.addArgument(argument);
                return;
            }
        }

        // For binary messages, if FromBinary isn't explicitly specified, byte[] would otherwise get picked up
        // as a supported built-in conversion type. We want to do an upfront check to see if we can
        // bind from the binary message first.
        if (wrapperType == WsBinaryMessageContext.class) {
            String binaryMethod = helperBuilder.addWsBinaryMethod(parameterType);
            if (binaryMethod != null) {
                String argument = CodeBlock.of("$N($N)", binaryMethod, wrapper).toString();
                result.addArgument(argument);
                return;
            }
        }

        // The getParameterClass method will only return a non-null value if the
        // parameter type is one of the types with built-in converters.
        HelperMethodBuilder.ConversionHelper conversionHelper = helperBuilder.getConversionHelper(parameterType);
        if (conversionHelper != null) {
            conversionHelper.addConversionMethod(helperBuilder);
            String sourceMethod = helperBuilder.addSourceMethod(
                valueSource, wrapperType, conversionHelper.isCollectionType(), defaultValue);
            String sourceCall = defaultValue == null
                ? CodeBlock.of("$N($N, $S)", sourceMethod, wrapper, parameterName).toString()
                : CodeBlock.of("$N($N, $S, $S)", sourceMethod, wrapper, parameterName, defaultValue).toString();
            String conversionCall = conversionHelper.getConversionCall(sourceCall);
            result.addArgument(conversionCall);
            return;
        }

        // Next, check if this is an object with a @From* annotation or if one of the type's
        // public members has a @From* annotation decorating it.
        WsValueSource defaultBinding = HelperMethodBuilder.getDefaultWsFromBinding(parameter);
        if (defaultBinding != WsValueSource.Any || helperBuilder.hasWsMemberBinding(parameter)) {
            TypeElement element = typeUtils.getTypeElement(parameterType);
            if (element != null && typeUtils.getCollectionComponentType(parameterType) == null) {
                ConversionMethodResult methodResult = helperBuilder.addConversionMethod(element, defaultBinding, wrapperType);
                String argument = CodeBlock.builder()
                    .add("$N($N)", methodResult.getMethod(), wrapper)
                    .build()
                    .toString();
                result.addArgument(argument);
                result.markInjectorNeeded(methodResult.isInjectorNeeded());
                return;
            }
        }

        // If we get to this point, we know there's no converter, @From annotation or primitive mapping.
        // If it is an object, it did not have any @From annotations on any members either. We assume,
        // then, that it is probably meant to be bound from JSON.
        if (wrapperType == WsMessageContext.class) {
            String jsonMethod = helperBuilder.addWsJsonMethod();
            TypeMirror erased = typeUtils.erasure(parameterType);
            String argument = CodeBlock.of("$N($N, $T.class)", jsonMethod, wrapper, erased).toString();
            result.addArgument(argument);
            return;
        }
        // Ignore requests to bind unrecognized types to the other WebSocket methods.
        // Provide explicit cast to prevent overloads from generating ambiguity errors.
        String argument = CodeBlock.of("($T) null", parameterType).toString();
        result.addArgument(argument);
    }

    private static String getNonBinderWsParameter(
            TypeUtils typeUtils,
            String context,
            Class<?> wrapperType,
            String wrapper,
            TypeMirror parameterType) {
        if (isBuiltInWsContextType(typeUtils, wrapperType, parameterType)) {
            return context;
        } else if (typeUtils.isSameType(parameterType, wrapperType)) {
            return wrapper;
        } else if (typeUtils.isSameType(parameterType, WsContext.class)) {
            return wrapper + ".getContext()";
        } else if (typeUtils.isSameType(parameterType, WsRequest.class)) {
            return wrapper + ".getRequest()";
        } else if (typeUtils.isSameType(parameterType, WsResponse.class)) {
            return wrapper + ".getResponse()";
        } else {
            return null;
        }
    }

    private static boolean isBuiltInWsContextType(TypeUtils typeUtils, Class<?> wrapperType, TypeMirror parameterType) {
        if (typeUtils.isSameType(parameterType, io.javalin.websocket.WsContext.class)) {
            return true;
        } else if (wrapperType.equals(WsConnectContext.class)
            && typeUtils.isSameType(parameterType, io.javalin.websocket.WsConnectContext.class)) {
            return true;
        } else if (wrapperType.equals(WsDisconnectContext.class)
            && typeUtils.isSameType(parameterType, io.javalin.websocket.WsCloseContext.class)) {
            return true;
        } else if (wrapperType.equals(WsErrorContext.class)
            && typeUtils.isSameType(parameterType, io.javalin.websocket.WsErrorContext.class)) {
            return true;
        } else if (wrapperType.equals(WsMessageContext.class)
            && typeUtils.isSameType(parameterType, io.javalin.websocket.WsMessageContext.class)) {
            return true;
        } else if (wrapperType.equals(WsBinaryMessageContext.class)
            && typeUtils.isSameType(parameterType, io.javalin.websocket.WsBinaryMessageContext.class)) {
            return true;
        } else {
            return false;
        }
    }

    private String getParameterName() {
        String specifiedName = HelperMethodBuilder.getSpecifiedName(parameter);
        if (specifiedName != null) {
            return specifiedName;
        }
        return parameter.getSimpleName().toString();
    }

    private static ValueSource getValueSource(Element parameter) {
        if (parameter.getAnnotation(FromHeader.class) != null) {
            return ValueSource.Header;
        }
        if (parameter.getAnnotation(jakarta.ws.rs.HeaderParam.class) != null) {
            return ValueSource.Header;
        }
        if (parameter.getAnnotation(FromCookie.class) != null) {
            return ValueSource.Cookie;
        }
        if (parameter.getAnnotation(jakarta.ws.rs.CookieParam.class) != null) {
            return ValueSource.Cookie;
        }
        if (parameter.getAnnotation(FromPath.class) != null) {
            return ValueSource.Path;
        }
        if (parameter.getAnnotation(jakarta.ws.rs.PathParam.class) != null) {
            return ValueSource.Path;
        }
        if (parameter.getAnnotation(FromQuery.class) != null) {
            return ValueSource.QueryString;
        }
        if (parameter.getAnnotation(jakarta.ws.rs.QueryParam.class) != null) {
            return ValueSource.QueryString;
        }
        if (parameter.getAnnotation(FromForm.class) != null) {
            return ValueSource.FormData;
        }
        if (parameter.getAnnotation(jakarta.ws.rs.FormParam.class) != null) {
            return ValueSource.FormData;
        }
        if (parameter.getAnnotation(FromBody.class) != null) {
            return ValueSource.Json;
        }
        //noinspection deprecation
        if (parameter.getAnnotation(FromJson.class) != null) {
            return ValueSource.Json;
        }
        return ValueSource.Any;
    }

    private static WsValueSource getWsValueSource(Element parameter) {
        if (parameter.getAnnotation(FromBody.class) != null) {
            return WsValueSource.Message;
        }
        //noinspection deprecation
        if (parameter.getAnnotation(FromJson.class) != null) {
            return WsValueSource.Message;
        }
        //noinspection deprecation
        if (parameter.getAnnotation(FromBinary.class) != null) {
            return WsValueSource.Message;
        }
        if (parameter.getAnnotation(FromHeader.class) != null) {
            return WsValueSource.Header;
        }
        if (parameter.getAnnotation(jakarta.ws.rs.HeaderParam.class) != null) {
            return WsValueSource.Header;
        }
        if (parameter.getAnnotation(FromCookie.class) != null) {
            return WsValueSource.Cookie;
        }
        if (parameter.getAnnotation(jakarta.ws.rs.CookieParam.class) != null) {
            return WsValueSource.Cookie;
        }
        if (parameter.getAnnotation(FromPath.class) != null) {
            return WsValueSource.Path;
        }
        if (parameter.getAnnotation(jakarta.ws.rs.PathParam.class) != null) {
            return WsValueSource.Path;
        }
        if (parameter.getAnnotation(FromQuery.class) != null) {
            return WsValueSource.QueryString;
        }
        if (parameter.getAnnotation(jakarta.ws.rs.QueryParam.class) != null) {
            return WsValueSource.QueryString;
        }
        return WsValueSource.Any;
    }
}
