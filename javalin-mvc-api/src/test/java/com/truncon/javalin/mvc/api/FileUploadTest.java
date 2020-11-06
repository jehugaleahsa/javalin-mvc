package com.truncon.javalin.mvc.api;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class FileUploadTest {
    @Test
    public void testCtor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        String contentType = "text/plain";
        String fileName = "test.txt";
        FileUpload upload = new FileUpload(inputStream, contentType, fileName);
        Assert.assertSame(inputStream, upload.getStream());
        Assert.assertEquals(contentType, upload.getContentType());
        Assert.assertEquals(fileName, upload.getFileName());
    }
}
