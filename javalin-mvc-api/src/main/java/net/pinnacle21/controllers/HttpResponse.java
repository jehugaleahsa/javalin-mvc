package io.javalin.mvc.api;

import java.io.InputStream;

/**
 * Provides functionality for sending a response.
 */
public interface HttpResponse {
    /**
     * Sets the status code of the response.
     * @param statusCode The status code to use.
     */
    HttpResponse setStatusCode(int statusCode);

    /**
     * Sets the content type of the response.
     * @param contentType The content type to use.
     */
    HttpResponse setContentType(String contentType);

    /**
     * Sets a header in the response.
     * @param name The name of the header being set.
     * @param value The value to set the header to.
     */
    HttpResponse setHeader(String name, String value);

    /**
     * Sets the body of the response to a String, with a MIME type of text/plain.
     * @param content The content to respond with.
     */
    HttpResponse setTextBody(String content);

    /**
     * Sets the body of the response as an HTML document, with a MIME type of text/html.
     * @param content The HTML document to respond with.
     */
    HttpResponse setHtmlBody(String content);

    /**
     * Sets the body of the response as a JSON document, with a MIME type of application/JSON.
     * @param data The object to serialize as JSON.
     */
    HttpResponse setJsonBody(Object data);

    /**
     * Sets the body of the response as the contents of the given stream. The MIME type must be set separately.
     * @param stream The stream to use as the body of the response.
     */
    HttpResponse setStreamBody(InputStream stream);

    /**
     * Sets response headers indicating that the user should go to a different URL.
     * The default status code (TEMPORARY) will be used.
     *
     * NOTE: This method should not be used in combination with other methods that set the body.
     *
     * @param location The URL to redirect the user to.
     */
    HttpResponse redirect(String location);

    /**
     * Sets response headers indicating that the user should go to a different URL.
     * The given status code will be used.
     *
     * NOTE: This method should not be used in combination with other methods that set the body.
     *
     * @param location The URL to redirect the user to.
     * @param statusCode The status code to be used.
     */
    HttpResponse redirect(String location, int statusCode);

    /**
     * Sets a cookie in the response.
     * @param name The name of the cookie.
     * @param value The value of the cookie.
     */
    HttpResponse setCookie(String name, String value);

    /**
     * Sets a cookie in the response.
     * @param name The name of the cookie.
     * @param value The value of the cookie.
     * @param maxAge Specifies how long the cookie should remain valid.
     */
    HttpResponse setCookie(String name, String value, int maxAge);

    /**
     * Removes the cookie from the response.
     * @param name The name of the cookie.
     */
    HttpResponse removeCookie(String name);

    /**
     * Removes the cookie from the response, scoped to the given path.
     * @param name The name of the cookie.
     * @param path The path under which to remove the cookie.
     */
    HttpResponse removeCookie(String name, String path);
}
