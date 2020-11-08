package com.truncon.javalin.mvc.api.ws;

import java.util.Collection;
import java.util.Map;

/**
 * Provides details about the current WebSocket request.
 */
public interface WsRequest {
    /**
     * Specifies whether a parameter with the given name exists in the URL.
     * @param name The name of the parameter to search for.
     * @return true if the parameter is found; otherwise, false.
     */
    boolean hasPathParameter(String name);

    /**
     * Gets the value of the parameter in the URL.
     * @param name The name of the parameter to search for.
     * @return the value of the parameter or null if it does not exist.
     */
    String getPathParameter(String name);

    /**
     * Gets the key/value pairs for any parameters in the URL.
     * @return the key/value pair lookup of the parameters.
     */
    Map<String, Collection<String>> getPathLookup();

    /**
     * Specifies whether a query parameter with the given name exists in the URL.
     * @param name The name of the parameter to search for.
     * @return true if the parameter is found; otherwise, false.
     */
    boolean hasQueryParameter(String name);

    /**
     * Gets the value of the parameter in the query string.
     * @param name The name of the parameter to search for.
     * @return the value of the parameter or null if it does not exist.
     */
    String getQueryParameter(String name);

    /**
     * Gets the key/value pairs for any query string parameters in the URL.
     * @return the key/value pair lookup of the parameters.
     */
    Map<String, Collection<String>> getQueryLookup();

    /**
     * Specifies whether a header with the given name exists.
     * @param name The name of the header to search for.
     * @return true if the parameter is found; otherwise, false.
     */
    boolean hasHeader(String name);

    /**
     * Gets the value of the header.
     * @param name The name of the header to search for.
     * @return the value of the header or null of it does not exist.
     */
    String getHeaderValue(String name);

    /**
     * Gets the key/value pairs for any headers.
     * @return the key/value pair lookup of the headers.
     */
    Map<String, Collection<String>> getHeaderLookup();

    /**
     * Indicates whether a cookie with the given name exists.
     * @param name The name of the cookie to search for.
     * @return true if the cookie is found; otherwise false.
     */
    boolean hasCookie(String name);

    /**
     * Gets the value of the cookie with the given name.
     * @param name The name of the cookie.
     * @return the value of the cookie, or null if it does not exist.
     */
    String getCookieValue(String name);

    /**
     * Gets the key/value pairs for the cookies.
     * @return the key/value pair lookup.
     */
    Map<String, Collection<String>> getCookieLookup();
}
