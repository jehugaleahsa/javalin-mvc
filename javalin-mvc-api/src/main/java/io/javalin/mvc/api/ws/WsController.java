package io.javalin.mvc.api.ws;

/**
 * The interface a WebSockets controller must implement.
 */
public interface WsController {
    default void onConnect(WsConnectContext context) {}
    default void onDisconnect(WsDisconnectContext context) {}
    default void onError(WsErrorContext context) {}
    default void onMessage(WsMessageContext context) {}
    default void onBinaryMessage(WsBinaryMessageContext context) {}
}
