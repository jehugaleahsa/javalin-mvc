package com.truncon.javalin.mvc.api;

import org.junit.Assert;
import org.junit.Test;

public final class JsonResultTest {
    @Test
    public void testCtor_data() {
        Object data = new Object();
        JsonResult result = new JsonResult(data);
        Assert.assertSame(data, result.getData());
    }

    @Test
    public void testCtor_dataStatusCode() {
        Object data = new Object();
        JsonResult result = new JsonResult(data, 400);
        Assert.assertSame(data, result.getData());
        Assert.assertEquals(400, result.getStatusCode());
    }

    @Test
    public void testExecute() {
        Object data = new Object();
        JsonResult result = new JsonResult(data, 400);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertSame(data, response.getJsonBody());
    }

    @Test
    public void testExecuteAsync() {
        Object data = new Object();
        JsonResult result = new JsonResult(data, 400);
        MockHttpContext context = new MockHttpContext();
        result.executeAsync(context);
        MockHttpResponse response = context.getResponse();
        Assert.assertEquals(400, response.getStatusCode());
        Assert.assertTrue(context.isToJsonCalled());
    }
}
