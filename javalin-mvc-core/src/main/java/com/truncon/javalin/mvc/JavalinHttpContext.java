package com.truncon.javalin.mvc;

import java.io.InputStream;
import java.util.Objects;

import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.HttpResponse;
import io.javalin.http.Context;
import io.javalin.plugin.json.JsonMapper;

public final class JavalinHttpContext implements HttpContext {
    private final JsonMapper jsonMapper;
    private final Context context;

    public JavalinHttpContext(JsonMapper jsonMapper, Context context) {
        Objects.requireNonNull(jsonMapper);
        Objects.requireNonNull(context);
        this.jsonMapper = jsonMapper;
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
    public String toJsonString(Object data) {
        return jsonMapper.toJsonString(data);
    }

    public InputStream toJsonStream(Object data) {
        return jsonMapper.toJsonStream(data);
    }

    @Override
    public <T> T fromJsonString(String json, Class<T> dataClass) {
        return jsonMapper.fromJsonString(json, dataClass);
    }

    @Override
    public <T> T fromJsonStream(InputStream json, Class<T> dataClass) {
        return jsonMapper.fromJsonStream(json, dataClass);
    }

    @Override
    public Object getHandle() {
        return context;
    }
}
