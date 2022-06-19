package com.truncon.javalin.mvc.api;

/**
 * Generates a JSON (application/json) response.
 */
public final class JsonResult implements ActionResult {
    private final Object data;
    private final int statusCode;
    private final boolean stream;

    /**
     * Initializes a new instance of a JsonResult.
     * @param data The object to serialize as JSON.
     */
    public JsonResult(Object data) {
        this(data, 200, false);
    }

    /**
     * Initializes a new instance of a JsonResult.
     * @param data The object to serialize as JSON.
     */
    public JsonResult(Object data, boolean stream) {
        this(data, 200, stream);
    }

    /**
     * Initializes a new instance of a JsonResult.
     * @param data The object to serialize as JSON.
     * @param statusCode The status code of the response.
     */
    public JsonResult(Object data, int statusCode) {
        this(data, statusCode, false);
    }

    /**
     * Initializes a new instance of a JsonResult.
     * @param data The object to serialize as JSON.
     * @param statusCode The status code of the response.
     * @param stream Whether the JSON should be streamed.
     */
    public JsonResult(Object data, int statusCode, boolean stream) {
        this.data = data;
        this.statusCode = statusCode;
        this.stream = stream;
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
     * Gets whether the JSON will be sent back in a stream.
     * @return Whether the JSON will be sent back in a stream.
     */
    public boolean isStreaming() {
        return stream;
    }

    /**
     * Sets the JSON serialized object as the response with the status code.
     * @param context The request context.
     */
    @Override
    public void execute(HttpContext context) {
        HttpResponse response = context.getResponse();
        response.setStatusCode(statusCode);
        if (stream) {
            response.setJsonStreamBody(data);
        } else {
            response.setJsonBody(data);
        }
    }
}
