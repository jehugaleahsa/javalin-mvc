package com.truncon.javalin.mvc.api.ws;

/**
 * Generates a binary response.
 */
public final class WsByteArrayResult implements WsActionResult {
    private final byte[] data;
    private final Integer offset;
    private final Integer length;

    /**
     * Initializes a new instance of a WsByteArrayResult.
     * @param data The data to send.
     */
    public WsByteArrayResult(byte[] data) {
        this.data = data;
        this.offset = null;
        this.length = null;
    }

    /**
     * Initializes a new instance of a WsByteArrayResult.
     *
     * @param data The data to send.
     */
    public WsByteArrayResult(byte[] data, int offset, int length) {
        this.data = data;
        this.offset = offset;
        this.length = length;
    }

    /**
     * Sets the content as the response.
     * @param context The context.
     */
    public void execute(WsContext context) {
        WsResponse response = context.getResponse();
        if (offset == null) {
            response.sendBinary(data);
        } else {
            response.sendBinary(data, offset, length);
        }
    }
}
