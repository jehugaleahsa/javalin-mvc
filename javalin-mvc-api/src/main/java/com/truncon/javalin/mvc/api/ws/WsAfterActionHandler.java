package com.truncon.javalin.mvc.api.ws;

/**
 * Defines the interface all handlers must implement to be called after a controller action completes.
 */
public interface WsAfterActionHandler {
    /**
     * Performs a task after a controller action completes. The handler will be called even if the
     * action results in an exception, in which case the exception will be passed as the third argument.
     * If the method returns an exception, it will be passed along to the next handler; otherwise, null
     * can be returned to indicate that the exception has been "handled". If the end of the handler chain
     * is reached and the exception is still unhandled, it will be rethrown.
     * @param context The request context being processed.
     * @param arguments The String constant arguments defined on the annotation.
     * @param exception The exception that was thrown by the action; otherwise, null.
     * @return the exception to be processed by the next handler.
     */
    Exception executeAfter(WsContext context, String[] arguments, Exception exception);
}
