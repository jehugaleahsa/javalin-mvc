package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.HttpMethodController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getGetStringResponse;

public class HttpMethodTest {
    @Test
    public void testGet() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(HttpMethodController.GET_ROUTE);
            String response = getGetStringResponse(route);
            Assert.assertEquals("GET", response);
        }).join();
    }
}
