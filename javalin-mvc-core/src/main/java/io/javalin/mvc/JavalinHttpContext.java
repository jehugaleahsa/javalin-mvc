package io.javalin.mvc;

import java.util.Objects;

import io.javalin.http.Context;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.mvc.api.HttpContext;
import io.javalin.mvc.api.HttpRequest;
import io.javalin.mvc.api.HttpResponse;

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
        return JavalinJackson.INSTANCE.toJson(data);
    }

    @Override
    public <T> T fromJson(String json, Class<T> dataClass) {
        return JavalinJackson.INSTANCE.fromJson(json, dataClass);
    }

    @Override
    public Object getHandle() {
        return context;
    }
}
