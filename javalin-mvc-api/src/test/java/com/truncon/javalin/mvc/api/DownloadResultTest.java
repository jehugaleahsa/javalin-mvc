package com.truncon.javalin.mvc.api;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class DownloadResultTest {
    @Test
    public void testCtor_stream() {
        InputStream stream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        DownloadResult result = new DownloadResult(stream);
        Assert.assertSame(stream, result.getInputStream());
        Assert.assertNull(result.getContentType());
    }
}