package com.truncon.javalin.mvc.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class ContentResultTest {
    @Test
    void testCtor_content() {
        ContentResult result = new ContentResult("hello");
        Assertions.assertEquals("hello", result.getContent());
        Assertions.assertEquals(200, result.getStatusCode());
    }

    @Test
    void testCtor_contentStatusCode() {
        ContentResult result = new ContentResult("bad", 400);
        Assertions.assertEquals("bad", result.getContent());
        Assertions.assertEquals(400, result.getStatusCode());
    }

    @Test
    void testExecute_setsStatusCodeBody() {
        ContentResult result = new ContentResult("hello");
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        Assertions.assertEquals(200, context.getResponse().getStatusCode());
        Assertions.assertEquals("hello", context.getResponse().getTextBody());
    }
}
