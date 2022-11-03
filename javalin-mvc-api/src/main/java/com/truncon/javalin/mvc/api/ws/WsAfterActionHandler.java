package com.truncon.javalin.mvc.api.ws;

/**
 * Defines the interface all handlers must implement to be called after a controller action completes.
 */
public interface WsAfterActionHandler {
    /**
     * Performs a task after a controller action completes. The handler will be called even if the
     * action results in an exception.
     * @param context Provides information about the results of the action.
     */
    void executeAfter(WsAfterActionContext context);
}
