package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import com.truncon.javalin.mvc.api.ws.WsResponse;
import io.javalin.plugin.json.JsonMapper;

import java.io.InputStream;
import java.util.Objects;

public abstract class JavalinWsContext implements WsContext {
    private final JsonMapper jsonMapper;
    private final io.javalin.websocket.WsContext context;

    public JavalinWsContext(JsonMapper jsonMapper, io.javalin.websocket.WsContext context) {
        Objects.requireNonNull(jsonMapper);
        Objects.requireNonNull(context);
        this.jsonMapper = jsonMapper;
        this.context = context;
    }

    @Override
    public String getSessionId() {
        return context.getSessionId();
    }

    @Override
    public WsRequest getRequest() {
        return new JavalinWsRequest(context);
    }

    @Override
    public WsResponse getResponse() {
        return new JavalinWsResponse(context);
    }

    @Override
    public String toJsonString(Object data) {
        return jsonMapper.toJsonString(data);
    }

    @Override
    public InputStream toJsonStream(Object data) {
        return jsonMapper.toJsonStream(data);
    }

    @Override
    public <T> T fromJsonString(String json, Class<T> dataClass) {
        return jsonMapper.fromJsonString(json, dataClass);
    }

    @Override
    public <T> T fromJsonStream(InputStream json, Class<T> dataClass) {
        return jsonMapper.fromJsonStream(json, dataClass);
    }

    @Override
    public Object getHandle() {
        return context;
    }
}
