package com.truncon.javalin.mvc.api;

/**
 * Generates a redirect response.
 */
public final class RedirectResult implements ActionResult {
    private final String location;
    private final boolean isPermanent;

    /**
     * Initializes a new instance of a RedirectResult.
     * @param location The URL to redirect the user to.
     */
    public RedirectResult(String location) {
        this(location, false);
    }

    /**
     * Initializes a new instance of a RedirectResult, indicating whether it is a permanent (or temporary) redirect.
     * @param location The URL to redirect the user to.
     * @param isPermanent Indicates whether the status code should be for permanent or temporary redirect.
     */
    public  RedirectResult(String location, boolean isPermanent) {
        this.location = location;
        this.isPermanent = isPermanent;
    }

    /**
     * Sets the redirect response.
     * @param context The request context.
     */
    public void execute(HttpContext context) {
        context.getResponse().redirect(location, isPermanent ? 301 : 302);
    }

    /**
     * Sets the redirect response.
     * @param context The request context.
     * @return null
     */
    public Object executeAsync(HttpContext context) {
        execute(context);
        return null;
    }
}
