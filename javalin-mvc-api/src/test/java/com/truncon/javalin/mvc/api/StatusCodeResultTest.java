package com.truncon.javalin.mvc.api;

import org.junit.Assert;
import org.junit.Test;

public final class StatusCodeResultTest {
    @Test
    public void testCtor() {
        StatusCodeResult result = new StatusCodeResult(200);
        Assert.assertEquals(200, result.getStatusCode());
    }

    @Test
    public void testExecute() {
        StatusCodeResult result = new StatusCodeResult(400);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        Assert.assertEquals(400, context.getResponse().getStatusCode());
    }

    @Test
    public void testExecuteAsync() {
        StatusCodeResult result = new StatusCodeResult(400);
        MockHttpContext context = new MockHttpContext();
        result.executeAsync(context);
        Assert.assertEquals(400, context.getResponse().getStatusCode());
    }
}
