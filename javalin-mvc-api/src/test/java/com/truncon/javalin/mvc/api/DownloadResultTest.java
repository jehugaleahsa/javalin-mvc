package com.truncon.javalin.mvc.api;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class DownloadResultTest {
    @Test
    public void testCtor_stream() {
        InputStream stream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        DownloadResult result = new DownloadResult(stream);
        Assert.assertSame(stream, result.getInputStream());
        Assert.assertEquals("application/octet-stream", result.getContentType());
    }

    @Test(expected = NullPointerException.class)
    public void testCtor_stream_nullStream_throws() {
        new DownloadResult((InputStream) null);
    }

    @Test
    public void testCtor_streamContentType() {
        InputStream stream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        DownloadResult result = new DownloadResult(stream, "text/plain");
        Assert.assertSame(stream, result.getInputStream());
        Assert.assertEquals("text/plain", result.getContentType());
    }

    @Test(expected = NullPointerException.class)
    public void testCtor_streamContentType_nullStream_throws() {
        new DownloadResult((InputStream) null, "text/plain");
    }

    @Test
    public void testCtor_byteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        DownloadResult result = new DownloadResult(data);
        byte[] actual = IOUtils.toByteArray(result.getInputStream());
        Assert.assertArrayEquals(data, actual);
        Assert.assertEquals("application/octet-stream", result.getContentType());
    }

    @Test(expected = NullPointerException.class)
    public void testCtor_byteArray_nullData_throws() {
        new DownloadResult((byte[]) null);
    }

    @Test
    public void testCtor_byteArrayContentType() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        DownloadResult result = new DownloadResult(data, "text/plain");
        byte[] actual = IOUtils.toByteArray(result.getInputStream());
        Assert.assertArrayEquals(data, actual);
        Assert.assertEquals("text/plain", result.getContentType());
    }

    @Test(expected = NullPointerException.class)
    public void testCtor_byteArrayContentType_nullData_throws() {
        new DownloadResult((byte[]) null, "text/plain");
    }

    @Test
    public void testCtor_rangedByteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        DownloadResult result = new DownloadResult(data, 1, 1);
        byte[] actual = IOUtils.toByteArray(result.getInputStream());
        Assert.assertArrayEquals(new byte[] { 2 }, actual);
        Assert.assertEquals("application/octet-stream", result.getContentType());
    }

    @Test(expected = NullPointerException.class)
    public void testCtor_rangedByteArray_nullData_throws() {
        new DownloadResult(null, 0, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor_rangedByteArray_negativeOffset_throws() {
        new DownloadResult(new byte[0], -1, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor_rangedByteArray_negativeLength_throws() {
        new DownloadResult(new byte[0], 0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor_rangedByteArray_lengthTooLong_throws() {
        new DownloadResult(new byte[0], 0, 1);
    }

    @Test
    public void testCtor_rangedByteArrayContentType() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        DownloadResult result = new DownloadResult(data, 1, 1, "text/plain");
        byte[] actual = IOUtils.toByteArray(result.getInputStream());
        Assert.assertArrayEquals(new byte[] { 2 }, actual);
        Assert.assertEquals("text/plain", result.getContentType());
    }

    @Test(expected = NullPointerException.class)
    public void testCtor_rangedByteArrayContentType_nullData_throws() {
        new DownloadResult(null, 0, 0, "plain/text");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor_rangedByteArrayContentType_negativeOffset_throws() {
        new DownloadResult(new byte[0], -1, 0, "plain/text");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor_rangedByteArrayContentType_negativeLength_throws() {
        new DownloadResult(new byte[0], 0, -1, "plain/text");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCtor_rangedByteArrayContentType_lengthTooLong_throws() {
        new DownloadResult(new byte[0], 0, 1, "plain/text");
    }

    @Test
    public void testSetContentType() {
        DownloadResult result = new DownloadResult(new byte[0]);
        Assert.assertEquals("application/octet-stream", result.getContentType());
        result.setContentType("text/plain");
        Assert.assertEquals("text/plain", result.getContentType());
    }

    @Test
    public void testSetFilename() {
        DownloadResult result = new DownloadResult(new byte[0]);
        result.setFileName("input.txt");
        Assert.assertEquals("input.txt", result.getFileName());
    }

    // Do I need to test for file names containing slashes?

    @Test
    public void testExecute_noFileName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DownloadResult result = new DownloadResult(inputStream);
        result.setContentType("text/plain");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assert.assertEquals(inputStream, response.getStreamBody());
        Assert.assertEquals("text/plain", response.getContentType());
        String header = response.getHeader("Content-Disposition");
        Assert.assertEquals("attachment;", header);
    }

    @Test
    public void testExecute_withFileName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DownloadResult result = new DownloadResult(inputStream);
        result.setContentType("text/plain");
        result.setFileName("text.txt");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assert.assertEquals(inputStream, response.getStreamBody());
        Assert.assertEquals("text/plain", response.getContentType());
        String header = response.getHeader("Content-Disposition");
        Assert.assertEquals("attachment;fileName=text.txt", header);
    }
}
