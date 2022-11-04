package com.truncon.javalin.mvc.api.ws;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class WsJsonResultTest {
    @Test
    void testCtor() {
        Object data = new Object();
        WsJsonResult result = new WsJsonResult(data);
        Assertions.assertSame(data, result.getData());
    }

    @Test
    void testExecute() {
        Object data = new Object();
        WsJsonResult result = new WsJsonResult(data);
        MockWsContext context = new MockWsContext();
        result.execute(context);
        Assertions.assertSame(data, context.getResponse().getJsonData());
    }
}
