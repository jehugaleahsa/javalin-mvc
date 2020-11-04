package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitivePathParamController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

public final class PrimitivePathParamTest {
    @Test
    public void testBoolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRouteWithPathParams(
                PrimitivePathParamController.BOOLEAN_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testInteger() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRouteWithPathParams(
                PrimitivePathParamController.INTEGER_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDouble() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRouteWithPathParams(
                PrimitivePathParamController.DOUBLE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testByte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRouteWithPathParams(
                PrimitivePathParamController.BYTE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testShort() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRouteWithPathParams(
                PrimitivePathParamController.SHORT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testFloat() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRouteWithPathParams(
                PrimitivePathParamController.FLOAT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testChar() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRouteWithPathParams(
                PrimitivePathParamController.CHAR_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testLong() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRouteWithPathParams(
                PrimitivePathParamController.LONG_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        }).join();
    }
}
