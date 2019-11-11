package io.javalin.mvc.ws;

import io.javalin.mvc.api.ws.WsConnectContext;
import io.javalin.mvc.api.ws.WsContext;

public final class JavalinWsConnectContext implements WsConnectContext {
    private final io.javalin.websocket.WsContext context;

    public JavalinWsConnectContext(io.javalin.websocket.WsContext context) {
        this.context = context;
    }

    @Override
    public WsContext getContext() {
        return new JavalinWsContext(context);
    }
}
