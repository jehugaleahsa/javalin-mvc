package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ConversionController;
import com.truncon.javalin.mvc.test.controllers.PairController;
import com.truncon.javalin.mvc.test.models.ConversionModel;
import com.truncon.javalin.mvc.test.models.HttpModelWithConversionModel;
import com.truncon.javalin.mvc.test.models.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

final class ConversionTest {
    @Test
    void testConversion_staticConverter_context() {
        String baseRoute = ConversionController.GET_CONVERSION_CONTEXT_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    void testConversion_staticConverter_request() {
        String baseRoute = ConversionController.GET_CONVERSION_REQUEST_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    void testConversion_staticConverter_contextName() {
        String baseRoute = ConversionController.GET_CONVERSION_CONTEXT_NAME_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    void testConversion_staticConverter_requestName() {
        String baseRoute = ConversionController.GET_CONVERSION_REQUEST_NAME_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    void testConversion_staticConverter_contextSource() {
        String baseRoute = ConversionController.GET_CONVERSION_CONTEXT_SOURCE_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    void testConversion_staticConverter_requestSource() {
        String baseRoute = ConversionController.GET_CONVERSION_REQUEST_SOURCE_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    void testConversion_staticConverter_contextNameSource() {
        String baseRoute = ConversionController.GET_CONVERSION_CONTEXT_NAME_SOURCE_ROUTE;
        testRoute(baseRoute);
    }

    @Test
    void testConversion_staticConverter_requestNameSource() {
        String baseRoute = ConversionController.GET_CONVERSION_REQUEST_NAME_SOURCE_ROUTE;
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
            ConversionModel model = getJsonResponseForGet(route, ConversionModel.class);
            assertConversionModel(model);
        });
    }

    private void assertConversionModel(ConversionModel model) {
        Assertions.assertNotNull(model);
        Assertions.assertTrue(model.getBoolean());
        Assertions.assertEquals((byte) 22, model.getByte());
        Assertions.assertEquals('A', model.getChar());
        Assertions.assertEquals(2.22, model.getDouble(), 0.0);
        Assertions.assertEquals(4.44f, model.getFloat(), 0.0);
        Assertions.assertEquals(22222222, model.getInteger());
        Assertions.assertEquals(4444444444L, model.getLong());
        Assertions.assertEquals((short) 22222, model.getShort());
    }

    @Test
    void testConversion_staticConverter_nested() {
        String baseRoute = ConversionController.GET_CONVERSION_NESTED_ROUTE;
        testNestedRoute(baseRoute);
    }

    private void testNestedRoute(String baseRoute) {
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
            HttpModelWithConversionModel model = getJsonResponseForGet(route, HttpModelWithConversionModel.class);
            Assertions.assertNotNull(model);
            assertConversionModel(model.field);
            assertConversionModel(model.getModel1());
            assertConversionModel(model.getModel2());
        });
    }

    @Test
    void testConversion_typeSpecifiesConverter() throws IOException {
        Pair pair = new Pair(123, 456);
        String route = buildRouteWithPathParams(PairController.ROUTE, pathParams(param("value", pair.toString())));
        AsyncTestUtils.runTest(app -> {
            String response = getStringForGet(route);
            Pair returnedPair = Pair.parse(response);
            Assertions.assertNotNull(returnedPair);
            Assertions.assertEquals(pair.getFirst(), returnedPair.getFirst());
            Assertions.assertEquals(pair.getSecond(), returnedPair.getSecond());
        });
    }
}
