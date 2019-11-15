package com.truncon.javalin.mvc;

import com.truncon.javalin.mvc.api.Named;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Supplier;

public final class ParameterCache {
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
                List<String> rawValues = getValues(key);
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
                List<String> rawValues = getValues(key);
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
