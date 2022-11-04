package com.truncon.javalin.mvc.api;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

final class DownloadResultTest {
    @Test
    void testCtor_stream() {
        InputStream stream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        DownloadResult result = new DownloadResult(stream);
        Assertions.assertSame(stream, result.getInputStream());
        Assertions.assertEquals("application/octet-stream", result.getContentType());
    }

    @Test
    void testCtor_stream_nullStream_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> new DownloadResult((InputStream) null));
    }

    @Test
    void testCtor_streamContentType() {
        InputStream stream = new ByteArrayInputStream(new byte[] { 1, 2, 3 });
        DownloadResult result = new DownloadResult(stream, "text/plain");
        Assertions.assertSame(stream, result.getInputStream());
        Assertions.assertEquals("text/plain", result.getContentType());
    }

    @Test
    void testCtor_streamContentType_nullStream_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> new DownloadResult((InputStream) null, "text/plain"));
    }

    @Test
    void testCtor_byteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        DownloadResult result = new DownloadResult(data);
        byte[] actual = IOUtils.toByteArray(result.getInputStream());
        Assertions.assertArrayEquals(data, actual);
        Assertions.assertEquals("application/octet-stream", result.getContentType());
    }

    @Test
    void testCtor_byteArray_nullData_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> new DownloadResult((byte[]) null));
    }

    @Test
    void testCtor_byteArrayContentType() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        DownloadResult result = new DownloadResult(data, "text/plain");
        byte[] actual = IOUtils.toByteArray(result.getInputStream());
        Assertions.assertArrayEquals(data, actual);
        Assertions.assertEquals("text/plain", result.getContentType());
    }

    @Test
    void testCtor_byteArrayContentType_nullData_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> new DownloadResult((byte[]) null, "text/plain"));
    }

    @Test
    void testCtor_rangedByteArray() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        DownloadResult result = new DownloadResult(data, 1, 1);
        byte[] actual = IOUtils.toByteArray(result.getInputStream());
        Assertions.assertArrayEquals(new byte[] { 2 }, actual);
        Assertions.assertEquals("application/octet-stream", result.getContentType());
    }

    @Test
    void testCtor_rangedByteArray_nullData_throws() {
        Assertions.assertThrows(NullPointerException.class, () -> new DownloadResult(null, 0, 0));
    }

    @Test
    void testCtor_rangedByteArray_negativeOffset_throws() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DownloadResult(new byte[0], -1, 0));
    }

    @Test
    void testCtor_rangedByteArray_negativeLength_throws() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DownloadResult(new byte[0], 0, -1));
    }

    @Test
    void testCtor_rangedByteArray_lengthTooLong_throws() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new DownloadResult(new byte[0], 0, 1));
    }

    @Test
    void testCtor_rangedByteArrayContentType() throws IOException {
        byte[] data = new byte[] { 1, 2, 3 };
        DownloadResult result = new DownloadResult(data, 1, 1, "text/plain");
        byte[] actual = IOUtils.toByteArray(result.getInputStream());
        Assertions.assertArrayEquals(new byte[] { 2 }, actual);
        Assertions.assertEquals("text/plain", result.getContentType());
    }

    @Test
    void testCtor_rangedByteArrayContentType_nullData_throws() {
        Assertions.assertThrows(
            NullPointerException.class,
            () -> new DownloadResult(null, 0, 0, "plain/text")
        );
    }

    @Test
    void testCtor_rangedByteArrayContentType_negativeOffset_throws() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new DownloadResult(new byte[0], -1, 0, "plain/text")
        );
    }

    @Test
    void testCtor_rangedByteArrayContentType_negativeLength_throws() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new DownloadResult(new byte[0], 0, -1, "plain/text")
        );
    }

    @Test
    void testCtor_rangedByteArrayContentType_lengthTooLong_throws() {
        Assertions.assertThrows(
            IllegalArgumentException.class,
            () -> new DownloadResult(new byte[0], 0, 1, "plain/text")
        );
    }

    @Test
    void testSetContentType() {
        DownloadResult result = new DownloadResult(new byte[0]);
        Assertions.assertEquals("application/octet-stream", result.getContentType());
        result.setContentType("text/plain");
        Assertions.assertEquals("text/plain", result.getContentType());
    }

    @Test
    void testSetFilename() {
        DownloadResult result = new DownloadResult(new byte[0]);
        result.setFileName("input.txt");
        Assertions.assertEquals("input.txt", result.getFileName());
    }

    // Do I need to test for file names containing slashes?

    @Test
    void testExecute_noFileName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DownloadResult result = new DownloadResult(inputStream);
        result.setContentType("text/plain");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assertions.assertEquals(inputStream, response.getStreamBody());
        Assertions.assertEquals("text/plain", response.getContentType());
        String header = response.getHeader("Content-Disposition");
        Assertions.assertEquals("attachment;", header);
    }

    @Test
    void testExecute_withFileName() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DownloadResult result = new DownloadResult(inputStream);
        result.setContentType("text/plain");
        result.setFileName("text.txt");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assertions.assertEquals(inputStream, response.getStreamBody());
        Assertions.assertEquals("text/plain", response.getContentType());
        String header = response.getHeader("Content-Disposition");
        Assertions.assertEquals("attachment;fileName=text.txt", header);
    }
}
