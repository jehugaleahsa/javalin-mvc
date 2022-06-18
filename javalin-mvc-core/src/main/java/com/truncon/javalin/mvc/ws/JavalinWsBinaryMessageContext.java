package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsBinaryMessageContext;
import io.javalin.plugin.json.JsonMapper;

public final class JavalinWsBinaryMessageContext extends JavalinWsContext implements WsBinaryMessageContext {
    private final io.javalin.websocket.WsBinaryMessageContext context;

    public JavalinWsBinaryMessageContext(JsonMapper jsonMapper, io.javalin.websocket.WsBinaryMessageContext context) {
        super(jsonMapper, context);
        this.context = context;
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
