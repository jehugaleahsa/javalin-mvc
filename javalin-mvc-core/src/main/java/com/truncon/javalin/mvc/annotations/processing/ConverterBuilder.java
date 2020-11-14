package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.Converter;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.ValueSource;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsDisconnectContext;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import com.truncon.javalin.mvc.api.ws.WsValueSource;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.stream.Collectors;

public final class ConverterBuilder {
    private final TypeUtils typeUtils;
    private final String name;
    private final TypeElement conversionClass;
    private final ExecutableElement conversionMethod;

    private ConverterBuilder(
            TypeUtils typeUtils,
            String name,
            TypeElement conversionClass,
            ExecutableElement conversionMethod) {
        this.typeUtils = typeUtils;
        this.name = name;
        this.conversionClass = conversionClass;
        this.conversionMethod = conversionMethod;
    }

    public static List<ConverterBuilder> getConverterBuilders(TypeUtils typeUtils, RoundEnvironment environment) throws ProcessingException {
        List<ExecutableElement> elements = environment.getElementsAnnotatedWith(Converter.class).stream()
            .map(ExecutableElement.class::cast)
            .collect(Collectors.toList());
        checkConverterElements(typeUtils, elements);
        List<ConverterBuilder> converters = elements.stream()
            .map(e -> create(typeUtils, e))
            .collect(Collectors.toList());
        checkConverters(converters);
        return converters;
    }

    private static void checkConverterElements(TypeUtils typeUtils, List<ExecutableElement> elements) throws ProcessingException {
        List<ExecutableElement> methodElements = elements.stream()
            .map(ExecutableElement.class::cast)
            .collect(Collectors.toList());

        // Check for invalid arguments getting passed to converters.
        List<ExecutableElement> badArgumentMethods = methodElements.stream()
            .filter(e -> hasBadArguments(typeUtils, e))
            .collect(Collectors.toList());
        if (!badArgumentMethods.isEmpty()) {
            String message = "Methods marked with the @Converter annotation must accept a context or request object, "
                + "an optional binding name, and an optional value source. For HTTP, an HttpContext or HttpRequest "
                + "object is expected. For WebSockets, a WsContext (or a sub-interface) or WsRequest is expected. The "
                + "binding name will be a string matching the action method parameter name or @Named annotation value. "
                + "The third parameter should be a ValueSource for HTTP or WsValueSource for WebSockets.";
            throw new ProcessingException(message, badArgumentMethods.toArray(new Element[0]));
        }
    }

    private static boolean hasBadArguments(TypeUtils typeUtils, ExecutableElement method) {
        List<? extends VariableElement> parameters = method.getParameters();
        // A context object is always required
        if (parameters.isEmpty() || !isContextOrRequestType(typeUtils, parameters.get(0))) {
            return true;
        }
        if (parameters.size() > 1 && !typeUtils.isType(parameters.get(1).asType(), String.class)) {
            return true;
        }
        if (parameters.size() <= 2) {
            return false;
        }
        TypeMirror sourceType = parameters.get(2).asType();
        TypeMirror contextType = parameters.get(0).asType();
        return parameters.size() > 2 && !isSourceType(typeUtils, sourceType, contextType);
    }

    private static boolean isContextOrRequestType(TypeUtils typeUtils, VariableElement variableElement) {
        return typeUtils.isType(variableElement.asType(), HttpContext.class)
            || typeUtils.isType(variableElement.asType(), HttpRequest.class)
            || typeUtils.isType(variableElement.asType(), WsContext.class)
            || typeUtils.isType(variableElement.asType(), WsConnectContext.class)
            || typeUtils.isType(variableElement.asType(), WsDisconnectContext.class)
            || typeUtils.isType(variableElement.asType(), WsErrorContext.class)
            || typeUtils.isType(variableElement.asType(), WsMessageContext.class)
            || typeUtils.isType(variableElement.asType(), WsRequest.class);
    }

    private static boolean isSourceType(TypeUtils typeUtils, TypeMirror sourceType, TypeMirror contextType) {
        boolean isHttp = typeUtils.isType(contextType, HttpContext.class)
            || typeUtils.isType(contextType, HttpRequest.class);
        return (isHttp && typeUtils.isType(sourceType, ValueSource.class))
            || (!isHttp && typeUtils.isType(sourceType, WsValueSource.class));
    }

    private static ConverterBuilder create(TypeUtils typeUtils, ExecutableElement method) {
        Converter converter = method.getAnnotation(Converter.class);
        String name = converter.value();
        TypeElement converterType = (TypeElement) method.getEnclosingElement();
        return new ConverterBuilder(typeUtils, name, converterType, method);
    }

    private static void checkConverters(List<ConverterBuilder> converters) {
        ProcessingException[] exceptions = converters.stream()
            .collect(Collectors.groupingBy(ConverterBuilder::getName))
            .entrySet().stream()
            .filter(e -> e.getValue().size() > 1)
            .map(e -> new ProcessingException("Multiple converters have the same name: " + e.getKey() + ".",
                e.getValue().stream()
                    .map(c -> c.conversionMethod)
                    .toArray(ExecutableElement[]::new)))
            .toArray(ProcessingException[]::new);
        if (exceptions.length > 0) {
            throw new ProcessingMultiException(exceptions);
        }
    }

    public String getName() {
        return name;
    }

    public boolean hasContextOrRequestType(Class<?> contextType) {
        return typeUtils.isType(conversionMethod.getParameters().get(0).asType(), contextType);
    }

    public CodeBlock getConverterCall(
            ContainerSource container,
            String contextOrRequestName,
            String parameterName,
            ValueSource valueSource) {
        return getConverterCallBody(container, contextOrRequestName, parameterName, ValueSource.class, valueSource);
    }

    public CodeBlock getConverterCall(
            ContainerSource container,
            String contextOrRequestName,
            String parameterName,
            WsValueSource valueSource) {
        return getConverterCallBody(container, contextOrRequestName, parameterName, WsValueSource.class, valueSource);
    }

    private <TValueSource extends Enum<TValueSource>> CodeBlock getConverterCallBody(
            ContainerSource container,
            String contextOrRequestName,
            String parameterName,
            Class<? extends TValueSource> sourceClass,
            TValueSource valueSource) {
        CodeBlock.Builder callBuilder = CodeBlock.builder();
        if (conversionMethod.getModifiers().contains(Modifier.STATIC)) {
            callBuilder.add("$T", conversionClass.asType());
        } else {
            Name converterName = container.getDependencyName(conversionClass);
            if (converterName == null) {
                callBuilder.add("new $T()", conversionClass.asType());
            } else {
                callBuilder.add("injector.$L()", converterName);
            }
        }
        callBuilder.add(".$N($N", conversionMethod.getSimpleName(), contextOrRequestName);
        if (hasNameParameter()) {
            callBuilder.add(", $S", parameterName);
        }
        if (hasSourceParameter(sourceClass)) {
            callBuilder.add(", $T.$N", sourceClass, valueSource.name());
        }
        callBuilder.add(")");
        return callBuilder.build();
    }

    private boolean hasNameParameter() {
        if (conversionMethod.getParameters().size() < 2) {
            return false;
        }
        VariableElement parameter = conversionMethod.getParameters().get(1);
        return typeUtils.isType(parameter.asType(), String.class);
    }

    private boolean hasSourceParameter(Class<?> valueSourceType) {
        if (conversionMethod.getParameters().size() < 3) {
            return false;
        }
        VariableElement parameter = conversionMethod.getParameters().get(2);
        return typeUtils.isType(parameter.asType(), valueSourceType);
    }
}
