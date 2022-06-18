package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsDisconnectContext;
import io.javalin.plugin.json.JsonMapper;

public final class JavalinWsDisconnectContext extends JavalinWsContext implements WsDisconnectContext {
    private final io.javalin.websocket.WsCloseContext context;

    public JavalinWsDisconnectContext(JsonMapper jsonMapper, io.javalin.websocket.WsCloseContext context) {
        super(jsonMapper, context);
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
