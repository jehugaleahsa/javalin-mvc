package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.conversion.WsContextController;
import com.truncon.javalin.mvc.test.controllers.ws.conversion.WsContextNameController;
import com.truncon.javalin.mvc.test.controllers.ws.conversion.WsContextNameSourceController;
import com.truncon.javalin.mvc.test.controllers.ws.conversion.WsContextSourceController;
import com.truncon.javalin.mvc.test.controllers.ws.conversion.WsRequestController;
import com.truncon.javalin.mvc.test.controllers.ws.conversion.WsRequestNameController;
import com.truncon.javalin.mvc.test.controllers.ws.conversion.WsRequestNameSourceController;
import com.truncon.javalin.mvc.test.controllers.ws.conversion.WsRequestSourceController;
import com.truncon.javalin.mvc.test.models.ConversionModel;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.QueryUtils.parseJson;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class WsConversionTest {
    @Test
    public void testConverter_context_query() throws IOException {
        String baseRoute = WsContextController.ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConverter_contextName_query() throws IOException {
        String baseRoute = WsContextNameController.ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConverter_contextSource_query() throws IOException {
        String baseRoute = WsContextSourceController.ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConverter_contextNameSource_query() throws IOException {
        String baseRoute = WsContextNameSourceController.ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConverter_request_query() throws IOException {
        String baseRoute = WsRequestController.ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConverter_requestName_query() throws IOException {
        String baseRoute = WsRequestNameController.ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConverter_requestSource_query() throws IOException {
        String baseRoute = WsRequestSourceController.ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConverter_requestNameSource_query() throws IOException {
        String baseRoute = WsRequestNameSourceController.ROUTE;
        testRoute(baseRoute);
    }

    private void testRoute(String baseRoute) throws IOException {
        String route = buildWsRouteWithQueryParams(baseRoute, queryParams(
            param("boolean", Boolean.toString(true)),
            param("byte", Byte.toString((byte) 11)),
            param("char", "a"),
            param("double", Double.toString(1.11)),
            param("float", Float.toString(2.22f)),
            param("int", Integer.toString(11111111)),
            param("long", Long.toString(2222222222L)),
            param("short", Short.toString((short) 11111))
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response -> {
                ConversionModel model = parseJson(response, ConversionModel.class);
                Assert.assertTrue(model.getBoolean());
                Assert.assertEquals((byte) 22, model.getByte());
                Assert.assertEquals('A', model.getChar());
                Assert.assertEquals(2.22, model.getDouble(), 0.0);
                Assert.assertEquals(4.44f, model.getFloat(), 0.0);
                Assert.assertEquals(22222222, model.getInteger());
                Assert.assertEquals(4444444444L, model.getLong());
                Assert.assertEquals((short) 22222, model.getShort());
            }))
        );
    }
}
