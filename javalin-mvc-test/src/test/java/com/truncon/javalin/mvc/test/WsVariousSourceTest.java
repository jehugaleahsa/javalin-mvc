package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.parameters.VariousSourcesBuiltinController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.VariousSourcesStandardController;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.cookieParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.headerParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

final class WsVariousSourceTest {
    @Test
    void testVariousSources_builtin() {
        String route = buildRoute(VariousSourcesBuiltinController.ROUTE,
            pathParams(
                param("value", "path")
            ),
            queryParams(
                param("value", "query")
            ),
            true
        );
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(
                route,
                (u) -> {},
                headerParams(param("value", "header")),
                cookieParams(param("value", "cookie")),
                session -> session
                    .sendStringAndAwaitJsonResponse("", VariousSourcesModel.class)
                    .thenAccept(response -> {
                        Assertions.assertEquals("path", response.getPath());
                        Assertions.assertEquals("query", response.getQuery());
                        Assertions.assertEquals("header", response.getHeader());
                        Assertions.assertEquals("cookie", response.getCookie());
                    }))
        );
    }

    @Test
    void testVariousSources_standard() {
        String route = buildRoute(VariousSourcesStandardController.ROUTE,
            pathParams(
                param("value", "path")
            ),
            queryParams(
                param("value", "query")
            ),
            true
        );
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(
                route,
                (u) -> {},
                headerParams(param("value", "header")),
                cookieParams(param("value", "cookie")),
                session -> session
                    .sendStringAndAwaitJsonResponse("", VariousSourcesModel.class)
                    .thenAccept(response -> {
                        Assertions.assertEquals("path", response.getPath());
                        Assertions.assertEquals("query", response.getQuery());
                        Assertions.assertEquals("header", response.getHeader());
                        Assertions.assertEquals("cookie", response.getCookie());
                    }))
        );
    }
}
