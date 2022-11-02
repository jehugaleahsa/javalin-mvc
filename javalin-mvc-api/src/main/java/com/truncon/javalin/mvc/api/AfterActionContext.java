package com.truncon.javalin.mvc.api;

import java.util.List;

/**
 * Provides information about the result of processing an action.
 */
public interface AfterActionContext {
    /**
     * Gets the request context being processed.
     * @return The request context.
     */
    HttpContext getHttpContext();

    /**
     * Gets the constant arguments defined on the {@link After} annotation.
     * @return The constant arguments.
     */
    List<String> getArguments();

    /**
     * Gets the exception that was thrown by the action; otherwise, null.
     * @return The thrown exception or null.
     */
    Exception getException();

    /**
     * Gets whether the exception was handled by a previous handler. If
     * no exception was thrown, {@code true} will be returned.
     * @return Whether the exception has been handled.
     */
    boolean isHandled();

    /**
     * Sets the exception as handled. This is ignored if no exception was thrown or
     * the exception was handled by a previous handler.
     */
    void setHandled(boolean handled);
}
