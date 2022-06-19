package com.truncon.javalin.mvc.api;

/**
 * Holds details regarding the response to generate for the current request.
 */
public interface ActionResult {

    /**
     * Synchronously send the response.
     * @param context The request context.
     */
    void execute(HttpContext context);
}
