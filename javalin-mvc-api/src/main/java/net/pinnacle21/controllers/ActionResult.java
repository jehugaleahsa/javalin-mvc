package io.javalin.mvc.api;

/**
 * Holds details regarding the response to generate for the current request.
 */
public interface ActionResult {

    /**
     * Synchronously send the response.
     * @param context The request context.
     */
    void execute(HttpContext context);

    /**
     * Generate the response to be sent asynchronously. Headers will be set synchronously.
     * @param context The request context.
     * @return the content to send asynchronously.
     */
    Object executeAsync(HttpContext context);
}
