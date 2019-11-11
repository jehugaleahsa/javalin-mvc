package io.javalin.mvc.api.ws;

public interface WsErrorContext {
    WsContext getContext();
    Throwable getError();
}
