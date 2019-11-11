package io.javalin.mvc.ws;

import io.javalin.mvc.api.ws.WsContext;
import io.javalin.mvc.api.ws.WsMessageContext;

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
