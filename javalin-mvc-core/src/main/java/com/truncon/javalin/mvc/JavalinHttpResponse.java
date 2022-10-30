package com.truncon.javalin.mvc;

import io.javalin.http.Context;

import com.truncon.javalin.mvc.api.HttpResponse;
import io.javalin.http.HttpStatus;

import java.io.InputStream;

final class JavalinHttpResponse implements HttpResponse {
    private static final String JSON_CONTENT_TYPE = "application/json";
    private final Context context;

    public JavalinHttpResponse(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public HttpResponse setStatusCode(int statusCode) {
        context.status(statusCode);
        return this;
    }

    @Override
    public HttpResponse setContentType(String contentType) {
        context.contentType(contentType);
        return this;
    }

    @Override
    public HttpResponse setHeader(String name, String value) {
        context.header(name, value);
        return this;
    }

    @Override
    public HttpResponse setTextBody(String content) {
        context.result(content);
        return this;
    }

    @Override
    public HttpResponse setHtmlBody(String content) {
        context.html(content);
        return this;
    }

    @Override
    public HttpResponse setJsonBody(Object data) {
        if (data == null) {
            context.contentType(JSON_CONTENT_TYPE);
            context.result("null");
        } else {
            context.json(data);
        }
        return this;
    }

    @Override
    public HttpResponse setJsonStreamBody(Object data) {
        if (data == null) {
            context.contentType(JSON_CONTENT_TYPE);
            context.result("null");
        } else {
            context.jsonStream(data);
        }
        return this;
    }

    @Override
    public HttpResponse setStreamBody(InputStream stream) {
        context.result(stream);
        return this;
    }

    @Override
    public HttpResponse redirect(String location) {
        context.redirect(location);
        return this;
    }

    @Override
    public HttpResponse redirect(String location, int statusCode) {
        context.redirect(location, HttpStatus.forStatus(statusCode));
        return this;
    }

    @Override
    public HttpResponse setCookie(String name, String value) {
        context.cookie(name, value);
        return this;
    }

    @Override
    public HttpResponse setCookie(String name, String value, int maxAge) {
        context.cookie(name, value, maxAge);
        return this;
    }

    @Override
    public HttpResponse removeCookie(String name) {
        context.removeCookie(name);
        return this;
    }

    @Override
    public HttpResponse removeCookie(String name, String path) {
        context.removeCookie(name, path);
        return this;
    }
}
