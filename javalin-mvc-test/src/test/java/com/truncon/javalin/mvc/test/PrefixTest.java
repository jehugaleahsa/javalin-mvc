package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrefixController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.QueryUtils.getStatusCodeForGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStatusCodeForPost;
import static com.truncon.javalin.mvc.test.QueryUtils.getStatusCodeForPut;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;

final class PrefixTest {
    @Test
    void testPrefix_GET() throws IOException {
        String route = buildRoute(PrefixController.GET_ROUTE);
        AsyncTestUtils.runTest(app -> {
            int statusCode = getStatusCodeForGet(route);
            Assertions.assertEquals(200, statusCode);
        });
    }

    @Test
    void testPrefix_POST() throws IOException {
        String route = buildRoute(PrefixController.POST_ROUTE);
        AsyncTestUtils.runTest(app -> {
            int statusCode = getStatusCodeForPost(route);
            Assertions.assertEquals(200, statusCode);
        });
    }

    @Test
    void testPrefix_PUT() throws IOException {
        String route = buildRoute(PrefixController.PUT_ROUTE);
        AsyncTestUtils.runTest(app -> {
            int statusCode = getStatusCodeForPut(route);
            Assertions.assertEquals(200, statusCode);
        });
    }
}
