package com.truncon.javalin.mvc.api;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public final class MockHttpResponse implements HttpResponse {
    private int statusCode;
    private String contentType;
    private String textBody;
    private InputStream streamBody;
    private Object jsonBody;
    private Object jsonStreamBody;
    private String redirectLocation;
    private final Map<String, String> headers = new LinkedHashMap<>();

    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public HttpResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public HttpResponse setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public String getHeader(String name) {
        return this.headers.get(name);
    }

    @Override
    public HttpResponse setHeader(String name, String value) {
        this.headers.put(name, value);
        return this;
    }

    public String getTextBody() {
        return textBody;
    }

    @Override
    public HttpResponse setTextBody(String content) {
        this.textBody = content;
        return this;
    }

    @Override
    public HttpResponse setHtmlBody(String content) {
        return null;
    }

    public Object getJsonBody() {
        return this.jsonBody;
    }

    @Override
    public HttpResponse setJsonBody(Object data) {
        this.jsonBody = data;
        return this;
    }

    public Object getJsonStreamBody() {
        return this.jsonStreamBody;
    }

    @Override
    public HttpResponse setJsonStreamBody(Object data) {
        this.jsonStreamBody = data;
        return this;
    }

    public InputStream getStreamBody() {
        return streamBody;
    }

    @Override
    public HttpResponse setStreamBody(InputStream stream) {
        this.streamBody = stream;
        return this;
    }

    public String getRedirectLocation() {
        return this.redirectLocation;
    }

    @Override
    public HttpResponse redirect(String location) {
        this.redirectLocation = location;
        return this;
    }

    @Override
    public HttpResponse redirect(String location, int statusCode) {
        this.redirectLocation = location;
        this.statusCode = statusCode;
        return null;
    }

    @Override
    public HttpResponse setCookie(String name, String value) {
        return null;
    }

    @Override
    public HttpResponse setCookie(String name, String value, int maxAge) {
        return null;
    }

    @Override
    public HttpResponse removeCookie(String name) {
        return null;
    }

    @Override
    public HttpResponse removeCookie(String name, String path) {
        return null;
    }
}
