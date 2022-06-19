package com.truncon.javalin.mvc.api;

import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;

public final class StreamResultTest {
    @Test
    public void testCtor_stream() {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        StreamResult result = new StreamResult(stream);
        Assert.assertSame(stream, result.getInputStream());
        Assert.assertEquals("application/octet-stream", result.getContentType());
    }

    @Test
    public void testCtor_streamContentType() {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        StreamResult result = new StreamResult(stream, "text/plain");
        Assert.assertSame(stream, result.getInputStream());
        Assert.assertEquals("text/plain", result.getContentType());
    }

    @Test
    public void testSetContentType() {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        StreamResult result = new StreamResult(stream);
        result.setContentType("text/plain");
        Assert.assertEquals("text/plain", result.getContentType());
    }

    @Test
    public void testExecute() {
        ByteArrayInputStream stream = new ByteArrayInputStream(new byte[0]);
        StreamResult result = new StreamResult(stream, "text/plain");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assert.assertSame(stream, response.getStreamBody());
        Assert.assertEquals("text/plain", response.getHeader("Content-Type"));
    }
}
