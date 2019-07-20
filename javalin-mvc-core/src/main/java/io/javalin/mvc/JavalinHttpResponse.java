package io.javalin.mvc;

import io.javalin.http.Context;

import io.javalin.mvc.api.HttpResponse;

import java.io.InputStream;

final class JavalinHttpResponse implements HttpResponse {
    private final Context context;

    public JavalinHttpResponse(Context context) {
        this.context = context;
    }

    public HttpResponse setStatusCode(int statusCode) {
        context.status(statusCode);
        return this;
    }

    public HttpResponse setContentType(String contentType) {
        context.contentType(contentType);
        return this;
    }

    public HttpResponse setHeader(String name, String value) {
        context.header(name, value);
        return this;
    }

    public HttpResponse setTextBody(String content) {
        context.result(content);
        return this;
    }

    public HttpResponse setHtmlBody(String content) {
        context.html(content);
        return this;
    }

    public HttpResponse setJsonBody(Object data) {
        if (data == null) {
            context.contentType("application/json");
            context.result("null");
        } else {
            context.json(data);
        }
        return this;
    }

    public HttpResponse setStreamBody(InputStream stream) {
        context.result(stream);
        return this;
    }

    public HttpResponse redirect(String location) {
        context.redirect(location);
        return this;
    }

    public HttpResponse redirect(String location, int statusCode) {
        context.redirect(location, statusCode);
        return this;
    }

    public HttpResponse setCookie(String name, String value) {
        context.cookie(name, value);
        return this;
    }

    public HttpResponse setCookie(String name, String value, int maxAge) {
        context.cookie(name, value, maxAge);
        return this;
    }

    public HttpResponse removeCookie(String name) {
        context.removeCookie(name);
        return this;
    }

    public HttpResponse removeCookie(String name, String path) {
        context.removeCookie(name, path);
        return this;
    }
}
