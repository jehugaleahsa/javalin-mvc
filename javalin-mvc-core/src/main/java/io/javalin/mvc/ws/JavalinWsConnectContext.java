package io.javalin.mvc.ws;

import io.javalin.mvc.api.ws.WsConnectContext;
import io.javalin.mvc.api.ws.WsContext;

public final class JavalinWsConnectContext implements WsConnectContext {
    private final WsContext context;

    public JavalinWsConnectContext(WsContext context) {
        this.context = context;
    }

    @Override
    public WsContext getContext() {
        return context;
    }
}
