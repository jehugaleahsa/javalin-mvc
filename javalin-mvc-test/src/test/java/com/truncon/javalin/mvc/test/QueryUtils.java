package com.truncon.javalin.mvc.test;

import org.apache.http.client.fluent.Request;

import java.io.IOException;

public final class QueryUtils {
    private QueryUtils() {
    }

    public static String getStringResponse(String route) throws IOException {
        return Request.Get(route)
                .execute()
                .returnContent()
                .asString();
    }
}
