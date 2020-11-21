package com.truncon.javalin.mvc.api.ws;

/**
 * Provides information about a received binary message.
 */
public interface WsBinaryMessageContext extends WsContext {

    /**
     * Gets the binary data as a {@link byte[]}.
     * @return The binary data.
     */
    byte[] getData();

    /**
     * Gets the starting offset into the binary data.
     * @return The starting offset.
     */
    int getOffset();

    /**
     * Gets the number of bytes to read.
     * @return The number of bytes.
     */
    int getLength();
}
