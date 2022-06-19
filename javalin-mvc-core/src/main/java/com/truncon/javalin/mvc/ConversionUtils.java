package com.truncon.javalin.mvc;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

final class ConversionUtils {
    private static final Map<Class<?>, Function<String, Object>> primitiveConverters = getPrimitiveConverters();
    private static final Map<Class<?>, Class<?>> primitiveToBoxed = getPrimitiveToBoxed();

    private ConversionUtils() {
    }

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

    private static Map<Class<?>, Function<String, Object>> getPrimitiveConverters() {
        // We treat anything that can be parsed as a single value as "primitive".
        // Everything else we treat as an object that can be parsed from JSON.
        Map<Class<?>, Function<String, Object>> converters = new HashMap<>();
        converters.put(String.class, s -> s);
        converters.put(Integer.class, s -> StringUtils.isBlank(s) ? null : Integer.parseInt(s));
        converters.put(Boolean.class, s -> StringUtils.isBlank(s) ? null : Boolean.parseBoolean(s));
        converters.put(Date.class, ConversionUtils::toDate);
        converters.put(Instant.class, ConversionUtils::toInstant);
        converters.put(ZonedDateTime.class, s -> StringUtils.isBlank(s) ? null : ZonedDateTime.parse(s));
        converters.put(OffsetDateTime.class, s -> StringUtils.isBlank(s) ? null : OffsetDateTime.parse(s));
        converters.put(LocalDateTime.class, s -> StringUtils.isBlank(s) ? null : LocalDateTime.parse(s));
        converters.put(LocalDate.class, s -> StringUtils.isBlank(s) ? null : LocalDate.parse(s));
        converters.put(YearMonth.class, s -> StringUtils.isBlank(s) ? null : YearMonth.parse(s));
        converters.put(Year.class, s -> StringUtils.isBlank(s) ? null : Year.parse(s));
        converters.put(Double.class, s -> StringUtils.isBlank(s) ? null : Double.parseDouble(s));
        converters.put(Long.class, s -> StringUtils.isBlank(s) ? null : Long.parseLong(s));
        converters.put(Short.class, s -> StringUtils.isBlank(s) ? null : Short.parseShort(s));
        converters.put(Float.class, s -> StringUtils.isBlank(s) ? null : Float.parseFloat(s));
        converters.put(Character.class, s -> s.length() == 1 ? s.charAt(0) : null);
        converters.put(BigInteger.class, s -> StringUtils.isBlank(s) ? null : new BigInteger(s));
        converters.put(BigDecimal.class, s -> StringUtils.isBlank(s) ? null : new BigDecimal(s));
        converters.put(Byte.class, s -> StringUtils.isBlank(s) ? null : Byte.parseByte(s));
        converters.put(UUID.class, s -> StringUtils.isBlank(s) ? null : UUID.fromString(s));
        return converters;
    }

    private static Date toDate(String value) {
        Instant instant = toInstant(value);
        return instant == null ? null : Date.from(instant);
    }

    private static Instant toInstant(String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }
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

    public static Optional<Object> toParameterValue(Class<?> type, Collection<String> rawValues) {
        if (type.isArray()) {
            Class<?> elementType = type.getComponentType();
            List<Object> values = rawValues.stream()
                    .map((v) -> toPrimitiveValue(elementType, v))
                    .map(o -> o.orElse(null))
                    .collect(Collectors.toList());
            return Optional.of(getArray(elementType, values));
        } else {
            return toPrimitiveValue(type, String.join(";", rawValues));
        }
    }

    public static Optional<Object> toPrimitiveValue(Class<?> type, String value) {
        if (type.isPrimitive()) {
            type = primitiveToBoxed.get(type);
        }
        Function<String, Object> converter = primitiveConverters.get(type);
        if (converter == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(converter.apply(value));
    }

    private static Object getArray(Class<?> componentType, List<Object> values) {
        Object results = Array.newInstance(componentType, values.size());
        for (int index = 0; index != values.size(); ++index) {
            Object rawValue = values.get(index);
            Array.set(results, index, rawValue);
        }
        return results;
    }
}
