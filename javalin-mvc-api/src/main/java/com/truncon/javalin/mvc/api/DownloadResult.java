package com.truncon.javalin.mvc.api;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

/**
 * Generates a response that will prompt the use to download a file.
 */
public final class DownloadResult implements ActionResult {
    private static final String DEFAULT_CONTENT_TYPE = "application/octet-stream";
    private final InputStream inputStream;
    private String contentType;
    private String fileName;

    /**
     * Initializes a new instance of a DownloadResult with the default MIME type of application/octet-stream.
     * @param inputStream The stream of data to respond with.
     * @throws NullPointerException if the input stream is null.
     */
    public DownloadResult(InputStream inputStream) {
        this(inputStream, null);
    }

    /**
     * Initializes a new instance of a DownloadResult with the specified content type.
     * @param inputStream The stream of data to respond with.
     * @param contentType The MIME type of the content.
     * @throws NullPointerException if the input stream is null.
     */
    public DownloadResult(InputStream inputStream, String contentType) {
        this.inputStream = Objects.requireNonNull(inputStream);
        setContentType(contentType);
    }

    /**
     * Initializes a new instance of a DownloadResult with the specified binary data and the default MIME type of
     * application/octet-stream.
     * @param data The binary data to respond with.
     * @throws NullPointerException if the data array is null.
     */
    public DownloadResult(byte[] data) {
        this(data, 0, data.length, null);
    }

    /**
     * Initializes a new instance of a DownloadResult with the specified binary data and content type.
     * @param data The binary data to respond with.
     * @param contentType The MIME type of the content.
     * @throws NullPointerException if the data array is null.
     */
    public DownloadResult(byte[] data, String contentType) {
        this(data, 0, data.length, contentType);
    }

    /**
     * Initializes a new instance of a DownloadResult with the specified binary data and the default MIME type
     * of application/octet-stream, limited only to the bytes within the specified range.
     * @param data The binary data to respond with.
     * @param offset The offset into the data array where the response should start.
     * @param length The number of bytes in the data array to include.
     * @throws NullPointerException if the data array is null.
     * @throws IllegalArgumentException if the offset is negative, length is negative or length is greater than
     * the number of bytes remaining after the offset.
     */
    public DownloadResult(byte[] data, int offset, int length) {
        this(data, offset, length, null);
    }

    /**
     * Initializes a new instance of a DownloadResult with the specified binary data and content type,
     * limited only to the bytes within the specified range.
     * @param data The binary data to respond with.
     * @param offset The offset into the data array where the response should start.
     * @param length The number of bytes in the data array to include.
     * @param contentType The MIME type of the content.
     * @throws NullPointerException if the data array is null.
     * @throws IllegalArgumentException if the offset is negative, length is negative or length is greater than
      the number of bytes remaining after the offset.
     */
    public DownloadResult(byte[] data, int offset, int length, String contentType) {
        Objects.requireNonNull(data);
        if (offset < 0) {
            throw new IllegalArgumentException("The offset cannot be negative.");
        }
        if (length < 0) {
            throw new IllegalArgumentException("The length cannot be negative.");
        }
        if (length > data.length - offset) {
            throw new IllegalArgumentException("The length exceeds the remaining length of the data array after the offset.");
        }
        this.inputStream = new ByteArrayInputStream(data, offset, length);
        setContentType(contentType);
    }

    /**
     * Gets the input stream that will streamed as the result.
     * @return The input stream.
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * Gets the MIME type of the response.
     * @return the content type.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the MIME type of the response.
     * @param contentType the content type.
     * @return this DownloadResult for further configuration.
     */
    public DownloadResult setContentType(String contentType) {
        this.contentType = isBlank(contentType) ? DEFAULT_CONTENT_TYPE : contentType;
        return this;
    }

    /**
     * Gets the name that will be displayed in the browser download dialog.
     * @return the file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the name that will be displayed in the browser download dialog.
     * @param fileName The name of the file that will be displayed in the browser download dialog.
     * @return this DownloadResult for further configuration.
     */
    public DownloadResult setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    /**
     * Sends the stream as a response to the request with the specified MIME type.
     * @param context The request context.
     */
    @Override
    public void execute(HttpContext context) {
        HttpResponse response = context.getResponse();
        setSynchronousSettings(response);
        response.setStreamBody(inputStream);
    }

    private void setSynchronousSettings(HttpResponse response) {
        String disposition = "attachment;";
        if (!isBlank(fileName)) {
            disposition += "fileName=" + fileName;
        }
        response.setHeader("Content-Disposition", disposition);
        response.setContentType(this.contentType);
    }

    private static boolean isBlank(String value) {
        return value == null || value.trim().length() == 0;
    }
}
