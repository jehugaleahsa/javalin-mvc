package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsConnectContext;

public final class JavalinWsConnectContext extends JavalinWsContext implements WsConnectContext {
    public JavalinWsConnectContext(io.javalin.websocket.WsContext context) {
        super(context);
    }
}
