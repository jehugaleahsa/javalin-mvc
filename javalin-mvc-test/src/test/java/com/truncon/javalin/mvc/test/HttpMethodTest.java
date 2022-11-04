package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.HttpMethodController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringForGet;

class HttpMethodTest {
    @Test
    void testGet() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(HttpMethodController.GET_ROUTE);
            String response = getStringForGet(route);
            Assertions.assertEquals("GET", response);
        });
    }
}
