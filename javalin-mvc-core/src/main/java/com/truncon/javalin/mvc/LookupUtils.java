package com.truncon.javalin.mvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class LookupUtils {
    private LookupUtils() {
    }

    public static Map<String, List<String>> explode(Map<String, String> map) {
        Map<String, List<String>> exploded = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            exploded.put(key, Collections.singletonList(emptyToNull(value)));
        }
        return Collections.unmodifiableMap(exploded);
    }

    public static Map<String, List<String>> copy(Map<String, List<String>> map) {
        Map<String, List<String>> copy = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            List<String> valuesCopy = new ArrayList<>(values.size());
            for (String value : entry.getValue()) {
                valuesCopy.add(emptyToNull(value));
            }
            copy.put(key, Collections.unmodifiableList(valuesCopy));
        }
        return Collections.unmodifiableMap(copy);
    }

    private static String emptyToNull(String value) {
        return (value == null || value.isEmpty()) ? null : value;
    }
}
