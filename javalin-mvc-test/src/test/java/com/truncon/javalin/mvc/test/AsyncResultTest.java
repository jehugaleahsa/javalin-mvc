package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.AsyncController;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static com.truncon.javalin.mvc.test.QueryUtils.downloadForGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForGet;

public final class AsyncResultTest {
    @Test
    public void testAsync_getContent() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(AsyncController.GET_CONTENT_ROUTE);
            String result = getStringForGet(route);
            Assert.assertEquals("Hello", result);
        }).join();
    }

    @Test
    public void testAsync_getDownload() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRoute(AsyncController.GET_DOWNLOAD_ROUTE);
            DownloadDetails result = downloadForGet(route);
            Assert.assertEquals("text/plain", result.getContentType());
            Assert.assertEquals("Hello", new String(result.getData(), StandardCharsets.UTF_8));
        }).join();
    }
}
