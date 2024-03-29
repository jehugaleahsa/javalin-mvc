package com.truncon.javalin.mvc.api;

/**
 * Generates an empty response with the specified status code.
 */
public final class StatusCodeResult implements ActionResult {
    private final int statusCode;

    /**
     * Initializes a new instance of a StatusCodeResult.
     * @param statusCode The status code of the response.
     */
    public StatusCodeResult(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the status code of the response.
     * @return The status code.
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the status code.
     * @param context The request context.
     */
    @Override
    public void execute(HttpContext context) {
        context.getResponse().setStatusCode(statusCode);
    }
}
