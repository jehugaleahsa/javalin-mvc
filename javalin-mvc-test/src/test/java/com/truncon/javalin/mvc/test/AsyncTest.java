package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.AsyncController;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static com.truncon.javalin.mvc.test.QueryUtils.downloadForGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForGet;

public final class AsyncTest {
    @Test
    public void testAsync_getContent() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(AsyncController.GET_CONTENT_ROUTE);
            String result = getStringForGet(route);
            Assert.assertEquals("Hello", result);
        });
    }

    @Test
    public void testAsync_getDownload() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(AsyncController.GET_DOWNLOAD_ROUTE);
            DownloadDetails result = downloadForGet(route);
            Assert.assertEquals("text/plain", result.getContentType());
            Assert.assertEquals("Hello", new String(result.getData(), StandardCharsets.UTF_8));
        });
    }

    @Test
    public void testAsync_getWildcard() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(AsyncController.GET_WILDCARD_ROUTE);
            int result = QueryUtils.getJsonResponseForGet(route, int.class);
            Assert.assertEquals(123, result);
        });
    }

    @Test
    public void testAsync_getWildcard_extendsActionResult() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(AsyncController.GET_WILDCARD_EXTENDS_ACTION_RESULT_ROUTE);
            int result = QueryUtils.getJsonResponseForGet(route, int.class);
            Assert.assertEquals(123, result);
        });
    }

    @Test
    public void testAsync_getObject() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(AsyncController.GET_OBJECT_ROUTE);
            PrimitiveModel result = QueryUtils.getJsonResponseForGet(route, PrimitiveModel.class);
            Assert.assertNotNull(result);
        });
    }

    @Test
    public void testAsync_getPrimitiveInt() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(AsyncController.GET_PRIMITIVE_INT_ROUTE);
            int result = QueryUtils.getJsonResponseForGet(route, int.class);
            Assert.assertEquals(123, result);
        });
    }

    @Test
    public void testAsync_getVoid() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(AsyncController.GET_VOID_ROUTE);
            String result = QueryUtils.getStringForGet(route);
            Assert.assertEquals("Everything's fine", result);
        });
    }
}
