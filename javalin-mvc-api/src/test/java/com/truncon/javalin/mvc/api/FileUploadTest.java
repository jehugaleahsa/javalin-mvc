package com.truncon.javalin.mvc.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

final class FileUploadTest {
    @Test
    void testCtor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String contentType = "text/plain";
        String fileName = "test.txt";
        FileUpload upload = new FileUpload(inputStream, contentType, fileName);
        Assertions.assertSame(inputStream, upload.getStream());
        Assertions.assertEquals(contentType, upload.getContentType());
        Assertions.assertEquals(fileName, upload.getFileName());
    }
}
