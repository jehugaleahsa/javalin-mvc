package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveCookieParamController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringForCookiesAndDelete;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForCookiesAndGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForCookiesAndHead;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForCookiesAndOptions;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForCookiesAndPatch;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForCookiesAndPost;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForCookiesAndPut;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.cookieParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;

final class PrimitiveCookieParamTest {
    // region GET

    @Test
    void testGet_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveCookieParamController.BOOLEAN_ROUTE);
            String response = getStringForCookiesAndGet(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.INTEGER_ROUTE);
            String response = getStringForCookiesAndGet(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.DOUBLE_ROUTE);
            String response = getStringForCookiesAndGet(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.BYTE_ROUTE);
            String response = getStringForCookiesAndGet(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.SHORT_ROUTE);
            String response = getStringForCookiesAndGet(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.FLOAT_ROUTE);
            String response = getStringForCookiesAndGet(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveCookieParamController.CHAR_ROUTE);
            String response = getStringForCookiesAndGet(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.LONG_ROUTE);
            String response = getStringForCookiesAndGet(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region POST

    @Test
    void testPost_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveCookieParamController.BOOLEAN_ROUTE);
            String response = getStringForCookiesAndPost(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.INTEGER_ROUTE);
            String response = getStringForCookiesAndPost(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.DOUBLE_ROUTE);
            String response = getStringForCookiesAndPost(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.BYTE_ROUTE);
            String response = getStringForCookiesAndPost(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.SHORT_ROUTE);
            String response = getStringForCookiesAndPost(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.FLOAT_ROUTE);
            String response = getStringForCookiesAndPost(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveCookieParamController.CHAR_ROUTE);
            String response = getStringForCookiesAndPost(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.LONG_ROUTE);
            String response = getStringForCookiesAndPost(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region PUT

    @Test
    void testPut_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveCookieParamController.BOOLEAN_ROUTE);
            String response = getStringForCookiesAndPut(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.INTEGER_ROUTE);
            String response = getStringForCookiesAndPut(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.DOUBLE_ROUTE);
            String response = getStringForCookiesAndPut(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.BYTE_ROUTE);
            String response = getStringForCookiesAndPut(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.SHORT_ROUTE);
            String response = getStringForCookiesAndPut(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.FLOAT_ROUTE);
            String response = getStringForCookiesAndPut(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveCookieParamController.CHAR_ROUTE);
            String response = getStringForCookiesAndPut(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.LONG_ROUTE);
            String response = getStringForCookiesAndPut(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region PATCH

    @Test
    void testPatch_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveCookieParamController.BOOLEAN_ROUTE);
            String response = getStringForCookiesAndPatch(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.INTEGER_ROUTE);
            String response = getStringForCookiesAndPatch(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.DOUBLE_ROUTE);
            String response = getStringForCookiesAndPatch(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.BYTE_ROUTE);
            String response = getStringForCookiesAndPatch(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.SHORT_ROUTE);
            String response = getStringForCookiesAndPatch(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.FLOAT_ROUTE);
            String response = getStringForCookiesAndPatch(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveCookieParamController.CHAR_ROUTE);
            String response = getStringForCookiesAndPatch(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.LONG_ROUTE);
            String response = getStringForCookiesAndPatch(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region DELETE

    @Test
    void testDelete_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveCookieParamController.BOOLEAN_ROUTE);
            String response = getStringForCookiesAndDelete(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.INTEGER_ROUTE);
            String response = getStringForCookiesAndDelete(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.DOUBLE_ROUTE);
            String response = getStringForCookiesAndDelete(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.BYTE_ROUTE);
            String response = getStringForCookiesAndDelete(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.SHORT_ROUTE);
            String response = getStringForCookiesAndDelete(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.FLOAT_ROUTE);
            String response = getStringForCookiesAndDelete(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveCookieParamController.CHAR_ROUTE);
            String response = getStringForCookiesAndDelete(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.LONG_ROUTE);
            String response = getStringForCookiesAndDelete(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region HEAD

    @Test
    void testHead_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveCookieParamController.BOOLEAN_ROUTE);
            String response = getStringForCookiesAndHead(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.INTEGER_ROUTE);
            String response = getStringForCookiesAndHead(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.DOUBLE_ROUTE);
            String response = getStringForCookiesAndHead(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.BYTE_ROUTE);
            String response = getStringForCookiesAndHead(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.SHORT_ROUTE);
            String response = getStringForCookiesAndHead(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.FLOAT_ROUTE);
            String response = getStringForCookiesAndHead(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveCookieParamController.CHAR_ROUTE);
            String response = getStringForCookiesAndHead(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.LONG_ROUTE);
            String response = getStringForCookiesAndHead(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region OPTIONS

    @Test
    void testOptions_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveCookieParamController.BOOLEAN_ROUTE);
            String response = getStringForCookiesAndOptions(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.INTEGER_ROUTE);
            String response = getStringForCookiesAndOptions(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.DOUBLE_ROUTE);
            String response = getStringForCookiesAndOptions(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.BYTE_ROUTE);
            String response = getStringForCookiesAndOptions(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.SHORT_ROUTE);
            String response = getStringForCookiesAndOptions(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.FLOAT_ROUTE);
            String response = getStringForCookiesAndOptions(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveCookieParamController.CHAR_ROUTE);
            String response = getStringForCookiesAndOptions(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveCookieParamController.LONG_ROUTE);
            String response = getStringForCookiesAndOptions(route, cookieParams(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion
}
