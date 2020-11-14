package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.conversion.WsContextController;
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
    public void testBindSettersFromQuery() throws IOException {
        String baseRoute = WsContextController.ROUTE;
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
