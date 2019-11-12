package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;

public final class JavalinWsMessageContext implements WsMessageContext {
    private final io.javalin.websocket.WsMessageContext context;

    public JavalinWsMessageContext(io.javalin.websocket.WsMessageContext context) {
        this.context = context;
    }

    @Override
    public WsContext getContext() {
        return new JavalinWsContext(context);
    }

    @Override
    public String getMessage() {
        return context.message();
    }
}
