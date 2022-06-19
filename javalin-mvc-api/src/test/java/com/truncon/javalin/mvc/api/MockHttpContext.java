package com.truncon.javalin.mvc.api;

import java.io.InputStream;

public final class MockHttpContext implements HttpContext {
    private final MockHttpResponse response = new MockHttpResponse();
    private boolean toJsonCalled;

    @Override
    public HttpRequest getRequest() {
        return null;
    }

    @Override
    public MockHttpResponse getResponse() {
        return response;
    }

    public boolean isToJsonCalled() {
        return this.toJsonCalled;
    }

    @Override
    public String toJsonString(Object data) {
        this.toJsonCalled = true;
        return null;
    }

    @Override
    public InputStream toJsonStream(Object data) {
        this.toJsonCalled = true;
        return null;
    }

    @Override
    public <T> T fromJsonString(String json, Class<T> dataClass) {
        return null;
    }

    @Override
    public <T> T fromJsonStream(InputStream json, Class<T> dataClass) {
        return null;
    }

    @Override
    public Object getHandle() {
        return null;
    }
}
