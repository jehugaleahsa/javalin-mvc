package com.truncon.javalin.mvc.api.ws;

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
    public Object getHandle() {
        return null;
    }
}
