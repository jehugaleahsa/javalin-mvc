package com.truncon.javalin.mvc.api.ws;

import java.nio.ByteBuffer;

public final class MockWsResponse implements WsResponse {
    private String text;
    private Object jsonData;
    private ByteBuffer binary;

    public String getText() {
        return text;
    }

    @Override
    public void sendText(String content) {
        this.text = content;
    }

    public Object getJsonData() {
        return this.jsonData;
    }

    @Override
    public void sendJson(Object data) {
        this.jsonData = data;
    }

    public ByteBuffer getBinary() {
        return this.binary;
    }

    @Override
    public void sendBinary(ByteBuffer buffer) {
        this.binary = buffer;
    }
}
