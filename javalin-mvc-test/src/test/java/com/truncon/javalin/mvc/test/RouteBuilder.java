package com.truncon.javalin.mvc.test;

import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public final class RouteBuilder {
    private RouteBuilder() {
    }

    public static String buildRoute(String path) throws IOException {
        return buildRoute(path, Collections.emptyMap());
    }

    public static String buildRoute(String path, Map<String, String> pathReplacements) throws IOException {
        return buildRoute(path, pathReplacements, Collections.emptyList());
    }

    public static String buildRoute(
            String path,
            Map<String, String> pathReplacements,
            List<Pair<String, String>> query) throws IOException {
        Properties settings = loadConfig();
        if (path == null) {
            path = "";
        } else if (!path.startsWith("/")) {
            path = "/" + path;
        }
        for (String pathKey : pathReplacements.keySet()) {
            String replacement = pathReplacements.get(pathKey);
            replacement = URLEncoder.encode(replacement, StandardCharsets.UTF_8.name());
            if (!pathKey.startsWith(":")) {
                pathKey = ":" + pathKey;
            }
            path = path.replaceAll(pathKey, replacement);
        }
        String queryString = query.stream().map(p -> {
            try {
                String value = URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8.name());
                return p.getKey() + "=" + value;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }).collect(Collectors.joining("&"));
        String route = "http://localhost:" + settings.getProperty("port") + path;
        if (!queryString.isEmpty()) {
            route = route + "?" + queryString;
        }
        return route;
    }

    @SafeVarargs
    public static Map<String, String> pathParams(Pair<String, String> ...pairs) {
        Map<String, String> values = new HashMap<>();
        for (Pair<String, String> pair : pairs) {
            values.put(pair.getKey(), pair.getValue());
        }
        return values;
    }

    @SafeVarargs
    public static List<Pair<String, String>> queryParams(Pair<String, String> ...pairs) {
        return Arrays.asList(pairs);
    }

    public static Pair<String, String> param(String key, String value) {
        return Pair.of(key, value);
    }

    private static Properties loadConfig() throws IOException {
        return SettingsUtilities.loadSettings(
                "./config/application.properties",
                "./config/application-test.properties");
    }
}
