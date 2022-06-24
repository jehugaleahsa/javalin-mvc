package com.truncon.javalin.mvc.api;

public final class MockHttpContext implements HttpContext {
    private final MockHttpResponse response = new MockHttpResponse();

    @Override
    public HttpRequest getRequest() {
        return null;
    }

    @Override
    public MockHttpResponse getResponse() {
        return response;
    }

    @Override
    public Object getHandle() {
        return null;
    }
}
