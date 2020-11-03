package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveHeaderParamController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.deleteStringResponseWithHeaders;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringResponseWithHeaders;
import static com.truncon.javalin.mvc.test.QueryUtils.headStringResponseWithHeaders;
import static com.truncon.javalin.mvc.test.QueryUtils.optionsStringResponseWithHeaders;
import static com.truncon.javalin.mvc.test.QueryUtils.patchStringResponseWithHeaders;
import static com.truncon.javalin.mvc.test.QueryUtils.postStringResponseWithHeaders;
import static com.truncon.javalin.mvc.test.QueryUtils.putStringResponseWithHeaders;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.headerParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;

public final class PrimitiveHeaderParamTest {
    // region GET

    @Test
    public void testGet_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testGet_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testGet_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testGet_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testGet_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testGet_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testGet_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testGet_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    // endregion

    // region POST

    @Test
    public void testPost_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = postStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPost_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = postStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPost_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = postStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPost_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = postStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPost_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = postStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPost_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = postStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPost_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = postStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPost_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = postStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    // endregion

    // region PUT

    @Test
    public void testPut_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = putStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPut_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = putStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPut_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = putStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPut_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = putStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPut_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = putStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPut_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = putStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPut_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = putStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPut_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = putStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    // endregion

    // region PATCH

    @Test
    public void testPatch_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = patchStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPatch_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = patchStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPatch_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = patchStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPatch_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = patchStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPatch_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = patchStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPatch_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = patchStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPatch_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = patchStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testPatch_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = patchStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    // endregion

    // region DELETE

    @Test
    public void testDelete_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = deleteStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDelete_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = deleteStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDelete_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = deleteStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDelete_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = deleteStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDelete_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = deleteStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDelete_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = deleteStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDelete_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = deleteStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDelete_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = deleteStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    // endregion

    // region HEAD

    @Test
    public void testHead_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = headStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testHead_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = headStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testHead_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = headStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testHead_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = headStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testHead_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = headStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testHead_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = headStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testHead_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = headStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testHead_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = headStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    // endregion

    // region OPTIONS

    @Test
    public void testOptions_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = optionsStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testOptions_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = optionsStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testOptions_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = optionsStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testOptions_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = optionsStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testOptions_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = optionsStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testOptions_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = optionsStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testOptions_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = optionsStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testOptions_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = optionsStringResponseWithHeaders(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        }).join();
    }

    // endregion
}
