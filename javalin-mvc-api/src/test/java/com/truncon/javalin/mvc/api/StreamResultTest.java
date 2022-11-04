package com.truncon.javalin.mvc.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

final class StreamResultTest {
    @Test
    void testCtor_stream() {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        StreamResult result = new StreamResult(stream);
        Assertions.assertSame(stream, result.getInputStream());
        Assertions.assertEquals("application/octet-stream", result.getContentType());
    }

    @Test
    void testCtor_streamContentType() {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        StreamResult result = new StreamResult(stream, "text/plain");
        Assertions.assertSame(stream, result.getInputStream());
        Assertions.assertEquals("text/plain", result.getContentType());
    }

    @Test
    void testSetContentType() {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        StreamResult result = new StreamResult(stream);
        result.setContentType("text/plain");
        Assertions.assertEquals("text/plain", result.getContentType());
    }

    @Test
    void testExecute() {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        StreamResult result = new StreamResult(stream, "text/plain");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assertions.assertSame(stream, response.getStreamBody());
        Assertions.assertEquals("text/plain", response.getHeader("Content-Type"));
    }
}
