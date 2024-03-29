package com.truncon.javalin.mvc.test;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.utils.URIBuilder;

import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public final class RouteBuilder {
    private RouteBuilder() {
    }

    public static String buildRoute(String path) {
        return buildRoute(path, Collections.emptyMap(), Collections.emptyList(), false);
    }

    public static String buildRouteWithPathParams(String path, Map<String, String> pathReplacements) {
        return buildRoute(path, pathReplacements, Collections.emptyList(), false);
    }

    public static String buildRouteWithQueryParams(String path, Collection<Pair<String, String>> queryString) {
        return buildRoute(path, Collections.emptyMap(), queryString, false);
    }

    public static String buildWsRoute(String path) {
        return buildRoute(path, Collections.emptyMap(), Collections.emptyList(), true);
    }

    public static String buildWsRouteWithPathParams(String path, Map<String, String> pathReplacements) {
        return buildRoute(path, pathReplacements, Collections.emptyList(), true);
    }

    public static String buildWsRouteWithQueryParams(String path, Collection<Pair<String, String>> queryString) {
        return buildRoute(path, Collections.emptyMap(), queryString, true);
    }

    public static String buildRoute(
            String path,
            Map<String, String> pathReplacements,
            Collection<Pair<String, String>> query,
            boolean isWebSockets) {
        if (path == null) {
            path = "";
        } else {
            path = StringUtils.prependIfMissing(path, "/");
        }
        try {
            for (String pathKey : pathReplacements.keySet()) {
                String replacement = pathReplacements.get(pathKey);
                replacement = URLEncoder.encode(replacement, StandardCharsets.UTF_8.name());
                String placeholder = "\\{" + pathKey + "}";
                path = path.replaceAll(placeholder, replacement);
            }
        } catch (UnsupportedEncodingException exception) {
            throw new UncheckedIOException(exception);
        }
        String route = (isWebSockets ? "ws" : "http") + "://localhost:" + AppHost.PORT + path;
        route = appendQueryString(route, query);
        return route;
    }

    public static String appendQueryString(String route, Collection<Pair<String, String>> query) {
        try {
            URIBuilder builder = new URIBuilder(route);
            for (Pair<String, String> nameValuePair : query) {
                builder.addParameter(nameValuePair.getKey(), nameValuePair.getValue());
            }
            return builder.toString();
        } catch (URISyntaxException exception) {
            throw new RuntimeException(exception);
        }
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
