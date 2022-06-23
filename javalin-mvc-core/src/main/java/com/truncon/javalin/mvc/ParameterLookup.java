package com.truncon.javalin.mvc;

import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromForm;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromJson;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ValueSource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class ParameterLookup {
    private static final Map<Class<?>, Object> PRIMITIVE_DEFAULT_VALUES = getDefaultPrimitiveValues();
    private static final Map<Class<? extends Annotation>, ValueSource> BINDING_ANNOTATION_TYPES = getFromMappings();

    private static Map<Class<?>, Object> getDefaultPrimitiveValues() {
        Map<Class<?>, Object> map = new HashMap<>(9);
        map.put(int.class, 0);
        map.put(long.class, 0L);
        map.put(short.class, (short) 0);
        map.put(byte.class, (byte) 0);
        map.put(char.class, '\0');
        map.put(float.class, 0.0f);
        map.put(double.class, 0.0);
        map.put(boolean.class, false);
        return map;
    }

    private static Map<Class<? extends Annotation>, ValueSource> getFromMappings() {
        Map<Class<? extends Annotation>, ValueSource> map = new LinkedHashMap<>(5);
        map.put(FromHeader.class, ValueSource.Header);
        map.put(FromCookie.class, ValueSource.Cookie);
        map.put(FromPath.class, ValueSource.Path);
        map.put(FromQuery.class, ValueSource.QueryString);
        map.put(FromForm.class, ValueSource.FormData);
        map.put(FromJson.class, ValueSource.Json);
        return map;
    }

    private final Map<ValueSource, ParameterCache> valueSources = new HashMap<>();

    public void addCache(ParameterCache cache) {
        valueSources.put(cache.getValueSource(), cache);
    }

    public Object bindValues(Class<?> type, ValueSource defaultSource) {
        if (type.isPrimitive()) {
            return PRIMITIVE_DEFAULT_VALUES.get(type);
        }
        if (Modifier.isAbstract(type.getModifiers())) {
            return null;
        }
        try {
            Object instance = type.newInstance();
            // To efficiently match values to model properties we first loop
            // over all of the default source's keys. Then we look for specific
            // setters and fields with From* annotations.
            for (String key : getDefaultKeys(defaultSource)) {
                bindValue(type, defaultSource, instance, key);
            }
            for (BoundMethod boundMethod : getBoundMethods(type)) {
                Method method = boundMethod.getMethod();
                ValueSource source = boundMethod.getValueSource();
                String key = getKey(method);
                setMethodValue(source, instance, key, method);
            }
            for (BoundField boundField : getBoundFields(type)) {
                Field field = boundField.getField();
                ValueSource source = boundField.getValueSource();
                String key = getKey(field);
                setFieldValue(source, instance, key, field);
            }
            return instance;
        } catch (Exception exception) {
            // swallow any deserialization errors
            return null;
        }
    }

    private void bindValue(Class<?> type, ValueSource valueSource, Object instance, String key) {
        try {
            Method method = getMethodForKey(type, key);
            if (method != null && setMethodValue(valueSource, instance, key, method)) {
                return;
            }
            Field field = getFieldForKey(type, key);
            if (field != null) {
                setFieldValue(valueSource, instance, key, field);
            }
        } catch (Exception exception) {
            // Swallow any binding errors
        }
    }

    private static String getKey(Method method) {
        Named named = method.getAnnotation(Named.class);
        if (named != null) {
            return named.value();
        }
        return StringUtils.uncapitalize(StringUtils.removeStart(method.getName(), "set"));
    }

    private static String getKey(Field field) {
        Named named = field.getAnnotation(Named.class);
        if (named != null) {
            return named.value();
        }
        return field.getName();
    }

    private boolean setMethodValue(ValueSource valueSource, Object instance, String key, Method method) throws IllegalAccessException, InvocationTargetException {
        Collection<String> rawValues = getValues(valueSource, key);
        Class<?> parameterType = method.getParameters()[0].getType();
        Optional<Object> value = ConversionUtils.toParameterValue(parameterType, rawValues);
        if (value.isPresent()) {
            method.setAccessible(true);
            method.invoke(instance, value.get());
            return true;
        }
        return false;
    }

    private void setFieldValue(ValueSource valueSource, Object instance, String key, Field field) throws IllegalAccessException {
        Collection<String> rawValues = getValues(valueSource, key);
        Optional<Object> value = ConversionUtils.toParameterValue(field.getType(), rawValues);
        if (value.isPresent()) {
            field.setAccessible(true);
            field.set(instance, value.get());
        }
    }

    public Collection<String> getDefaultKeys(ValueSource defaultSource) {
        ParameterCache cache = this.valueSources.get(defaultSource);
        if (cache == null) {
            return Collections.emptySet();
        }
        Map<String, Collection<String>> lookup = cache.getLookup();
        return lookup.keySet();
    }

    public Collection<String> getValues(ValueSource source, String name) {
        ParameterCache cache = this.valueSources.get(source);
        if (cache == null) {
            return Collections.emptyList();
        }
        Map<String, Collection<String>> lookup = cache.getLookup();
        return lookup.get(name);
    }

    private static Collection<BoundMethod> getBoundMethods(Class<?> type) {
        List<BoundMethod> pairs = new ArrayList<>();
        for (Method method : getMethods(type)) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            if (method.getParameterCount() != 1) {
                continue;
            }
            for (Map.Entry<Class<? extends Annotation>, ValueSource> entry : BINDING_ANNOTATION_TYPES.entrySet()) {
                Annotation[] annotations = method.getAnnotationsByType(entry.getKey());
                if (annotations.length > 0) {
                    pairs.add(new BoundMethod(method, entry.getValue()));
                    break;
                }
            }
        }
        return pairs;
    }

    private static Collection<BoundField> getBoundFields(Class<?> type) {
        List<BoundField> pairs = new ArrayList<>();
        for (Field field : getFields(type)) {
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            for (Map.Entry<Class<? extends Annotation>, ValueSource> entry : BINDING_ANNOTATION_TYPES.entrySet()) {
                Annotation[] annotations = field.getAnnotationsByType(entry.getKey());
                if (annotations.length > 0) {
                    pairs.add(new BoundField(field, entry.getValue()));
                    break;
                }
            }
        }
        return pairs;
    }

    public static boolean hasBindings(Class<?> type) {
        return hasMethodBindings(type) || hasFieldBindings(type);
    }

    private static boolean hasMethodBindings(Class<?> type) {
        return getMethods(type).stream()
            .filter(m -> !Modifier.isStatic(m.getModifiers()))
            .flatMap(m -> BINDING_ANNOTATION_TYPES.keySet().stream().map(m::getAnnotationsByType).flatMap(Arrays::stream))
            .anyMatch(m -> true);
    }

    private static boolean hasFieldBindings(Class<?> type) {
        return getFields(type).stream()
            .filter(f -> !Modifier.isStatic(f.getModifiers()))
            .filter(f -> !Modifier.isFinal(f.getModifiers()))
            .flatMap(f -> BINDING_ANNOTATION_TYPES.keySet().stream().map(f::getAnnotationsByType).flatMap(Arrays::stream))
            .anyMatch(a -> true);
    }

    private static Method getMethodForKey(Class<?> type, String key) {
        return getMethods(type).stream()
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

    private static Field getFieldForKey(Class<?> type, String key) {
        return getFields(type).stream()
            .filter(f -> !Modifier.isStatic(f.getModifiers()))
            .filter(f -> !Modifier.isFinal(f.getModifiers()))
            .filter(f -> hasMatchingName(f, key))
            .findAny()
            .orElse(null);
    }

    private static boolean hasMatchingName(Field field, String key) {
        Named annotation = field.getAnnotation(Named.class);
        if (annotation == null) {
            // The field is not named, so we look for exact matches, ignoring case.
            return field.getName().equalsIgnoreCase(key);
        } else {
            // If the setter is named, the name must match exactly, ignoring case.
            return annotation.value().equalsIgnoreCase(key);
        }
    }

    private static Collection<Method> getMethods(Class<?> type) {
        return getMembers(type, Class::getDeclaredMethods);
    }

    private static Collection<Field> getFields(Class<?> type) {
        return getMembers(type, Class::getDeclaredFields);
    }

    private static <T> Collection<T> getMembers(Class<?> type, Function<Class<?>, T[]> accessor) {
        List<T> members = new ArrayList<>();
        Class<?> next = type;
        while (next != null && next != Object.class) {
            members.addAll(Arrays.asList(accessor.apply(next)));
            next = next.getSuperclass();
        }
        return members;
    }

    private static final class BoundMethod {
        private final Method method;
        private final ValueSource valueSource;

        public BoundMethod(Method method, ValueSource valueSource) {
            this.method = method;
            this.valueSource = valueSource;
        }

        public Method getMethod() {
            return method;
        }

        public ValueSource getValueSource() {
            return valueSource;
        }
    }

    private static final class BoundField {
        private final Field field;
        private final ValueSource valueSource;

        public BoundField(Field field, ValueSource valueSource) {
            this.field = field;
            this.valueSource = valueSource;
        }

        public Field getField() {
            return field;
        }

        public ValueSource getValueSource() {
            return valueSource;
        }
    }
}
