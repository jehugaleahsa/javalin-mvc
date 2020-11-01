package com.truncon.javalin.mvc;

import com.truncon.javalin.mvc.api.Named;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class ParameterCache {
    private final Supplier<Map<String, Collection<String>>> getter;
    private Map<String, Collection<String>> lookup;

    public ParameterCache(Supplier<Map<String, Collection<String>>> getter) {
        this.getter = getter;
    }

    public Collection<String> getKeys() {
        Map<String, Collection<String>> lookup = getLookup();
        return lookup.keySet();
    }

    public boolean hasValue(String name) {
        Map<String, Collection<String>> lookup = getLookup();
        return lookup.containsKey(sterilize(name));
    }

    public Collection<String> getValues(String name) {
        Map<String, Collection<String>> lookup = getLookup();
        return lookup.get(sterilize(name));
    }

    private Map<String, Collection<String>> getLookup() {
        if (lookup != null) {
            return lookup;
        }
        Map<String, Collection<String>> cache = new LinkedHashMap<>();
        Map<String, Collection<String>> source = getter.get();
        for (String name : source.keySet()) {
            String sterilized = sterilize(name);
            Collection<String> values = source.get(name);
            cache.computeIfAbsent(sterilized, k -> new ArrayList<>()).addAll(values);
        }
        lookup = cache;
        return lookup;
    }

    private static String sterilize(String name) {
        return name == null ? null : name.trim().toUpperCase();
    }

    public Object bindValues(Class<?> type) {
        try {
            Object instance = type.newInstance();
            for (String key : getKeys()) {
                bindValue(type, instance, key);
            }
            return instance;
        } catch (Exception exception) {
            // swallow any deserialization errors
            return null;
        }
    }

    private void bindValue(Class<?> type, Object instance, String key) {
        try {
            Method method = getMethodForKey(type, key);
            if (method != null) {
                method.setAccessible(true);
                Collection<String> rawValues = getValues(key);
                Class<?> parameterType = method.getParameters()[0].getType();
                Optional<Object> value = ConversionUtils.toParameterValue(parameterType, rawValues);
                if (value.isPresent()) {
                    method.invoke(instance, value.get());
                    return;
                }
            }
            Field field = getFieldForKey(type, key);
            if (field != null) {
                field.setAccessible(true);
                Collection<String> rawValues = getValues(key);
                Optional<Object> value = ConversionUtils.toParameterValue(field.getType(), rawValues);
                if (value.isPresent()) {
                    field.set(instance, value.get());
                }
            }
        } catch (Exception exception) {
            // Swallow any binding errors
        }
    }

    private static Method getMethodForKey(Class<?> type, String key) {
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

    private static Field getFieldForKey(Class<?> type, String key) {
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
}
