package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import io.javalin.plugin.json.JsonMapper;

public final class JavalinWsErrorContext extends JavalinWsContext implements WsErrorContext {
    private final io.javalin.websocket.WsErrorContext context;

    public JavalinWsErrorContext(JsonMapper jsonMapper, io.javalin.websocket.WsErrorContext context) {
        super(jsonMapper, context);
        this.context = context;
    }

    @Override
    public Throwable getError() {
        return context.error();
    }
}
