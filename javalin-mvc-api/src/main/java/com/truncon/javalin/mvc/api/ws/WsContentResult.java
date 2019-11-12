package com.truncon.javalin.mvc.api.ws;

/**
 * Generates a JSON response.
 */
public final class WsContentResult implements WsActionResult {
    private final String content;

    /**
     * Initializes a new instance of a WsJsonResult.
     * @param content The object to serialize as JSON.
     */
    public WsContentResult(String content) {
        this.content = content;
    }

    /**
     * Sets the content as the response.
     * @param context The context.
     */
    public void execute(WsContext context) {
        WsResponse response = context.getResponse();
        response.sendText(content);
    }
}
