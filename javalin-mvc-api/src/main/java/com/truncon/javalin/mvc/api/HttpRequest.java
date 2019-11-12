package com.truncon.javalin.mvc.api;

import java.util.List;
import java.util.Map;

/**
 * Provides details about the current request.
 */
public interface HttpRequest {
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
     * Gets the key/value pairs for any query string parameters in the URL.
     * @return the key/value pair lookup of the parameters.
     */
    Map<String, List<String>> getQueryLookup();

    /**
     * Specifies whether a form field (URL encoded) with the given name exists in the URL.
     * @param name The name of the parameter to search for.
     * @return true if the parameter is found; otherwise, false.
     */
    boolean hasFormParameter(String name);

    /**
     * Gets the value of the form field (URL encoded).
     * @param name The name of the form field to search for.
     * @return the value of the form field or null if it does not exist.
     */
    String getFormParameter(String name);

    /**
     * Gets the key/value pairs for any form fields.
     * @return the key/value pair lookup of the form fields.
     */
    Map<String, List<String>> getFormLookup();

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
    String getHeader(String name);

    /**
     * Gets the key/value pairs for any headers.
     * @return the key/value pair lookup of the headers.
     */
    Map<String, List<String>> getHeaderLookup();

    /**
     * Gets the length (in bytes) of the request body.
     * @return the length of the request body.
     */
    int getContentLength();

    /**
     * Gets the content type of the request.
     * @return the content type.
     */
    String getContentType();

    /**
     * Gets the body of the request as a byte array.
     * @return the body as a byte array.
     */
    byte[] getBodyAsBytes();

    /**
     * Gets the body of the request as a String.
     * @return the request body.
     */
    String getBodyAsText();

    /**
     * Gets the request body as a deserialized JSON document.
     * @param bodyCls The {@link Class} of the object to deserialize.
     * @param <T> The type of the object to deserialize.
     * @return the deserialized JSON document.
     */
    <T> T getBodyFromJson(Class<T> bodyCls);

    /**
     * Gets the full URL of the request.
     * @return the full URL.
     */
    String getUrl();

    /**
     * Gets the request protocol.
     * @return the request protocol.
     */
    String getHttpVersion();

    /**
     * Gets the request method.
     * @return the request method.
     */
    String getMethod();

    /**
     * Gets the user agent of the requester.
     * @return the user agent.
     */
    String getUserAgent();

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
     * Gets whether this is a multi-part form data request.
     * @return true if the request is multipart; otherwise, false.
     */
    boolean isMultipart();

    /**
     * Gets a file from the request body for multi-part requests.
     * @param name The name of the file to retrieve.
     * @return the file.
     */
    FileUpload getFile(String name);
}
