package com.truncon.javalin.mvc.api.ws;

import java.nio.ByteBuffer;
import java.util.Objects;

/**
 * Generates a binary response.
 */
public final class WsBinaryResult implements WsActionResult {
    private final ByteBuffer data;

    /**
     * Initializes a new instance of a WsByteArrayResult.
     *
     * @param data The data to send.
     */
    public WsBinaryResult(byte[] data) {
        this(ByteBuffer.wrap(Objects.requireNonNull(data)));
    }

    /**
     * Initializes a new instance of a WsByteArrayResult.
     *
     * @param data   The data to send.
     * @param offset The zero-based offset into the array where the data begins.
     * @param length The number of bytes making up the data.
     */
    public WsBinaryResult(byte[] data, int offset, int length) {
        this(ByteBuffer.wrap(Objects.requireNonNull(data), offset, length));
    }

    /**
     * Initializes a new instance of a WsByteArrayResult.
     * @param buffer The data to send.
     */
    public WsBinaryResult(ByteBuffer buffer) {
        Objects.requireNonNull(buffer);
        this.data = buffer;
    }

    /**
     * Gets the data to be sent with the response.
     * @return The data to be sent with the response.
     */
    public ByteBuffer getData() {
        return this.data;
    }

    /**
     * Sets the content as the response.
     * @param context The context.
     */
    @Override
    public void execute(WsContext context) {
        WsResponse response = context.getResponse();
        response.sendBinary(data);
    }
}
