package com.truncon.javalin.mvc.test;

import io.javalin.plugin.json.JavalinJackson;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public final class QueryUtils {
    private QueryUtils() {
    }

    public static String getJsonString(Object value) {
        return JavalinJackson.INSTANCE.toJson(value);
    }

    public static <T> T parseJson(String json, Class<T> clz) {
        return JavalinJackson.INSTANCE.fromJson(json, clz);
    }

    public static String getGetStringResponse(String route) throws IOException {
        return getGetStringResponseWithHeaders(route, Collections.emptyList());
    }

    public static String getGetStringResponseWithHeaders(String route, Collection<Pair<String, String>> headers) throws IOException {
        Request request = Request.Get(route);
        for (Pair<String, String> header : headers) {
            request = request.addHeader(header.getKey(), header.getValue());
        }
        return request.execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
    }

    public static String getGetStringResponseWithCookies(String route, Collection<Pair<String, String>> cookies) throws IOException {
        Request request = Request.Get(route);
        String cookieString = cookies.stream()
            .map(p -> p.getKey() + "=" + p.getValue())
            .collect(Collectors.joining(";"));
        request = request.addHeader("Cookie", cookieString);
        return request.execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
    }

    public static <T> T getGetJsonResponse(String route, Class<T> clz) throws Exception {
        String json = Request.Get(route)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
        return parseJson(json, clz);
    }

    public static <T> T getPostJsonResponse(String route, Object body, Class<T> clz) throws Exception {
        String json = Request.Post(route)
            .bodyString(getJsonString(body), ContentType.APPLICATION_JSON)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
        return parseJson(json, clz);
    }
}
