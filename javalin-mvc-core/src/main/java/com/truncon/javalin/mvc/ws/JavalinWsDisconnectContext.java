package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsDisconnectContext;

public final class JavalinWsDisconnectContext implements WsDisconnectContext {
    private final io.javalin.websocket.WsCloseContext context;

    public JavalinWsDisconnectContext(io.javalin.websocket.WsCloseContext context) {
        this.context = context;
    }

    @Override
    public WsContext getContext() {
        return new JavalinWsContext(context);
    }

    @Override
    public int getStatusCode() {
        return context.status();
    }

    @Override
    public String getReason() {
        return context.reason();
    }
}
