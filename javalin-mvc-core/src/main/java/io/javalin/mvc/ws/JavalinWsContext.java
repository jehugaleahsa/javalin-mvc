package io.javalin.mvc.ws;

import io.javalin.mvc.api.ws.WsContext;
import io.javalin.mvc.api.ws.WsRequest;
import io.javalin.mvc.api.ws.WsResponse;
import io.javalin.plugin.json.JavalinJackson;

public final class JavalinWsContext implements WsContext {
    private final io.javalin.websocket.WsContext context;

    public JavalinWsContext(io.javalin.websocket.WsContext context) {
        this.context = context;
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
    public String toJson(Object data) {
        return JavalinJackson.INSTANCE.toJson(data);
    }

    @Override
    public <T> T fromJson(String json, Class<T> dataClass) {
        return JavalinJackson.INSTANCE.fromJson(json, dataClass);
    }

    @Override
    public Object getHandle() {
        return context;
    }
}
