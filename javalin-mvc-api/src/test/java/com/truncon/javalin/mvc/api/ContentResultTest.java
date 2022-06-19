package com.truncon.javalin.mvc.api;

import org.junit.Assert;
import org.junit.Test;

public final class ContentResultTest {
    @Test
    public void testCtor_content() {
        ContentResult result = new ContentResult("hello");
        Assert.assertEquals("hello", result.getContent());
        Assert.assertEquals(200, result.getStatusCode());
    }

    @Test
    public void testCtor_contentStatusCode() {
        ContentResult result = new ContentResult("bad", 400);
        Assert.assertEquals("bad", result.getContent());
        Assert.assertEquals(400, result.getStatusCode());
    }

    @Test
    public void testExecute_setsStatusCodeBody() {
        ContentResult result = new ContentResult("hello");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        Assert.assertEquals(200, context.getResponse().getStatusCode());
        Assert.assertEquals("hello", context.getResponse().getTextBody());
    }
}
