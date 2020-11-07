package com.truncon.javalin.mvc.api.ws;

import org.junit.Assert;
import org.junit.Test;

public final class WsJsonResultTest {
    @Test
    public void testCtor() {
        Object data = new Object();
        WsJsonResult result = new WsJsonResult(data);
        Assert.assertSame(data, result.getData());
    }

    @Test
    public void testExecute() {
        Object data = new Object();
        WsJsonResult result = new WsJsonResult(data);
        MockWsContext context = new MockWsContext();
        result.execute(context);
        Assert.assertSame(data, context.getResponse().getJsonData());
    }
}
