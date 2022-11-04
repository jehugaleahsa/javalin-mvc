package com.truncon.javalin.mvc.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class JsonResultTest {
    @Test
    void testCtor_data() {
        Object data = new Object();
        JsonResult result = new JsonResult(data);
        Assertions.assertSame(data, result.getData());
    }

    @Test
    void testCtor_dataStatusCode() {
        Object data = new Object();
        JsonResult result = new JsonResult(data, 400);
        Assertions.assertSame(data, result.getData());
        Assertions.assertEquals(400, result.getStatusCode());
    }

    @Test
    void testExecute() {
        Object data = new Object();
        JsonResult result = new JsonResult(data, 400);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assertions.assertEquals(400, response.getStatusCode());
        Assertions.assertSame(data, response.getJsonBody());
    }

    @Test
    void testExecute_streaming() {
        Object data = new Object();
        JsonResult result = new JsonResult(data, 400, true);
        MockHttpContext context = new MockHttpContext();
        result.execute(context);
        MockHttpResponse response = context.getResponse();
        Assertions.assertEquals(400, response.getStatusCode());
        Assertions.assertSame(data, response.getJsonStreamBody());
    }
}
