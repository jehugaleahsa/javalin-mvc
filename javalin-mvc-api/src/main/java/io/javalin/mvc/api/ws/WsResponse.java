package io.javalin.mvc.api.ws;

import java.nio.ByteBuffer;

/**
 * Provides functionality for sending a WebSocket response.
 */
public interface WsResponse {
    /**
     * Sends textual content to the client.
     * @param content The text to send.
     */
    void sendText(String content);

    /**
     * Sends the given object serialized as JSON to the client.
     * @param data The object to send as JSON.
     */
    void sendJson(Object data);

    /**
     * Sends binary data to the client.
     * @param data   A byte array containing the data to send.
     */
    default void sendBinary(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        sendBinary(buffer);
    }

    /**
     * Sends binary data to the client.
     * @param data A byte array containing the data to send.
     * @param offset An offset into the array at the start of the data to send.
     * @param length The number of bytes from the starting offset to send.
     */
    default void sendBinary(byte[] data, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(data, offset, length);
        sendBinary(buffer);
    }

    /**
     * Send binary data to the client.
     * @param buffer A {@link ByteBuffer} wrapping the data to send.
     */
    void sendBinary(ByteBuffer buffer);
}
