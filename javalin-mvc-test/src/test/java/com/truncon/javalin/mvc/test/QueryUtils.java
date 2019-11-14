package com.truncon.javalin.mvc.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.fluent.Request;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class QueryUtils {
    private QueryUtils() {
    }

    public static String jsonStringify(Object value) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(value);
    }

    public static String getStringResponse(String route) throws IOException {
        return Request.Get(route)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
    }

    public static <T> T getJsonResponse(String route, Class<T> clz) throws IOException {
        String json = getStringResponse(route);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, clz);
    }
}
