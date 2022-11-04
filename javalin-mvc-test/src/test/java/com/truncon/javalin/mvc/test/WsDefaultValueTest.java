package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue.DefaultValueBuiltinCollectionController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue.DefaultValueBuiltinScalarController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue.DefaultValueStandardCollectionController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue.DefaultValueStandardScalarController;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRoute;

final class WsDefaultValueTest {
    @Test
    void testDefaultValues_builtin_scalar() {
        String route = buildWsRoute(DefaultValueBuiltinScalarController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitJsonResponse("", VariousSourcesModel.class).thenAccept(response -> {
                Assertions.assertEquals("path", response.getPath());
                Assertions.assertEquals("query", response.getQuery());
                Assertions.assertEquals("header", response.getHeader());
                Assertions.assertEquals("cookie", response.getCookie());
                Assertions.assertEquals("any", response.getAny());
            }))
        );
    }

    @Test
    void testDefaultValues_standard_scalar() {
        String route = buildWsRoute(DefaultValueStandardScalarController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitJsonResponse("", VariousSourcesModel.class).thenAccept(response -> {
                Assertions.assertEquals("path", response.getPath());
                Assertions.assertEquals("query", response.getQuery());
                Assertions.assertEquals("header", response.getHeader());
                Assertions.assertEquals("cookie", response.getCookie());
                Assertions.assertEquals("any", response.getAny());
            }))
        );
    }

    @Test
    void testDefaultValues_builtin_collection() {
        String route = buildWsRoute(DefaultValueBuiltinCollectionController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitJsonResponse("", VariousSourcesModel.class).thenAccept(response -> {
                Assertions.assertEquals("path", response.getPath());
                Assertions.assertEquals("query", response.getQuery());
                Assertions.assertEquals("header", response.getHeader());
                Assertions.assertEquals("cookie", response.getCookie());
                Assertions.assertEquals("any", response.getAny());
            }))
        );
    }

    @Test
    void testDefaultValues_standard_collection() {
        String route = buildWsRoute(DefaultValueStandardCollectionController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitJsonResponse("", VariousSourcesModel.class).thenAccept(response -> {
                Assertions.assertEquals("path", response.getPath());
                Assertions.assertEquals("query", response.getQuery());
                Assertions.assertEquals("header", response.getHeader());
                Assertions.assertEquals("cookie", response.getCookie());
                Assertions.assertEquals("any", response.getAny());
            }))
        );
    }
}
