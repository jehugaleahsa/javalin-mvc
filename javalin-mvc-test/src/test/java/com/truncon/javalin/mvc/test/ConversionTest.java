package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ConversionController;
import com.truncon.javalin.mvc.test.models.ConversionModel;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class ConversionTest {
    @Test
    public void testConversion_staticConverter_context_oneParameter() {
        String baseRoute = ConversionController.GET_CONVERSION_CONTEXT1_QUERY_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConversion_staticConverter_request_oneParameter() {
        String baseRoute = ConversionController.GET_CONVERSION_REQUEST1_QUERY_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConversion_staticConverter_context_twoParameters() {
        String baseRoute = ConversionController.GET_CONVERSION_CONTEXT2_QUERY_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConversion_staticConverter_request_twoParameters() {
        String baseRoute = ConversionController.GET_CONVERSION_REQUEST2_QUERY_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConversion_staticConverter_context_threeParameters() {
        String baseRoute = ConversionController.GET_CONVERSION_CONTEXT3_QUERY_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    public void testConversion_staticConverter_request_threeParameters() {
        String baseRoute = ConversionController.GET_CONVERSION_REQUEST3_QUERY_ROUTE;
        testRoute(baseRoute);
    }

    private void testRoute(String baseRoute) {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(baseRoute, queryParams(
                param("boolean", Boolean.toString(true)),
                param("byte", Byte.toString((byte) 11)),
                param("char", "a"),
                param("double", Double.toString(1.11)),
                param("float", Float.toString(2.22f)),
                param("int", Integer.toString(11111111)),
                param("long", Long.toString(2222222222L)),
                param("short", Short.toString((short) 11111))
            ));
            ConversionModel model = QueryUtils.getJsonResponseForGet(route, ConversionModel.class);
            Assert.assertTrue(model.getBoolean());
            Assert.assertEquals((byte) 22, model.getByte());
            Assert.assertEquals('A', model.getChar());
            Assert.assertEquals(2.22, model.getDouble(), 0.0);
            Assert.assertEquals(4.44f, model.getFloat(), 0.0);
            Assert.assertEquals(22222222, model.getInteger());
            Assert.assertEquals(4444444444L, model.getLong());
            Assert.assertEquals((short) 22222, model.getShort());
        });
    }
}