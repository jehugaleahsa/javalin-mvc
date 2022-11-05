package com.truncon.javalin.mvc.api;

import java.util.List;

/**
 * Provides the results of processing an action.
 */
public interface AfterActionContext {
    /**
     * Gets the request context being processed.
     * @return The request context.
     */
    HttpContext getHttpContext();

    /**
     * Gets the constant arguments defined on the {@link After} annotation
     * using {@link After#arguments()}.
     * @return The constant arguments.
     */
    List<String> getArguments();

    /**
     * Gets the exception that was thrown by the action, or replaced by the
     * current or previous handler; otherwise, null.
     * @return The exception, or null.
     */
    Exception getException();

    /**
     * Sets the exception, which will be passed to the next handler, or
     * thrown if no more handlers are present and the error is not marked as handled.
     * @param exception The exception to send to the next handler or be thrown.
     */
    void setException(Exception exception);

    /**
     * Gets whether the exception was handled by the current or previous handler. If
     * no exception is {@code null}, {@code true} will be returned.
     * @return Whether the exception has been handled.
     */
    boolean isHandled();

    /**
     * Sets the exception as handled. If no exception is set, this operation is ignored.
     * @param handled Whether the exception was handled, if applicable.
     * @apiNote The exception remains accessible to future handlers unless
     * explicitly set to {@code null}.
     */
    void setHandled(boolean handled);
}
