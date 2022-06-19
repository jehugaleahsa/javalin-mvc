package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsErrorContext;

public final class JavalinWsErrorContext extends JavalinWsContext implements WsErrorContext {
    private final io.javalin.websocket.WsErrorContext context;

    public JavalinWsErrorContext(io.javalin.websocket.WsErrorContext context) {
        super(context);
        this.context = context;
    }

    @Override
    public Throwable getError() {
        return context.error();
    }
}
