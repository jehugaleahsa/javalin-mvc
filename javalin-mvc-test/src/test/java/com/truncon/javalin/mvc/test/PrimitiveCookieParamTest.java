package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveCookieParamController;
import com.truncon.javalin.mvc.test.controllers.PrimitiveHeaderParamController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getGetStringResponseWithCookies;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.cookieParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;

public final class PrimitiveCookieParamTest {
    @Test
    public void testBoolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveCookieParamController.BOOLEAN_ROUTE);
            String response = getGetStringResponseWithCookies(route, cookieParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testInteger() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.INTEGER_ROUTE);
            String response = getGetStringResponseWithCookies(route, cookieParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDouble() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.DOUBLE_ROUTE);
            String response = getGetStringResponseWithCookies(route, cookieParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testByte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.BYTE_ROUTE);
            String response = getGetStringResponseWithCookies(route, cookieParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testShort() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.SHORT_ROUTE);
            String response = getGetStringResponseWithCookies(route, cookieParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testFloat() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.FLOAT_ROUTE);
            String response = getGetStringResponseWithCookies(route, cookieParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testChar() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveCookieParamController.CHAR_ROUTE);
            String response = getGetStringResponseWithCookies(route, cookieParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testLong() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.LONG_ROUTE);
            String response = getGetStringResponseWithCookies(route, cookieParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }
}
