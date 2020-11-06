package com.truncon.javalin.mvc.test;

public final class DownloadDetails {
    private final byte[] data;
    private final String contentType;

    public DownloadDetails(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public String getContentType() {
        return contentType;
    }
}
