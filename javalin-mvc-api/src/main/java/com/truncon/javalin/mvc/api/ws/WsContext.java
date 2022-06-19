package com.truncon.javalin.mvc.api.ws;

/**
 * Represents the WebSocket request being processed by the web application. It provides access to the
 * request information and allows for sending a response. Other utilities for handling requests
 * are provided, as well.
 */
public interface WsContext {
    /**
     * Gets the unique ID of the session.
     * @return The unique session ID.
     */
    String getSessionId();

    /**
     * Gets the {@link WsRequest} object for retrieving the request details.
     * @return the request object.
     */
    WsRequest getRequest();

    /**
     * Gets the {@link WsResponse} object for sending a response.
     * @return the response object.
     */
    WsResponse getResponse();

    /**
     * Gets access to the underlying implementation of the context.
     * @return An object.
     */
    Object getHandle();
}
