package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsMessageContext;
import io.javalin.plugin.json.JsonMapper;

public final class JavalinWsMessageContext extends JavalinWsContext implements WsMessageContext {
    private final io.javalin.websocket.WsMessageContext context;

    public JavalinWsMessageContext(JsonMapper jsonMapper, io.javalin.websocket.WsMessageContext context) {
        super(jsonMapper, context);
        this.context = context;
    }

    @Override
    public String getMessage() {
        return context.message();
    }
}
