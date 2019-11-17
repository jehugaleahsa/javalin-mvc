package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveParameterController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringResponse;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

public final class PrimitiveParameterTest {
    @Test
    public void testBoolean() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(
                PrimitiveParameterController.BOOLEAN_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testInteger() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(
                PrimitiveParameterController.INTEGER_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDouble() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(
                PrimitiveParameterController.DOUBLE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }



    @Test
    public void testByte() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(
                PrimitiveParameterController.BYTE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testShort() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(
                PrimitiveParameterController.SHORT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testFloat() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(
                PrimitiveParameterController.FLOAT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testChar() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(
                PrimitiveParameterController.CHAR_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testLong() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(
                PrimitiveParameterController.LONG_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }
}
