package io.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import io.javalin.mvc.api.*;

import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

final class ParameterGenerator {
    private static final Class<?>[] SUPPORTED_TYPES = new Class[] {
            String.class, Integer.class, Boolean.class, Date.class, BigDecimal.class,
            Double.class, Long.class, BigInteger.class, Short.class, Byte.class, Float.class,
            Character.class, Instant.class, ZonedDateTime.class, LocalDateTime.class,
            OffsetDateTime.class, LocalDate.class
    };
    private static final Map<Class<?>, Class<?>> ARRAY_TYPE_LOOKUP = getArrayTypeLookup();

    private static Map<Class<?>, Class<?>> getArrayTypeLookup() {
        Map<Class<?>, Class<?>> lookup = new HashMap<>();
        lookup.put(String.class, String[].class);
        lookup.put(Integer.class, Integer[].class);
        lookup.put(Boolean.class, Boolean[].class);
        lookup.put(Date.class, Date[].class);
        lookup.put(BigDecimal.class, BigDecimal[].class);
        lookup.put(Double.class, Double[].class);
        lookup.put(Long.class, Long[].class);
        lookup.put(BigInteger.class, BigInteger[].class);
        lookup.put(Short.class, Short[].class);
        lookup.put(Byte.class, Byte[].class);
        lookup.put(Float.class, Float[].class);
        lookup.put(Character.class, Character[].class);
        lookup.put(Instant.class, Instant[].class);
        lookup.put(ZonedDateTime.class, ZonedDateTime[].class);
        lookup.put(LocalDateTime.class, LocalDateTime[].class);
        lookup.put(OffsetDateTime.class, OffsetDateTime[].class);
        lookup.put(LocalDate.class, LocalDate[].class);
        return lookup;
    }

    private final Types typeUtils;
    private final Elements elementUtils;
    private final VariableElement parameter;

    private ParameterGenerator(Types typeUtils, Elements elementUtils, VariableElement parameter) {
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.parameter = parameter;
    }

    public static ParameterGenerator getParameterGenerator(RouteGenerator route, VariableElement parameter) {
        return new ParameterGenerator(route.getTypeUtils(), route.getElementUtils(), parameter);
    }

    public String generateParameter(String contextName) {
        TypeMirror parameterType = parameter.asType();
        if (isType(parameterType, HttpContext.class)) {
            return contextName;
        }
        if (isType(parameterType, HttpRequest.class)) {
            return contextName + ".getRequest()";
        }
        if (isType(parameterType, HttpResponse.class)) {
            return contextName + ".getResponse()";
        }
        String parameterName = getParameterName(parameter);
        if (isType(parameterType, FileUpload.class)) {
            return CodeBlock.of(contextName + ".getRequest().getFile($S)", parameterName).toString();
        }
        ValueSource valueSource = getValueSource(parameter);
        for (Class<?> parameterClass : SUPPORTED_TYPES) {
            if (isType(parameterType, parameterClass)) {
                return bindParameter(parameterName, parameterClass, valueSource);
            } else {
                Class<?> arrayClass = ARRAY_TYPE_LOOKUP.get(parameterClass);
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

    private boolean isType(TypeMirror parameterType, Class<?> type) {
        if (parameterType.getKind() == TypeKind.ARRAY) {
            return type.isArray() && isType(((ArrayType)parameterType).getComponentType(), type.getComponentType());
        } else {
            return !type.isArray() && typeUtils.isSameType(parameterType, elementUtils.getTypeElement(type.getCanonicalName()).asType());
        }
    }

    private String getParameterName(VariableElement parameter) {
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

    private static String bindParameter(String parameterName, Class<?> parameterClass, ValueSource valueSource) {
        return CodeBlock.of(
                "($T)binder.getValue($S, $T.class, $T.$L)",
                parameterClass,
                parameterName,
                parameterClass,
                ValueSource.class,
                valueSource).toString();
    }
}
