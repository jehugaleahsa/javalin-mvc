package com.truncon.javalin.mvc.api.ws;

import java.util.List;

/**
 * Provides information about a pending request and allows for cancellation.
 */
public interface WsBeforeActionContext {
    /**
     * Gets the request context being processed.
     * @return The request context.
     */
    WsContext getWsContext();

    /**
     * Gets the constant arguments defined on the {@link WsBefore} annotation
     * using {@link WsBefore#arguments()}.
     * @return The constant arguments.
     */
    List<String> getArguments();

    /**
     * Gets whether the request was cancelled by the current or
     * a previous handler.
     * @return True if the request was cancelled; otherwise, false.
     */
    boolean isCancelled();

    /**
     * Sets whether the request is cancelled. This will prevent
     * subsequent before handlers from running.
     * @param cancelled Whether the request should be cancelled.
     */
    void setCancelled(boolean cancelled);
}
