package com.truncon.javalin.mvc.api;

import java.io.InputStream;
import java.util.Objects;

/**
 * Generates a response containing the contents of a stream.
 */
public final class StreamResult implements ActionResult {
    private final InputStream stream;
    private final String contentType;

    /**
     * Initializes a new instance of a StreamResult.
     * @param stream The stream to send as a response.
     * @param contentType The MIME type of the response.
     */
    public StreamResult(InputStream stream, String contentType) {
        this.stream = Objects.requireNonNull(stream);
        this.contentType = contentType;
    }

    /**
     * Sets the content type header and sends the stream as the response.
     * @param context The request context.
     */
    public void execute(HttpContext context) {
        HttpResponse response = context.getResponse();
        response.setHeader("Content-Type", contentType);
        response.setStreamBody(stream);
    }

    /**
     * Sets the content type header synchronously, then returns the stream to be sent asynchronously.
     * @param context The request context.
     * @return the stream.
     */
    public Object executeAsync(HttpContext context) {
        HttpResponse response = context.getResponse();
        response.setHeader("Content-Type", contentType);
        return stream;
    }
}
