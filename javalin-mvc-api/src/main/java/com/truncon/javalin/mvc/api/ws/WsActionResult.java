package com.truncon.javalin.mvc.api.ws;

/**
 * Holds details regarding the response to generate for the current request.
 */
public interface WsActionResult {

    /**
     * Sends the response.
     * @param context The request context.
     */
    void execute(WsContext context);
}
