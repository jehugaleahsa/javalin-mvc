package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsBinaryMessageContext;
import com.truncon.javalin.mvc.api.ws.WsContext;

public final class JavalinWsBinaryMessageContext implements WsBinaryMessageContext {
    private final io.javalin.websocket.WsBinaryMessageContext context;

    public JavalinWsBinaryMessageContext(io.javalin.websocket.WsBinaryMessageContext context) {
        this.context = context;
    }

    @Override
    public WsContext getContext() {
        return new JavalinWsContext(context);
    }

    @Override
    public byte[] getData() {
        return context.data();
    }

    @Override
    public int getOffset() {
        return context.offset();
    }

    @Override
    public int getLength() {
        return context.length();
    }
}
