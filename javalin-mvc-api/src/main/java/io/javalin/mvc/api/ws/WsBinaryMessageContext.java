package io.javalin.mvc.api.ws;

public interface WsBinaryMessageContext {
    WsContext getContext();
    byte[] getData();
    int getOffset();
    int getLength();
}
