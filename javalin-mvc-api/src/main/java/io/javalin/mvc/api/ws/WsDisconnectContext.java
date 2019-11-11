package io.javalin.mvc.api.ws;

public interface WsDisconnectContext {
    WsContext getContext();
    int getStatusCode();
    String getReason();
}
