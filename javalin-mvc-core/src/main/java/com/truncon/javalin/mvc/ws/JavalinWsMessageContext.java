package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsMessageContext;

public final class JavalinWsMessageContext extends JavalinWsContext implements WsMessageContext {
    private final io.javalin.websocket.WsMessageContext context;

    public JavalinWsMessageContext(io.javalin.websocket.WsMessageContext context) {
        super(context);
        this.context = context;
    }

    @Override
    public String getMessage() {
        return context.message();
    }

    @Override
    public <T> T getMessage(Class<T> dataClass) {
        return context.messageAsClass(dataClass);
    }
}
