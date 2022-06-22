package com.truncon.javalin.mvc;

import java.util.Objects;

import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.HttpResponse;
import io.javalin.http.Context;

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
    public Context getHandle() {
        return context;
    }
}
