package io.javalin.mvc.api;

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
     * Sets the status code.
     * @param context The request context.
     */
    public void execute(HttpContext context) {
        context.getResponse().setStatusCode(statusCode);
    }

    /**
     * Sets the status code.
     * @param context The request context.
     * @return null
     */
    public Object executeAsync(HttpContext context) {
        execute(context);
        return null;
    }
}
