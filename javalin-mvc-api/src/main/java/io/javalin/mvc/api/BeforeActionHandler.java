package io.javalin.mvc.api;

/**
 * Defines the interface all handlers must implement to be called before a controller action executes.
 */
public interface BeforeActionHandler {
    /**
     * Performs a task before a controller action executes. The request can be cancelled by returning false.
     * @param context The request context being processed.
     * @param arguments The String constant arguments defined on the annotation.
     * @return false if the request should be cancelled; otherwise, true.
     */
    boolean executeBefore(HttpContext context, String[] arguments);
}
