package com.truncon.javalin.mvc.api;

import java.util.List;

/**
 * Provides information about a pending request and allows for cancellation.
 */
public interface BeforeActionContext {
    /**
     * Gets the request context being processed.
     * @return The request context.
     */
    HttpContext getHttpContext();

    /**
     * Gets the constant arguments defined on the annotation.
     * @return The constant arguments.
     */
    List<String> getArguments();

    /**
     * Gets whether the request was cancelled by a previous handler.
     * @return True if the request was cancelled; otherwise, false.
     */
    boolean isCancelled();

    /**
     * Sets the request as cancelled, preventing the request from
     * being processed by a controlled method.
     * @param cancelled Whether the request should be cancelled.
     */
    void setCancelled(boolean cancelled);
}
