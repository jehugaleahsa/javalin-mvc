package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ws.*;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Performs model binding from header, URL path parameter, query string, form fields and the request body.
 */
public class DefaultWsModelBinder implements WsModelBinder {
    private static final Map<Class<?>, Function<String, Object>> primitiveConverters = getPrimitiveConverters();
    public static final Class<?>[] SUPPORTED_TYPES = primitiveConverters.keySet().toArray(new Class<?>[0]);
    private static final Map<Class<?>, Class<?>> primitiveToBoxed = getPrimitiveToBoxed();

    private static Map<Class<?>, Class<?>> getPrimitiveToBoxed() {
        Map<Class<?>, Class<?>> primitiveWrapperMap = new HashMap<>();
        primitiveWrapperMap.put(boolean.class, Boolean.class);
        primitiveWrapperMap.put(byte.class, Byte.class);
        primitiveWrapperMap.put(char.class, Character.class);
        primitiveWrapperMap.put(short.class, Short.class);
        primitiveWrapperMap.put(int.class, Integer.class);
        primitiveWrapperMap.put(long.class, Long.class);
        primitiveWrapperMap.put(double.class, Double.class);
        primitiveWrapperMap.put(float.class, Float.class);
        return primitiveWrapperMap;
    }

    private final WsContext context;
    private final ParameterCache headers;
    private final ParameterCache cookies;
    private final ParameterCache pathParameters;
    private final ParameterCache queryStrings;

    private static Map<Class<?>, Function<String, Object>> getPrimitiveConverters() {
        // We treat anything that can be parsed as a single value as "primitive".
        // Everything else we treat as an object that can be parsed from JSON.
        Map<Class<?>, Function<String, Object>> converters = new HashMap<>();
        converters.put(String.class, s -> s);
        converters.put(Integer.class, Integer::parseInt);
        converters.put(Boolean.class, Boolean::parseBoolean);
        converters.put(Date.class, DefaultWsModelBinder::toDate);
        converters.put(Instant.class, DefaultWsModelBinder::toInstant);
        converters.put(ZonedDateTime.class, ZonedDateTime::parse);
        converters.put(OffsetDateTime.class, OffsetDateTime::parse);
        converters.put(LocalDateTime.class, LocalDateTime::parse);
        converters.put(LocalDate.class, LocalDate::parse);
        converters.put(Double.class, Double::parseDouble);
        converters.put(Long.class, Long::parseLong);
        converters.put(Short.class, Short::parseShort);
        converters.put(Float.class, Float::parseFloat);
        converters.put(Character.class, s -> s.length() == 1 ? s.charAt(0) : null);
        converters.put(BigInteger.class, BigInteger::new);
        converters.put(BigDecimal.class, BigDecimal::new);
        converters.put(Byte.class, Byte::parseByte);
        converters.put(UUID.class, UUID::fromString);
        return converters;
    }

    private static Date toDate(String value) {
        return Date.from(toInstant(value));
    }

    private static Instant toInstant(String value) {
        try {
            return Instant.parse(value);
        } catch (DateTimeParseException exception) {
            // Ignore and try again
        }
        try {
            OffsetDateTime dateTime = OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return dateTime.toInstant();
        } catch (DateTimeParseException exception) {
            // Ignore and try again
        }
        try {
            LocalDateTime dateTime = LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            return dateTime.toInstant(ZonedDateTime.now().getOffset());
        } catch (DateTimeParseException exception) {
            // Ignore and try again
        }
        LocalDate date = LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE);
        return date.atStartOfDay().toInstant(ZonedDateTime.now().getOffset());
    }

    /**
     * Instantiates a new instance of a DefaultModelBinder.
     * @param context the current context object.
     */
    public DefaultWsModelBinder(WsContext context) {
        this.context = context;
        WsRequest request = context.getRequest();
        this.headers = new ParameterCache(request::getHeaderLookup);
        this.cookies = new ParameterCache(request::getCookieLookup);
        this.pathParameters = new ParameterCache(request::getPathLookup);
        this.queryStrings = new ParameterCache(request::getQueryLookup);
    }

    /**
     * Gets a value from the request for a parameter with the given name and type.
     * If the value is coming from a header, URL path parameter, query string or form field,
     * the name of the parameter will be used to lookup the value; this is a case-insensitive
     * lookup that removes any leading or trailing whitespace. Normally,
     * the name of the parameter is whatever the parameter name in the action method is called; however,
     * this name can be overridden using the {@link Named} annotation. The conversion type
     * is also determined by looking at the action method parameter type. If the type is not a supported
     * primitive type, the value will be parsed as a JSON document and the name is ignored.
     * @param name The name of the parameter to search for, if applicable.
     * @param parameterClass The type to convert the parameter to.
     * @param valueSource Specifies where the value must be sourced.
     * @return the converted value or null if no value corresponds to the name or the parsing fails.
     */
    @Override
    public Object getValue(String name, Class<?> parameterClass, WsValueSource valueSource) {
        // First see if the value is supposed to come from the message.
        if (valueSource == WsValueSource.Message) {
            return getDeserializedBody(parameterClass);
        }
        // Next, we see if the parameter is associated with a specific source.
        ParameterCache cache = getParameterCache(valueSource);
        if (cache == null) {
            // Either the source is not specified explicitly or the value should come from the body.
            cache = getParameterCache(name);
            if (cache == null) {
                // There was not a specific source, so we fallback on JSON deserialization.
                return getDeserializedBody(parameterClass);
            } else {
                return toParameterValue(parameterClass, cache.getValues(name)).orElse(null);
            }
        }
        // A specific source was determined. It is either an array, a "primitive",
        // or we are being asked to bind values inside of an object.
        if (!cache.hasValue(name)) {
            // We are being asked to pull from a specific source.
            // However, that source does not contain a parameter with that name or its value is nonsense.
            // We are probably being asked to bind values inside of an object.
            return bindValues(cache, parameterClass);
        }
        // A value with that name exists in the specific source, so try to bind it directly.
        // We can bind the value if it is an array or a "primitive".
        // Otherwise, try to bind the value inside of an object.
        return toParameterValue(parameterClass, cache.getValues(name)).orElse(bindValues(cache, parameterClass));
    }

    private Object getDeserializedBody(Class<?> paramType) {
        try {
            if (context instanceof WsMessageContext) {
                WsMessageContext messageContext = (WsMessageContext)context;
                if (paramType.equals(String.class)) {
                   return messageContext.getMessage();
                } else {
                    return messageContext.getMessage(paramType);
                }
            } else if (context instanceof WsBinaryMessageContext) {
                WsBinaryMessageContext binaryContext = (WsBinaryMessageContext) context;
                if (isByteArray(paramType)) {
                    return getByteArray(binaryContext);
                } else if (paramType.equals(ByteBuffer.class)) {
                    return getByteBuffer(binaryContext);
                }
            }
            return null;
        } catch (Exception exception) {
            // swallow any deserialization errors
            return null;
        }
    }

    private byte[] getByteArray(WsBinaryMessageContext binaryContext) {
        byte[] data = binaryContext.getData();
        if (binaryContext.getOffset() == null) {
            return data;
        }
        int length = binaryContext.getLength() == null
            ? data.length - binaryContext.getOffset()
            : binaryContext.getLength();
        byte[] result = new byte[length];
        System.arraycopy(data, binaryContext.getOffset(), result, 0, length);
        return result;
    }

    private ByteBuffer getByteBuffer(WsBinaryMessageContext binaryContext) {
        byte[] data = binaryContext.getData();
        if (binaryContext.getOffset() == null) {
            return ByteBuffer.wrap(data);
        }
        int length = binaryContext.getLength() == null
            ? data.length - binaryContext.getOffset()
            : binaryContext.getLength();
        return ByteBuffer.wrap(data, binaryContext.getOffset(), length);
    }

    private static boolean isByteArray(Class<?> paramType) {
        if (!paramType.isArray()) {
            return false;
        }
        Class<?> elementType = paramType.getComponentType();
        return elementType.equals(byte.class) || elementType.equals(Byte.class);
    }

    private ParameterCache getParameterCache(WsValueSource source) {
        switch (source) {
            case Path:
                return pathParameters;
            case Header:
                return headers;
            case Cookie:
                return cookies;
            case QueryString:
                return queryStrings;
            default:
                return null;
        }
    }

    private ParameterCache getParameterCache(String name) {
        if (pathParameters.hasValue(name)) {
            return pathParameters;
        } else if (headers.hasValue(name)) {
            return headers;
        } else if (cookies.hasValue(name)) {
            return cookies;
        } else if (queryStrings.hasValue(name)) {
            return queryStrings;
        } else {
            return null;
        }
    }

    private Object bindValues(ParameterCache cache, Class<?> type) {
        try {
            Object instance = type.getConstructor().newInstance();
            for (String key : cache.getKeys()) {
                bindValue(cache, type, instance, key);
            }
            return instance;
        } catch (Exception exception) {
            // swallow any deserialization errors
            return null;
        }
    }

    private void bindValue(ParameterCache cache, Class<?> type, Object instance, String key) {
        try {
            Method method = getMethodForKey(type, key);
            if (method != null) {
                method.setAccessible(true);
                List<String> rawValues = cache.getValues(key);
                Class<?> parameterType = method.getParameters()[0].getType();
                Optional<Object> value = toParameterValue(parameterType, rawValues);
                if (value.isPresent()) {
                    method.invoke(instance, value.get());
                    return;
                }
            }
            Field field = getFieldForKey(type, key);
            if (field != null) {
                field.setAccessible(true);
                List<String> rawValues = cache.getValues(key);
                Optional<Object> value = toParameterValue(field.getType(), rawValues);
                if (value.isPresent()) {
                    field.set(instance, value.get());
                }
            }
        } catch (Exception exception) {
            // Swallow any binding errors
        }
    }

    private Method getMethodForKey(Class<?> type, String key) {
        return Arrays.stream(type.getDeclaredMethods())
            .filter(m -> !Modifier.isStatic(m.getModifiers()))
            .filter(m -> m.getParameterCount() == 1)
            .filter(m -> hasMatchingName(m, key))
            .findAny()
            .orElse(null);
    }

    private static boolean hasMatchingName(Method method, String key) {
        Named annotation = method.getAnnotation(Named.class);
        if (annotation == null) {
            // The method is not named, so we look for exact matches or leading "set"
            return method.getName().equalsIgnoreCase(key)
                || method.getName().equalsIgnoreCase("set" + key);
        } else {
            // If the setter is named, the name must match exactly, ignoring case.
            return annotation.value().equalsIgnoreCase(key);
        }
    }

    private Field getFieldForKey(Class<?> type, String key) {
        return Arrays.stream(type.getDeclaredFields())
            .filter(f -> !Modifier.isStatic(f.getModifiers()))
            .filter(f -> !Modifier.isFinal(f.getModifiers()))
            .filter(f -> hasMatchingName(f, key))
            .findAny()
            .orElse(null);
    }

    private static boolean hasMatchingName(Field method, String key) {
        Named annotation = method.getAnnotation(Named.class);
        if (annotation == null) {
            // The field is not named, so we look for exact matches, ignoring case.
            return method.getName().equalsIgnoreCase(key);
        } else {
            // If the setter is named, the name must match exactly, ignoring case.
            return annotation.value().equalsIgnoreCase(key);
        }
    }

    private Optional<Object> toParameterValue(Class<?> type, List<String> rawValues) {
        if (type.isArray()) {
            Class<?> elementType = type.getComponentType();
            List<Object> values = rawValues.stream()
                .map((v) -> toPrimitiveValue(elementType, v))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
            return Optional.of(getArray(elementType, values));
        } else {
            return toPrimitiveValue(type, String.join(";", rawValues));
        }
    }

    private Object getArray(Class<?> componentType, List<Object> values) {
        Object results = Array.newInstance(componentType, values.size());
        for (int index = 0; index != values.size(); ++index) {
            Object rawValue = values.get(index);
            Array.set(results, index, rawValue);
        }
        return results;
    }

    private Optional<Object> toPrimitiveValue(Class<?> type, String value) {
        if (type.isPrimitive()) {
            type = primitiveToBoxed.get(type);
        }
        Function<String, Object> converter = primitiveConverters.get(type);
        if (converter == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(converter.apply(value));
    }

    private static class ParameterCache {
        private final Supplier<Map<String, List<String>>> getter;
        private Map<String, List<String>> lookup;

        public ParameterCache(Supplier<Map<String, List<String>>> getter) {
            this.getter = getter;
        }

        public Collection<String> getKeys() {
            Map<String, List<String>> lookup = getLookup();
            return lookup.keySet();
        }

        public boolean hasValue(String name) {
            Map<String, List<String>> lookup = getLookup();
            return lookup.containsKey(sterilize(name));
        }

        public List<String> getValues(String name) {
            Map<String, List<String>> lookup = getLookup();
            return lookup.get(sterilize(name));
        }

        private Map<String, List<String>> getLookup() {
            if (lookup != null) {
                return lookup;
            }
            Map<String, List<String>> cache = new HashMap<>();
            Map<String, List<String>> source = getter.get();
            for (String name : source.keySet()) {
                String sterilized = sterilize(name);
                List<String> values = source.get(name);
                if (cache.containsKey(sterilized)) {
                    cache.get(sterilized).addAll(values);  // fold overlapping values together
                } else {
                    cache.put(sterilized, values);
                }
            }
            lookup = cache;
            return lookup;
        }

        private static String sterilize(String name) {
            return name == null ? null : name.trim().toUpperCase();
        }
    }
}
