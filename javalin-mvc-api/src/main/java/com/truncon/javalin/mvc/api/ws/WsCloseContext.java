package com.truncon.javalin.mvc.api.ws;

/**
 * Provides information about a WebSocket disconnect.
 */
public interface WsCloseContext extends WsContext {
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
