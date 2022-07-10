package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue.DefaultValueBuiltinCollectionController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue.DefaultValueBuiltinScalarController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue.DefaultValueStandardCollectionController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue.DefaultValueStandardScalarController;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRoute;

public final class WsDefaultValueTest {
    @Test
    public void testDefaultValues_builtin_scalar() {
        String route = buildWsRoute(DefaultValueBuiltinScalarController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitJsonResponse("", VariousSourcesModel.class).thenAccept(response -> {
                Assert.assertEquals("path", response.getPath());
                Assert.assertEquals("query", response.getQuery());
                Assert.assertEquals("header", response.getHeader());
                Assert.assertEquals("cookie", response.getCookie());
                Assert.assertEquals("any", response.getAny());
            }))
        );
    }

    @Test
    public void testDefaultValues_standard_scalar() {
        String route = buildWsRoute(DefaultValueStandardScalarController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitJsonResponse("", VariousSourcesModel.class).thenAccept(response -> {
                Assert.assertEquals("path", response.getPath());
                Assert.assertEquals("query", response.getQuery());
                Assert.assertEquals("header", response.getHeader());
                Assert.assertEquals("cookie", response.getCookie());
                Assert.assertEquals("any", response.getAny());
            }))
        );
    }

    @Test
    public void testDefaultValues_builtin_collection() {
        String route = buildWsRoute(DefaultValueBuiltinCollectionController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitJsonResponse("", VariousSourcesModel.class).thenAccept(response -> {
                Assert.assertEquals("path", response.getPath());
                Assert.assertEquals("query", response.getQuery());
                Assert.assertEquals("header", response.getHeader());
                Assert.assertEquals("cookie", response.getCookie());
                Assert.assertEquals("any", response.getAny());
            }))
        );
    }

    @Test
    public void testDefaultValues_standard_collection() {
        String route = buildWsRoute(DefaultValueStandardCollectionController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitJsonResponse("", VariousSourcesModel.class).thenAccept(response -> {
                Assert.assertEquals("path", response.getPath());
                Assert.assertEquals("query", response.getQuery());
                Assert.assertEquals("header", response.getHeader());
                Assert.assertEquals("cookie", response.getCookie());
                Assert.assertEquals("any", response.getAny());
            }))
        );
    }
}
