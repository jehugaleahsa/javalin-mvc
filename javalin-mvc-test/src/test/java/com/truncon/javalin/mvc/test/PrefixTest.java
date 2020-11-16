package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrefixController;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.QueryUtils.getStatusCodeForGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStatusCodeForPost;
import static com.truncon.javalin.mvc.test.QueryUtils.getStatusCodeForPut;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;

public final class PrefixTest {
    @Test
    public void testPrefix_GET() throws IOException {
        String route = buildRoute(PrefixController.GET_ROUTE);
        AsyncTestUtils.runTest(app -> {
            int statusCode = getStatusCodeForGet(route);
            Assert.assertEquals(200, statusCode);
        });
    }

    @Test
    public void testPrefix_POST() throws IOException {
        String route = buildRoute(PrefixController.POST_ROUTE);
        AsyncTestUtils.runTest(app -> {
            int statusCode = getStatusCodeForPost(route);
            Assert.assertEquals(200, statusCode);
        });
    }

    @Test
    public void testPrefix_PUT() throws IOException {
        String route = buildRoute(PrefixController.PUT_ROUTE);
        AsyncTestUtils.runTest(app -> {
            int statusCode = getStatusCodeForPut(route);
            Assert.assertEquals(200, statusCode);
        });
    }
}
