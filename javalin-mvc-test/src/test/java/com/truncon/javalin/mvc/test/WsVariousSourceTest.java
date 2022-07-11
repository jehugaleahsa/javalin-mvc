package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.parameters.VariousSourcesBuiltinController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.VariousSourcesStandardController;
import com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue.DefaultValueStandardScalarController;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.cookieParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.headerParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class WsVariousSourceTest {
    @Test
    public void testVariousSources_builtin() {
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
                        Assert.assertEquals("path", response.getPath());
                        Assert.assertEquals("query", response.getQuery());
                        Assert.assertEquals("header", response.getHeader());
                        Assert.assertEquals("cookie", response.getCookie());
                    }))
        );
    }

    @Test
    public void testVariousSources_standard() {
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
                        Assert.assertEquals("path", response.getPath());
                        Assert.assertEquals("query", response.getQuery());
                        Assert.assertEquals("header", response.getHeader());
                        Assert.assertEquals("cookie", response.getCookie());
                    }))
        );
    }
}
