package com.truncon.javalin.mvc.api.ws;

/**
 * Provides information about a WebSocket error.
 */
public interface WsErrorContext {
    /**
     * The WebSocket context.
     * @return The context.
     */
    WsContext getContext();

    /**
     * Gets the error that occurred.
     * @return The error.
     */
    Throwable getError();
}
