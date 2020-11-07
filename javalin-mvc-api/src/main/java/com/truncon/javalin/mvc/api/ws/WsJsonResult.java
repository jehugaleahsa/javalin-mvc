package com.truncon.javalin.mvc.api.ws;

/**
 * Generates a JSON response.
 */
public final class WsJsonResult implements WsActionResult {
    private final Object data;

    /**
     * Initializes a new instance of a WsJsonResult.
     * @param data The object to serialize as JSON.
     */
    public WsJsonResult(Object data) {
        this.data = data;
    }

    /**
     * Gets the data to be serialized as JSON.
     * @return The data to be serialized as JSON.
     */
    public Object getData() {
        return data;
    }

    /**
     * Sets the JSON serialized object as the response.
     * @param context The context.
     */
    public void execute(WsContext context) {
        WsResponse response = context.getResponse();
        response.sendJson(data);
    }
}
