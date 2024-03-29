package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import com.truncon.javalin.mvc.api.ws.WsResponse;

import java.util.Objects;

public abstract class JavalinWsContext implements WsContext {
    private final io.javalin.websocket.WsContext context;

    public JavalinWsContext(io.javalin.websocket.WsContext context) {
        Objects.requireNonNull(context);
        this.context = context;
    }

    @Override
    public String getSessionId() {
        return context.getSessionId();
    }

    @Override
    public WsRequest getRequest() {
        return new JavalinWsRequest(context);
    }

    @Override
    public WsResponse getResponse() {
        return new JavalinWsResponse(context);
    }

    @Override
    public io.javalin.websocket.WsContext getHandle() {
        return context;
    }
}
