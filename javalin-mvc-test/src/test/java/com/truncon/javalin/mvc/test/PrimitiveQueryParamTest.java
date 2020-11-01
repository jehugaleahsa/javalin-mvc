package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveQueryParamController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getGetStringResponse;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class PrimitiveQueryParamTest {
    @Test
    public void testBoolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRouteWithQueryParams(
                PrimitiveQueryParamController.BOOLEAN_ROUTE,
                queryParams(param("value", value)));
            String response = getGetStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testInteger() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRouteWithQueryParams(
                PrimitiveQueryParamController.INTEGER_ROUTE,
                queryParams(param("value", value)));
            String response = getGetStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDouble() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRouteWithQueryParams(
                PrimitiveQueryParamController.DOUBLE_ROUTE,
                queryParams(param("value", value)));
            String response = getGetStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testByte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRouteWithQueryParams(
                PrimitiveQueryParamController.BYTE_ROUTE,
                queryParams(param("value", value)));
            String response = getGetStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testShort() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRouteWithQueryParams(
                PrimitiveQueryParamController.SHORT_ROUTE,
                queryParams(param("value", value)));
            String response = getGetStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testFloat() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRouteWithQueryParams(
                PrimitiveQueryParamController.FLOAT_ROUTE,
                queryParams(param("value", value)));
            String response = getGetStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testChar() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRouteWithQueryParams(
                PrimitiveQueryParamController.CHAR_ROUTE,
                queryParams(param("value", value)));
            String response = getGetStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testLong() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRouteWithQueryParams(
                PrimitiveQueryParamController.LONG_ROUTE,
                queryParams(param("value", value)));
            String response = getGetStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }
}
