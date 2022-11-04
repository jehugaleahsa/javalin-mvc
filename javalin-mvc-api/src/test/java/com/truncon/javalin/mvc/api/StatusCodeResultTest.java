package com.truncon.javalin.mvc.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class StatusCodeResultTest {
    @Test
    void testCtor() {
        StatusCodeResult result = new StatusCodeResult(200);
        Assertions.assertEquals(200, result.getStatusCode());
    }

    @Test
    void testExecute() {
        StatusCodeResult result = new StatusCodeResult(400);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        Assertions.assertEquals(400, context.getResponse().getStatusCode());
    }
}
