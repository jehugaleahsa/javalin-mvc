package com.truncon.javalin.mvc.api;

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
    public String toJson(Object data) {
        this.toJsonCalled = true;
        return null;
    }

    @Override
    public <T> T fromJson(String json, Class<T> dataClass) {
        return null;
    }

    @Override
    public Object getHandle() {
        return null;
    }
}
