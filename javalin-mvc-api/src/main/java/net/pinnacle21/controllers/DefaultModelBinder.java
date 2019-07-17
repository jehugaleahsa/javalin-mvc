package io.javalin.mvc.api;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Performs model binding from header, URL path parameter, query string, form fields and the request body.
 */
public class DefaultModelBinder implements ModelBinder {
    private static final Map<Class<?>, Function<String, Object>> primitiveConverters = getPrimitiveConverters();
    private final HttpRequest request;
    private final ParameterCache headers;
    private final ParameterCache pathParameters;
    private final ParameterCache queryStrings;
    private final ParameterCache formFields;

    private static Map<Class<?>, Function<String, Object>> getPrimitiveConverters() {
        Map<Class<?>, Function<String, Object>> converters = new HashMap<>();
        converters.put(String.class, s -> s);  // we treat String as a primitive
        converters.put(Integer.class, Integer::parseInt);
        converters.put(Boolean.class, Boolean::parseBoolean);
        converters.put(Date.class, DefaultModelBinder::toDate);
        converters.put(Instant.class, DefaultModelBinder::toInstant);
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
     * @param request the current request object.
     */
    public DefaultModelBinder(HttpRequest request) {
        this.request = request;
        this.headers = new ParameterCache(request::getHeaderLookup);
        this.pathParameters = new ParameterCache(request::getPathLookup);
        this.queryStrings = new ParameterCache(request::getQueryLookup);
        this.formFields = new ParameterCache(request::getFormLookup);
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
    public Object getValue(String name, Class<?> parameterClass, ValueSource valueSource) {
        if (isArray(parameterClass)) {
            return getArray(name, parameterClass, valueSource);
        } else if (isPrimitive(parameterClass)) {
            return getPrimitiveValue(name, parameterClass, valueSource);
        } else {
            return getDeserializedBody(parameterClass);
        }
    }

    private static boolean isArray(Class<?> parameterClass) {
        if (!parameterClass.isArray()) {
            return false;
        }
        Class<?> componentType = parameterClass.getComponentType();
        return isPrimitive(componentType);
    }

    private Object getArray(String name, Class<?> parameterClass, ValueSource valueSource) {
        try {
            Result rawResult = getRawValue(name, valueSource);
            if (!rawResult.hasValue()) {
                return null;
            }
            List<String> rawValues = rawResult.getValues();
            Class<?> primitiveType = parameterClass.getComponentType();
            Object results = Array.newInstance(primitiveType, rawValues.size());
            for (int index = 0; index != rawValues.size(); ++index) {
                String rawValue = rawValues.get(index);
                Array.set(results, index, toPrimitive(primitiveType, rawValue));
            }
            return results;
        } catch (Exception exception) {
            // Swallow any parsing errors
        }
        return null;
    }

    private static boolean isPrimitive(Class<?> parameterClass) {
        if (parameterClass.isPrimitive()) {
            return true;
        }
        return primitiveConverters.containsKey(parameterClass);
    }

    private Object getPrimitiveValue(String name, Class<?> parameterClass, ValueSource valueSource) {
        try {
            Result rawResult = getRawValue(name, valueSource);
            if (!rawResult.hasValue()) {
                return null;
            }
            String rawValue = rawResult.getValue();
            return toPrimitive(parameterClass, rawValue);
        } catch (Exception exception) {
            // swallow any parsing errors
        }
        return null;
    }

    private static Object toPrimitive(Class<?> primitiveClass, String value) {
        Function<String, Object> converter = primitiveConverters.get(primitiveClass);
        return converter.apply(value);
    }

    private Result getRawValue(String name, ValueSource valueSource) {
        if (isValidSource(valueSource, ValueSource.Header) && headers.hasValue(name)) {
            return Result.forValue(headers.getValues(name));
        }
        if (isValidSource(valueSource, ValueSource.Path) && pathParameters.hasValue(name)) {
            return Result.forValue(pathParameters.getValues(name));
        }
        if (isValidSource(valueSource, ValueSource.QueryString) && queryStrings.hasValue(name)) {
            return Result.forValue(queryStrings.getValues(name));
        }
        if (isValidSource(valueSource, ValueSource.FormData) && formFields.hasValue(name)) {
            return Result.forValue(formFields.getValues(name));
        }
        return Result.empty();
    }

    private static boolean isValidSource(ValueSource actual, ValueSource required) {
        return actual == ValueSource.Any || actual == required;
    }

    private Object getDeserializedBody(Class<?> paramType) {
        try {
            return request.getBodyFromJson(paramType);
        } catch (Exception exception) {
            // swallow any deserialization errors
            return null;
        }
    }

    private static class Result {
        private final boolean hasValue;
        private final List<String> values;

        private Result(boolean hasValue, List<String> values) {
            this.hasValue = hasValue;
            this.values = values;
        }

        public static Result forValue(List<String> values) {
            return new Result(true, values);
        }

        public static Result empty() {
            return new Result(false, null);
        }

        public boolean hasValue() {
            return hasValue;
        }

        public String getValue() {
            return String.join(";", values);
        }

        public List<String> getValues() {
            return Collections.unmodifiableList(values);
        }
    }

    private static class ParameterCache {
        private final Supplier<Map<String, List<String>>> getter;
        private Map<String, List<String>> lookup;

        public ParameterCache(Supplier<Map<String, List<String>>> getter) {
            this.getter = getter;
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
