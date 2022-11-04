package com.truncon.javalin.mvc.api.ws;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class WsContentResultTest {
    @Test
    void testCtor() {
        WsContentResult result = new WsContentResult("hello");
        Assertions.assertEquals("hello", result.getContent());
    }

    @Test
    void testExecute() {
        WsContentResult result = new WsContentResult("hello");
        MockWsContext context = new MockWsContext();
        result.execute(context);
        Assertions.assertEquals("hello", context.getResponse().getText());
    }
}
