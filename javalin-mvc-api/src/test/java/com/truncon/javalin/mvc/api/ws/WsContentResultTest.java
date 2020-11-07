package com.truncon.javalin.mvc.api.ws;

import org.junit.Assert;
import org.junit.Test;

public final class WsContentResultTest {
    @Test
    public void testCtor() {
        WsContentResult result = new WsContentResult("hello");
        Assert.assertEquals("hello", result.getContent());
    }

    @Test
    public void testExecute() {
        WsContentResult result = new WsContentResult("hello");
        MockWsContext context = new MockWsContext();
        result.execute(context);
        Assert.assertEquals("hello", context.getResponse().getText());
    }
}
