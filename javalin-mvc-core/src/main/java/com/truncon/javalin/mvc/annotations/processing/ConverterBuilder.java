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
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static List<ConverterBuilder> getConverterBuilders(
            TypeUtils typeUtils,
            RoundEnvironment environment,
            Collection<TypeElement> alternativeTypes) throws ProcessingException {
        List<ExecutableElement> elements = environment.getElementsAnnotatedWith(Converter.class).stream()
            .map(ExecutableElement.class::cast)
            .collect(Collectors.toList());
        checkConverterElements(typeUtils, elements);
        Stream<ExecutableElement> alternateMethods = alternativeTypes.stream()
            .flatMap(t -> t.getEnclosedElements().stream())
            .filter(ExecutableElement.class::isInstance)
            .map(ExecutableElement.class::cast)
            .filter(e -> e.getAnnotation(Converter.class) != null);
        List<ConverterBuilder> converters = Stream.concat(elements.stream(), alternateMethods)
            .distinct()
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
        // At most there can be a context/request, String name, and a ValueSource.
        if (parameters.size() > 3) {
            return true;
        }
        // A context/request object is always required
        List<VariableElement> contexts = parameters.stream()
            .filter(p -> isContextOrRequestType(typeUtils, p))
            .collect(Collectors.toList());
        if (contexts.size() != 1) {
            return true;
        }
        List<VariableElement> remaining = new ArrayList<>(parameters);
        remaining.removeAll(contexts);
        // There should only be one name parameter, or none.
        List<VariableElement> names = remaining.stream()
            .filter(p -> typeUtils.isType(p.asType(), String.class))
            .collect(Collectors.toList());
        if (names.size() > 1) {
            return true;
        }
        remaining.removeAll(names);
        // At this point, the only remaining parameter should be a value source.
        // We might also not have any additional parameters. If we have more than
        // one remaining parameter, then there must not have been a name, so we have
        // an extra parameter we can't bind.
        if (remaining.isEmpty()) {
            return false;
        } else if (remaining.size() > 1) {
            return true;
        }
        TypeMirror contextType = contexts.get(0).asType();
        TypeMirror sourceType = remaining.get(0).asType();
        return !isSourceType(typeUtils, sourceType, contextType);
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
        if (converterType.getKind() == ElementKind.INTERFACE) {
            throw new ProcessingException("The converter '" + name + "' was defined within an interface, which is invalid.");
        }
        if (converterType.getModifiers().contains(Modifier.ABSTRACT)) {
            throw new ProcessingException("The converter '" + name + "' was defined within an abstract class, which is invalid.");
        }
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

    public TypeElement getConversionClass() {
        return conversionClass;
    }

    public String getName() {
        return name;
    }

    public boolean hasContextOrRequestType(Class<?> contextType) {
        return typeUtils.isType(conversionMethod.getParameters().get(0).asType(), contextType);
    }

    public ConvertCallResult getConverterCall(
            ContainerSource container,
            String contextOrRequestName,
            String parameterName,
            String injectorName,
            ValueSource valueSource) {
        return getConverterCallBody(
            container, contextOrRequestName, parameterName, injectorName, ValueSource.class, valueSource);
    }

    public ConvertCallResult getConverterCall(
            ContainerSource container,
            String contextOrRequestName,
            String parameterName,
            String injectorName,
            WsValueSource valueSource) {
        return getConverterCallBody(
            container, contextOrRequestName, parameterName, injectorName, WsValueSource.class, valueSource);
    }

    private <TValueSource extends Enum<TValueSource>> ConvertCallResult getConverterCallBody(
            ContainerSource container,
            String contextOrRequestName,
            String parameterName,
            String injectorName,
            Class<? extends TValueSource> sourceClass,
            TValueSource valueSource) {
        CodeBlock.Builder callBuilder = CodeBlock.builder();
        boolean injectorNeeded = false;
        if (conversionMethod.getModifiers().contains(Modifier.STATIC)) {
            callBuilder.add("$T", conversionClass.asType());
        } else {
            Name converterName = container.getDependencyName(conversionClass);
            if (container.getContainerType() == ContainerSource.Type.DAGGER && converterName != null) {
                callBuilder.add("$N.$L()", injectorName, converterName);
                injectorNeeded = true;
            } else if (container.getContainerType() == ContainerSource.Type.GUICE) {
                callBuilder.add("$N.getInstance($T.class)", injectorName, conversionClass.asType());
                injectorNeeded = true;
            } else {
                callBuilder.add("new $T()", conversionClass.asType());
            }
        }
        callBuilder.add(".$N(", conversionMethod.getSimpleName());

        // We don't force the order of the conversion method parameters so this code below
        // makes sure we call the conversion method in whatever order is necessary, skipping
        // over optional arguments that aren't needed.
        int contextPosition = getContextOrRequestPosition();
        int namePosition = getNamePosition();
        int sourcePosition = getSourcePosition(sourceClass);
        int size = 1 + (namePosition == -1 ? 0 : 1) + (sourcePosition == -1 ? 0 : 1);
        CodeBlock[] blocks = new CodeBlock[size];
        blocks[contextPosition] = CodeBlock.builder().add("$N", contextOrRequestName).build();
        if (namePosition != -1) {
            blocks[namePosition] = CodeBlock.builder().add("$S", parameterName).build();
        }
        if (sourcePosition != -1) {
            blocks[sourcePosition] = CodeBlock.builder().add("$T.$N", sourceClass, valueSource.name()).build();
        }
        callBuilder.add(CodeBlock.join(Arrays.asList(blocks), ", "));

        callBuilder.add(")");
        String call = callBuilder.build().toString();
        return new ConvertCallResult(call, injectorNeeded);
    }

    private int getContextOrRequestPosition() {
        for (int index = 0; index != conversionMethod.getParameters().size(); ++index) {
            VariableElement parameter = conversionMethod.getParameters().get(index);
            if (isContextOrRequestType(typeUtils, parameter)) {
                return index;
            }
        }
        return -1; // This should not be possible since we validated beforehand
    }

    private int getNamePosition() {
        for (int index = 0; index != conversionMethod.getParameters().size(); ++index) {
            VariableElement parameter = conversionMethod.getParameters().get(index);
            if (typeUtils.isType(parameter.asType(), String.class)) {
                return index;
            }
        }
        return -1; // This can happen if the name parameter is not provided.
    }

    private int getSourcePosition(Class<?> valueSourceType) {
        for (int index = 0; index != conversionMethod.getParameters().size(); ++index) {
            VariableElement parameter = conversionMethod.getParameters().get(index);
            if (typeUtils.isType(parameter.asType(), valueSourceType)) {
                return index;
            }
        }
        return -1; // This can happen if the name parameter is not provided.
    }

    public boolean isExpectedType(TypeMirror parameterType) {
        TypeMirror returnType = conversionMethod.getReturnType();
        return typeUtils.isSubtype(returnType, parameterType);
    }
}
