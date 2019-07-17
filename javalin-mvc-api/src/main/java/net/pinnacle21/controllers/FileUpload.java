package io.javalin.mvc.api;

import java.io.InputStream;

/**
 * Represents a file that was uploaded.
 */
public final class FileUpload {
    private final InputStream stream;
    private final String contentType;
    private final String fileName;

    /**
     * Instantiates a new instance of a FileUpload.
     * @param stream The stream containing the file contents.
     * @param contentType The type of the stream contents.
     * @param fileName The name of the file (optional).
     */
    public FileUpload(InputStream stream, String contentType, String fileName) {
        this.stream = stream;
        this.contentType = contentType;
        this.fileName = fileName;
    }

    /**
     * Gets the stream containing the file contents.
     * @return the stream.
     */
    public InputStream getStream() {
        return stream;
    }

    /**
     * Gets the type of the stream contents.
     * @return the type of the stream contents.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Gets the name of the file (optional).
     * @return the name of the file.
     */
    public String getFileName() {
        return fileName;
    }
}
