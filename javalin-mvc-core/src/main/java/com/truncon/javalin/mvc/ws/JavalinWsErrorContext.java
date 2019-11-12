package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;

public final class JavalinWsErrorContext implements WsErrorContext {
    private final io.javalin.websocket.WsErrorContext context;

    public JavalinWsErrorContext(io.javalin.websocket.WsErrorContext context) {
        this.context = context;
    }

    @Override
    public WsContext getContext() {
        return new JavalinWsContext(context);
    }

    @Override
    public Throwable getError() {
        return context.error();
    }
}
