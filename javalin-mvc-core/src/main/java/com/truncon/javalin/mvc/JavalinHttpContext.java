package com.truncon.javalin.mvc;

import java.util.Objects;

import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.HttpResponse;
import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJson;

public final class JavalinHttpContext implements HttpContext {
    private final Context context;

    public JavalinHttpContext(Context context) {
        Objects.requireNonNull(context);
        this.context = context;
    }

    @Override
    public HttpRequest getRequest() {
        return new JavalinHttpRequest(context);
    }

    @Override
    public HttpResponse getResponse() {
        return new JavalinHttpResponse(context);
    }

    @Override
    public String toJson(Object data) {
        return JavalinJson.toJson(data);
    }

    @Override
    public <T> T fromJson(String json, Class<T> dataClass) {
        return JavalinJson.fromJson(json, dataClass);
    }

    @Override
    public Object getHandle() {
        return context;
    }
}
