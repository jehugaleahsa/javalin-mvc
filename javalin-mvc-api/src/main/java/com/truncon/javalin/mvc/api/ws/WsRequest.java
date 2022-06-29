package com.truncon.javalin.mvc.api.ws;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
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
    Map<String, List<String>> getPathLookup();

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
     * Gets the values of the parameter in the query string.
     * @param name The name of the parameter to search for.
     * @return The values of the parameter or an empty list if it does not exist.
     */
    List<String> getQueryParameters(String name);

    /**
     * Gets the key/value pairs for any query string parameters in the URL.
     * @return the key/value pair lookup of the parameters.
     */
    Map<String, List<String>> getQueryLookup();

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
    Map<String, List<String>> getHeaderLookup();

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
    Map<String, List<String>> getCookieLookup();

    /**
     * Gets the lookup associated with the specified value source.
     *
     * @param valueSource The source of the lookup values to retrieve.
     * @return The lookup associated with the given source.
     */
    default Map<String, List<String>> getSourceLookup(WsValueSource valueSource) {
        if (valueSource == null) {
            return Collections.emptyMap();
        }
        switch (valueSource) {
            case Path:
                return getPathLookup();
            case QueryString:
                return getQueryLookup();
            case Header:
                return getHeaderLookup();
            case Cookie:
                return getCookieLookup();
            case Any: {
                // Ordered for security reasons
                Map<String, List<String>> lookup = new LinkedHashMap<>();
                lookup.putAll(getQueryLookup());
                lookup.putAll(getPathLookup());
                lookup.putAll(getCookieLookup());
                lookup.putAll(getHeaderLookup());
                return lookup;
            }
            default:
                return Collections.emptyMap();
        }
    }
}
