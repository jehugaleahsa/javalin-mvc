package com.truncon.javalin.mvc.api;

/**
 * Generates a redirect response.
 */
public final class RedirectResult implements ActionResult {
    private final String location;
    private boolean permanent;
    private boolean preserveMethod;

    /**
     * Initializes a new instance of a RedirectResult.
     * @param location The URL to redirect the user to.
     */
    public RedirectResult(String location) {
        this(location, false, false);
    }

    /**
     * Initializes a new instance of a RedirectResult, indicating whether it is a permanent (or temporary) redirect.
     * @param location The URL to redirect the user to.
     * @param permanent Indicates whether the status code should be for permanent or temporary redirect.
     */
    public  RedirectResult(String location, boolean permanent) {
        this(location, permanent, false);
    }

    /**
     * Initializes a new instance of a RedirectResult, indicating whether it is a permanent (or temporary) redirect,
     * and whether it should try to preserve the original method.
     * @param location The URL to redirect the user to.
     * @param permanent Indicates whether the status code should be for permanent or temporary redirect.
     * @param preserveMethod Indicates whether the same request method should be used for the new location.
     */
    public  RedirectResult(String location, boolean permanent, boolean preserveMethod) {
        this.location = location;
        this.permanent = permanent;
        this.preserveMethod = preserveMethod;
    }

    /**
     * Gets the URL that the user is being redirected to.
     * @return The URL that the user is being redirected to.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Gets whether the redirect should be indicated as permanent.
     * @return True if the redirect should be treated as permanent.
     */
    public boolean isPermanent() {
        return this.permanent;
    }

    /**
     * Sets whether the redirect should be indicated as permanent.
     * @param value True to indicate that redirect is permanent; otherwise, false.
     */
    public void setPermanent(boolean value) {
        this.permanent = value;
    }

    /**
     * Gets whether the same request method should be used for the new location.
     * @return Whether the same request method should be used for the new location.
     */
    public boolean isMethodPreserved() {
        return preserveMethod;
    }

    /**
     * Sets whether the same request method should be used for the new location.
     * @param value True to indicate the same request method should be used; otherwise, false.
     */
    public void setMethodPreserved(boolean value) {
        this.preserveMethod = value;
    }

    /**
     * Sets the redirect response.
     * @param context The request context.
     */
    public void execute(HttpContext context) {
        int statusCode = preserveMethod
            ? (permanent ? 308 : 307) // Permanent Direct vs Temporary Redirect
            : (permanent ? 301 : 302); // Moved Permanently vs Found (Previously Moved Temporarily)
        context.getResponse().redirect(location, statusCode);
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
