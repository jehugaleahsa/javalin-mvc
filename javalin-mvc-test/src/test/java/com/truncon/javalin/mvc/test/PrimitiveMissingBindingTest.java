package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveHeaderParamController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringResponseWithHeaders;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.headerParams;

public final class PrimitiveMissingBindingTest {
    @Test
    public void testGet_headerParam_integer() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams());
            Assert.assertEquals(Integer.toString(0), response);
        }).join();
    }

    @Test
    public void testGet_headerParam_long() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams());
            Assert.assertEquals(Long.toString(0L), response);
        }).join();
    }

    @Test
    public void testGet_headerParam_short() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams());
            Assert.assertEquals(Short.toString((short) 0), response);
        }).join();
    }

    @Test
    public void testGet_headerParam_byte() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams());
            Assert.assertEquals(Byte.toString((byte) 0), response);
        }).join();
    }

    @Test
    public void testGet_headerParam_boolean() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams());
            Assert.assertEquals(Boolean.toString(false), response);
        }).join();
    }

    @Test
    public void testGet_headerParam_char() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams());
            Assert.assertEquals(Character.toString('\0'), response);
        }).join();
    }

    @Test
    public void testGet_headerParam_float() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams());
            Assert.assertEquals(Float.toString(0.0f), response);
        }).join();
    }

    @Test
    public void testGet_headerParam_double() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams());
            Assert.assertEquals(Double.toString(0.0), response);
        }).join();
    }
}
