package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsCloseContext;

public final class JavalinWsCloseContext extends JavalinWsContext implements WsCloseContext {
    private final io.javalin.websocket.WsCloseContext context;

    public JavalinWsCloseContext(io.javalin.websocket.WsCloseContext context) {
        super(context);
        this.context = context;
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
