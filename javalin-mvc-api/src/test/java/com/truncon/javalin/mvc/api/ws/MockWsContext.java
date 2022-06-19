package com.truncon.javalin.mvc.api.ws;

import java.io.InputStream;
import java.util.UUID;

public final class MockWsContext implements WsContext {
    private final MockWsResponse response = new MockWsResponse();

    @Override
    public String getSessionId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public WsRequest getRequest() {
        return null;
    }

    @Override
    public MockWsResponse getResponse() {
        return response;
    }

    @Override
    public String toJsonString(Object data) {
        return null;
    }

    @Override
    public InputStream toJsonStream(Object data) {
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
