package com.truncon.javalin.mvc.api.ws;

/**
 * Defines the interface all handlers must implement to be called before a controller action executes.
 */
public interface WsBeforeActionHandler {
    /**
     * Performs a task before a controller action executes. The request can be cancelled by returning false.
     * @param context Provides information about the request to be performed.
     */
    void executeBefore(WsBeforeActionContext context);
}
