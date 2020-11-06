package com.truncon.javalin.mvc.api;

/**
 * Generates a plain text (text/plain) response.
 */
public final class ContentResult implements ActionResult {
    private final String content;
    private final int statusCode;

    /**
     * Instantiates a new instance of a ContentResult for the given content.
     * @param content The content to send as a response.
     */
    public ContentResult(String content) {
        this(content, 200);
    }

    /**
     * Instantiates a new instance of a ContentResult for the given content and status code.
     * @param content The content to send as a response.
     * @param statusCode The status code of the response.
     */
    public ContentResult(String content, int statusCode) {
        this.content = content;
        this.statusCode = statusCode;
    }

    /**
     * Gets the response content.
     * @return The response content.
     */
    public String getContent() {
        return content;
    }

    /**
     * Gets the response status code.
     * @return The status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the content as the response with the status code.
     * @param context The request context.
     */
    public void execute(HttpContext context) {
        HttpResponse response = context.getResponse();
        response.setStatusCode(statusCode);
        response.setTextBody(content);
    }

    /**
     * Sets the status code and returns the content to be sent asynchronously.
     * @param context The request context.
     * @return the content.
     */
    public Object executeAsync(HttpContext context) {
        HttpResponse response = context.getResponse();
        response.setStatusCode(statusCode);
        return content;
    }
}
