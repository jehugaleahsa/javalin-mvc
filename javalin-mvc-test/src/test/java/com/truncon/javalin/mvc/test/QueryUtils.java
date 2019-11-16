package com.truncon.javalin.mvc.test;

import io.javalin.plugin.json.JavalinJackson;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class QueryUtils {
    private QueryUtils() {
    }

    public static String jsonStringify(Object value) throws Exception {
        return JavalinJackson.INSTANCE.toJson(value);
    }

    public static <T> T jsonParse(String json, Class<T> clz) {
        return JavalinJackson.INSTANCE.fromJson(json, clz);
    }

    public static String getStringResponse(String route) throws IOException {
        return Request.Get(route)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
    }

    public static <T> T getGetJsonResponse(String route, Class<T> clz) throws Exception {
        String json = Request.Get(route)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
        return JavalinJackson.INSTANCE.fromJson(json, clz);
    }

    public static <T> T getPostJsonResponse(String route, Object body, Class<T> clz) throws Exception {
        String json = Request.Post(route)
            .bodyString(jsonStringify(body), ContentType.APPLICATION_JSON)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
        return jsonParse(json, clz);
    }
}
