package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsBinaryMessageContext;

public final class JavalinWsBinaryMessageContext extends JavalinWsContext implements WsBinaryMessageContext {
    private final io.javalin.websocket.WsBinaryMessageContext context;

    public JavalinWsBinaryMessageContext(io.javalin.websocket.WsBinaryMessageContext context) {
        super(context);
        this.context = context;
    }

    @Override
    public byte[] getData() {
        return context.data();
    }

    @Override
    public Integer getOffset() {
        return context.offset();
    }

    @Override
    public Integer getLength() {
        return context.length();
    }
}
