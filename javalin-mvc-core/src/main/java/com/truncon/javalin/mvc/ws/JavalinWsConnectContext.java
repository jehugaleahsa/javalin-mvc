package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import io.javalin.plugin.json.JsonMapper;

public final class JavalinWsConnectContext extends JavalinWsContext implements WsConnectContext {
    public JavalinWsConnectContext(JsonMapper jsonMapper, io.javalin.websocket.WsContext context) {
        super(jsonMapper, context);
    }
}
