package com.truncon.javalin.mvc.api;

import java.io.InputStream;
import java.util.Objects;

/**
 * Generates a response containing the contents of a stream.
 */
public final class StreamResult implements ActionResult {
    private final InputStream stream;
    private String contentType;

    /**
     * Initializes a new instance of a StreamResult.
     * @param stream The stream to send as a response.
     */
    public StreamResult(InputStream stream) {
        this(stream, null);
    }

    /**
     * Initializes a new instance of a StreamResult.
     * @param stream The stream to send as a response.
     * @param contentType The MIME type of the response.
     */
    public StreamResult(InputStream stream, String contentType) {
        this.stream = Objects.requireNonNull(stream);
        this.setContentType(contentType);
    }

    /**
     * Gets the {@link InputStream} that sources the data included in the response.
     * @return The {@link InputStream} that sources the data included in the response.
     */
    public InputStream getInputStream() {
        return stream;
    }

    /**
     * Gets the MIME type of the response.
     * @return The MIME type of the response.
     */
    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType == null || contentType.isEmpty() || contentType.trim().isEmpty()
            ? "application/octet-stream"
            : contentType;
    }

    /**
     * Sets the content type header and sends the stream as the response.
     * @param context The request context.
     */
    @Override
    public void execute(HttpContext context) {
        HttpResponse response = context.getResponse();
        response.setHeader("Content-Type", contentType);
        response.setStreamBody(stream);
    }
}
