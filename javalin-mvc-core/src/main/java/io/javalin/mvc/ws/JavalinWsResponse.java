package io.javalin.mvc.ws;

import io.javalin.mvc.api.ws.WsResponse;
import io.javalin.websocket.WsContext;

import java.nio.ByteBuffer;

final class JavalinWsResponse implements WsResponse {
    private final WsContext context;

    public JavalinWsResponse(WsContext context) {
        this.context = context;
    }

    public WsContext getContext() {
        return context;
    }

    @Override
    public void sendText(String content) {
        context.send(content);
    }

    @Override
    public void sendJson(Object data) {
        context.send(data);
    }

    @Override
    public void sendBinary(ByteBuffer buffer) {
        context.send(buffer);
    }
}
