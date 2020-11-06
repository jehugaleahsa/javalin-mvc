package com.truncon.javalin.mvc.api;

/**
 * Generates a JSON (application/json) response.
 */
public final class JsonResult implements ActionResult {
    private final Object data;
    private final int statusCode;

    /**
     * Initializes a new instance of a JsonResult.
     * @param data The object to serialize as JSON.
     */
    public JsonResult(Object data) {
        this(data, 200);
    }

    /**
     * Initializes a new instance of a JsonResult.
     * @param data The object to serialize as JSON.
     * @param statusCode The status code of the response.
     */
    public JsonResult(Object data, int statusCode) {
        this.data = data;
        this.statusCode = statusCode;
    }

    /**
     * Gets the object being serializes as JSON.
     * @return The object being serialized as JSON.
     */
    public Object getData() {
        return data;
    }

    /**
     * Gets the response status code.
     * @return The response status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the JSON serialized object as the response with the status code.
     * @param context The request context.
     */
    public void execute(HttpContext context) {
        HttpResponse response = context.getResponse();
        response.setStatusCode(statusCode);
        response.setJsonBody(data);
    }

    /**
     * Sets the status code and returns the JSON serialized object to be sent asynchronously.
     * @param context The request context.
     * @return the JSON document.
     */
    public Object executeAsync(HttpContext context) {
        HttpResponse response = context.getResponse();
        response.setStatusCode(statusCode);
        return context.toJson(data);
    }
}
