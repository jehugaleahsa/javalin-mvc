package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveHeaderParamController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringForHeadersAndDelete;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForHeadersAndGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForHeadersAndHead;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForHeadersAndOptions;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForHeadersAndPatch;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForHeadersAndPost;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForHeadersAndPut;
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
            String response = getStringForHeadersAndGet(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testGet_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testGet_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testGet_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testGet_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testGet_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testGet_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testGet_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringForHeadersAndGet(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    // endregion

    // region POST

    @Test
    public void testPost_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getStringForHeadersAndPost(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPost_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringForHeadersAndPost(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPost_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringForHeadersAndPost(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPost_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringForHeadersAndPost(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPost_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringForHeadersAndPost(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPost_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringForHeadersAndPost(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPost_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringForHeadersAndPost(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPost_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringForHeadersAndPost(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    // endregion

    // region PUT

    @Test
    public void testPut_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getStringForHeadersAndPut(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPut_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringForHeadersAndPut(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPut_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringForHeadersAndPut(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPut_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringForHeadersAndPut(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPut_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringForHeadersAndPut(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPut_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringForHeadersAndPut(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPut_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringForHeadersAndPut(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPut_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringForHeadersAndPut(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    // endregion

    // region PATCH

    @Test
    public void testPatch_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getStringForHeadersAndPatch(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPatch_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringForHeadersAndPatch(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPatch_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringForHeadersAndPatch(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPatch_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringForHeadersAndPatch(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPatch_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringForHeadersAndPatch(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPatch_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringForHeadersAndPatch(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPatch_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringForHeadersAndPatch(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testPatch_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringForHeadersAndPatch(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    // endregion

    // region DELETE

    @Test
    public void testDelete_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getStringForHeadersAndDelete(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testDelete_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringForHeadersAndDelete(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testDelete_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringForHeadersAndDelete(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testDelete_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringForHeadersAndDelete(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testDelete_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringForHeadersAndDelete(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testDelete_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringForHeadersAndDelete(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testDelete_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringForHeadersAndDelete(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testDelete_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringForHeadersAndDelete(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    // endregion

    // region HEAD

    @Test
    public void testHead_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getStringForHeadersAndHead(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testHead_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringForHeadersAndHead(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testHead_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringForHeadersAndHead(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testHead_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringForHeadersAndHead(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testHead_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringForHeadersAndHead(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testHead_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringForHeadersAndHead(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testHead_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringForHeadersAndHead(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testHead_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringForHeadersAndHead(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    // endregion

    // region OPTIONS

    @Test
    public void testOptions_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveHeaderParamController.BOOLEAN_ROUTE);
            String response = getStringForHeadersAndOptions(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testOptions_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.INTEGER_ROUTE);
            String response = getStringForHeadersAndOptions(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testOptions_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.DOUBLE_ROUTE);
            String response = getStringForHeadersAndOptions(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testOptions_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.BYTE_ROUTE);
            String response = getStringForHeadersAndOptions(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testOptions_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.SHORT_ROUTE);
            String response = getStringForHeadersAndOptions(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testOptions_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.FLOAT_ROUTE);
            String response = getStringForHeadersAndOptions(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testOptions_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveHeaderParamController.CHAR_ROUTE);
            String response = getStringForHeadersAndOptions(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testOptions_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveHeaderParamController.LONG_ROUTE);
            String response = getStringForHeadersAndOptions(route, headerParams(param("value", value)));
            Assert.assertEquals(value, response);
        });
    }

    // endregion
}
