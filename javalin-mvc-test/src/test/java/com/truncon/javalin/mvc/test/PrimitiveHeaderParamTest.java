package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveHeaderParamController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getGetStringResponseWithHeaders;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.headerParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;

public final class PrimitiveHeaderParamTest {
    @Test
    public void testBoolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getGetStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testInteger() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getGetStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDouble() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getGetStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testByte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getGetStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testShort() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getGetStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testFloat() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getGetStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testChar() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getGetStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testLong() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getGetStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }
}
