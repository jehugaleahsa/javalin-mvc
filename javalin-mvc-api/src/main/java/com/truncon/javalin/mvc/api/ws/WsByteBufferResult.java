package com.truncon.javalin.mvc.api.ws;

import java.nio.ByteBuffer;

/**
 * Generates a binary response.
 */
public final class WsByteBufferResult implements WsActionResult {
    private final ByteBuffer data;

    /**
     * Initializes a new instance of a WsByteArrayResult.
     * @param buffer The data to send.
     */
    public WsByteBufferResult(ByteBuffer buffer) {
        this.data = buffer;
    }

    /**
     * Sets the content as the response.
     * @param context The context.
     */
    public void execute(WsContext context) {
        WsResponse response = context.getResponse();
        response.sendBinary(data);
    }
}
