package com.truncon.javalin.mvc.api;

/**
 * Represents the HTTP request being processed by the web application. It provide access to the
 * request information and allows for sending a response. Other utilities for handling requests
 * are provided, as well.
 */
public interface HttpContext {
    /**
     * Gets the {@link HttpRequest} object for retrieving the request details.
     * @return the request object.
     */
    HttpRequest getRequest();

    /**
     * Gets the {@link HttpResponse} object for sending a response.
     * @return the response object.
     */
    HttpResponse getResponse();

    /**
     * Gets access to the underlying implementation of the context.
     * @return An object.
     */
    Object getHandle();
}
