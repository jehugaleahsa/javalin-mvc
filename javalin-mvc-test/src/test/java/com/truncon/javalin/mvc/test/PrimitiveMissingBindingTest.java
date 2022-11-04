package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveHeaderParamController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringForHeadersAndGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.headerParams;

final class PrimitiveMissingBindingTest {
    @Test
    void testGet_headerParam_integer() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams());
            Assertions.assertEquals(Integer.toString(0), response);
        });
    }

    @Test
    void testGet_headerParam_long() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams());
            Assertions.assertEquals(Long.toString(0L), response);
        });
    }

    @Test
    void testGet_headerParam_short() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams());
            Assertions.assertEquals(Short.toString((short) 0), response);
        });
    }

    @Test
    void testGet_headerParam_byte() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams());
            Assertions.assertEquals(Byte.toString((byte) 0), response);
        });
    }

    @Test
    void testGet_headerParam_boolean() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams());
            Assertions.assertEquals(Boolean.toString(false), response);
        });
    }

    @Test
    void testGet_headerParam_char() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams());
            Assertions.assertEquals(Character.toString('\0'), response);
        });
    }

    @Test
    void testGet_headerParam_float() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams());
            Assertions.assertEquals(Float.toString(0.0f), response);
        });
    }

    @Test
    void testGet_headerParam_double() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams());
            Assertions.assertEquals(Double.toString(0.0), response);
        });
    }
}
