package com.truncon.javalin.mvc;

import com.truncon.javalin.mvc.api.ValueSource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

final class ParameterCache {
    private final ValueSource valueSource;
    private final Supplier<Map<String, Collection<String>>> getter;
    private Map<String, Collection<String>> lookup;

    public ParameterCache(ValueSource valueSource, Supplier<Map<String, Collection<String>>> getter) {
        this.valueSource = valueSource;
        this.getter = getter;
    }

    public ValueSource getValueSource() {
        return valueSource;
    }

    public boolean hasValue(String name) {
        Map<String, Collection<String>> lookup = getLookup();
        return lookup.containsKey(name);
    }

    public Collection<String> getValues(String name) {
        Map<String, Collection<String>> lookup = getLookup();
        return lookup.get(name);
    }

    public Map<String, Collection<String>> getLookup() {
        if (lookup != null) {
            return lookup;
        }
        Map<String, Collection<String>> cache = new LinkedHashMap<>();
        Map<String, Collection<String>> source = getter.get();
        for (String name : source.keySet()) {
            Collection<String> values = source.get(name);
            cache.computeIfAbsent(name, k -> new ArrayList<>()).addAll(values);
        }
        lookup = cache;
        return lookup;
    }
}
