package com.truncon.javalin.mvc.api.ws;

/**
 * The interface a WebSockets controller must implement.
 */
public interface WsController {
    /**
     * A method to fire when a client connects.
     * @param context The connection context.
     */
    default void onConnect(WsConnectContext context) {}

    /**
     * A method to fire when a client disconnects.
     * @param context The disconnection context.
     */
    default void onDisconnect(WsDisconnectContext context) {}

    /**
     * A method to fire when a client reports an error.
     * @param context The error context.
     */
    default void onError(WsErrorContext context) {}

    /**
     * A method to fire when a client sends a message.
     * @param context The message context.
     */
    default void onMessage(WsMessageContext context) {}

    /**
     * A method to fire when a client sends a binary message.
     * @param context The binary message context.
     */
    default void onBinaryMessage(WsBinaryMessageContext context) {}
}
