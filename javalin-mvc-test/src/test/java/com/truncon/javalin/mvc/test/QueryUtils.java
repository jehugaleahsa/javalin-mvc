package com.truncon.javalin.mvc.test;

import io.javalin.plugin.json.JavalinJackson;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class QueryUtils {
    private QueryUtils() {
    }

    public static String jsonStringify(Object value) throws Exception {
        return JavalinJackson.INSTANCE.toJson(value);
    }

    public static String getStringResponse(String route) throws IOException {
        return Request.Get(route)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
    }

    public static <T> T getJsonResponse(String route, Class<T> clz) throws IOException {
        String json = getStringResponse(route);
        return JavalinJackson.INSTANCE.fromJson(json, clz);
    }
}
