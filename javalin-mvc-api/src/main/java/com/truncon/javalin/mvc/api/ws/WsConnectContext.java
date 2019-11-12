package com.truncon.javalin.mvc.api.ws;

/**
 * Provides information about a WebSocket connection.
 */
public interface WsConnectContext {
    /**
     * The WebSocket context.
     * @return The context.
     */
    WsContext getContext();
}
