package io.javalin.mvc.api.ws;

import java.nio.ByteBuffer;

public interface WsResponse {
    void sendText(String content);
    void sendJson(Object data);
    void sendBinary(ByteBuffer buffer);
}
