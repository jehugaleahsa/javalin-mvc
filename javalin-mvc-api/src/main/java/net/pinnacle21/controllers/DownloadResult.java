package io.javalin.mvc.api;

import java.io.InputStream;
import java.util.Objects;

/**
 * Generates a response that will prompt the use to download a file.
 */
public final class DownloadResult implements ActionResult {
    private final InputStream inputStream;
    private String contentType;
    private String fileName;

    /**
     * Initializes a new instance of a DownloadResult with the default MIME type of application/octet-stream.
     * @param inputStream The stream of data to respond with.
     */
    public DownloadResult(InputStream inputStream) {
        this(inputStream, null);
    }

    /**
     * Initializes a new instance of a DownloadResult with the specified content type.
     * @param inputStream The stream of data to respond with.
     * @param contentType The MIME type of the content.
     */
    public DownloadResult(InputStream inputStream, String contentType) {
        this.inputStream = Objects.requireNonNull(inputStream);
        this.contentType = contentType;
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
        this.contentType = contentType;
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
    public void execute(HttpContext context) {
        HttpResponse response = context.getResponse();
        setSynchronousSettings(response);
        response.setStreamBody(inputStream);
    }

    /**
     * Sets the status code and headers, and returns the stream to be sent asynchronously.
     * @param context The request context.
     * @return the stream.
     */
    public Object executeAsync(HttpContext context) {
        HttpResponse response = context.getResponse();
        setSynchronousSettings(response);
        return inputStream;
    }

    private void setSynchronousSettings(HttpResponse response) {
        String disposition = "attachment;";
        if (!isEmpty(fileName)) {
            disposition += "fileName=" + fileName;
        }
        response.setHeader("Content-Disposition", disposition);
        String contentType = isEmpty(this.contentType) ? "application/octet-stream" : this.contentType;
        response.setContentType(contentType);
    }

    private static boolean isEmpty(String value) {
        return value == null || value.trim().length() == 0;
    }
}
