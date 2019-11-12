package com.truncon.javalin.mvc.api.ws;

/**
 * Provides information about a WebSocket disconnection.
 */
public interface WsDisconnectContext extends WsContext {
    /**
     * Gets the status code on close.
     * @return The status code.
     */
    int getStatusCode();

    /**
     * The reason the client disconnected.
     * @return The reason.
     */
    String getReason();
}
