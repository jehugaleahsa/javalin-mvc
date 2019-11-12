package com.truncon.javalin.mvc.api.ws;

/**
 * Provides information about a WebSocket error.
 */
public interface WsErrorContext extends WsContext {
    /**
     * Gets the error that occurred.
     * @return The error.
     */
    Throwable getError();
}
