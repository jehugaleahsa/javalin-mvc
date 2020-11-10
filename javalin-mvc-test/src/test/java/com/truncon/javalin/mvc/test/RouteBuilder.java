package com.truncon.javalin.mvc.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class RouteBuilder {
    private RouteBuilder() {
    }

    public static String buildRoute(String path) throws IOException {
        return buildRoute(path, Collections.emptyMap(), Collections.emptyList(), false);
    }

    public static String buildRouteWithPathParams(String path, Map<String, String> pathReplacements) throws IOException {
        return buildRoute(path, pathReplacements, Collections.emptyList(), false);
    }

    public static String buildRouteWithQueryParams(String path, Collection<Pair<String, String>> queryString) throws IOException {
        return buildRoute(path, Collections.emptyMap(), queryString, false);
    }

    public static String buildWsRoute(String path) throws IOException {
        return buildRoute(path, Collections.emptyMap(), Collections.emptyList(), true);
    }

    public static String buildWsRouteWithPathParams(String path, Map<String, String> pathReplacements) throws IOException {
        return buildRoute(path, pathReplacements, Collections.emptyList(), true);
    }

    public static String buildWsRouteWithQueryParams(String path, Collection<Pair<String, String>> queryString) throws IOException {
        return buildRoute(path, Collections.emptyMap(), queryString, true);
    }

    public static String buildRoute(
            String path,
            Map<String, String> pathReplacements,
            Collection<Pair<String, String>> query,
            boolean isWebSockets) throws IOException {
        if (path == null) {
            path = "";
        } else {
            path = StringUtils.prependIfMissing(path, "/");
        }
        for (String pathKey : pathReplacements.keySet()) {
            String replacement = pathReplacements.get(pathKey);
            replacement = URLEncoder.encode(replacement, StandardCharsets.UTF_8.name());
            String placeholder = StringUtils.prependIfMissing(pathKey, ":");
            path = path.replaceAll(placeholder, replacement);
        }
        String queryString = query.stream().map(pair -> {
            try {
                String value = pair.getValue();
                value = StringUtils.defaultIfBlank(value, "");
                value = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
                return pair.getKey() + "=" + value;
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }).collect(Collectors.joining("&"));
        String route = (isWebSockets ? "ws" : "http") + "://localhost:" + AppHost.PORT + path;
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
    public static Collection<Pair<String, String>> queryParams(Pair<String, String> ...pairs) {
        return Arrays.asList(pairs);
    }

    @SafeVarargs
    public static Collection<Pair<String, String>> headerParams(Pair<String, String> ...pairs) {
        return Arrays.asList(pairs);
    }

    @SafeVarargs
    public static Collection<Pair<String, String>> cookieParams(Pair<String, String> ...pairs) {
        return Arrays.asList(pairs);
    }

    @SafeVarargs
    public static Collection<Pair<String, String>> formData(Pair<String, String> ...pairs) {
        return Arrays.asList(pairs);
    }

    public static Pair<String, String> param(String key, String value) {
        return Pair.of(key, value);
    }
}
