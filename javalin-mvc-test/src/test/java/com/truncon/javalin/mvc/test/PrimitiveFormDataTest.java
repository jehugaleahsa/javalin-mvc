package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveFormDataController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringForFormDataAndDelete;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForFormDataAndGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForFormDataAndHead;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForFormDataAndOptions;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForFormDataAndPatch;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForFormDataAndPost;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForFormDataAndPut;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.formData;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;

final class PrimitiveFormDataTest {
    // region GET

    @Test
    void testGet_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveFormDataController.BOOLEAN_ROUTE);
            String response = getStringForFormDataAndGet(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.INTEGER_ROUTE);
            String response = getStringForFormDataAndGet(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.DOUBLE_ROUTE);
            String response = getStringForFormDataAndGet(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.BYTE_ROUTE);
            String response = getStringForFormDataAndGet(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.SHORT_ROUTE);
            String response = getStringForFormDataAndGet(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.FLOAT_ROUTE);
            String response = getStringForFormDataAndGet(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveFormDataController.CHAR_ROUTE);
            String response = getStringForFormDataAndGet(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testGet_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.LONG_ROUTE);
            String response = getStringForFormDataAndGet(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region POST

    @Test
    void testPost_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveFormDataController.BOOLEAN_ROUTE);
            String response = getStringForFormDataAndPost(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.INTEGER_ROUTE);
            String response = getStringForFormDataAndPost(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.DOUBLE_ROUTE);
            String response = getStringForFormDataAndPost(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.BYTE_ROUTE);
            String response = getStringForFormDataAndPost(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.SHORT_ROUTE);
            String response = getStringForFormDataAndPost(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.FLOAT_ROUTE);
            String response = getStringForFormDataAndPost(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveFormDataController.CHAR_ROUTE);
            String response = getStringForFormDataAndPost(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPost_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.LONG_ROUTE);
            String response = getStringForFormDataAndPost(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region PUT

    @Test
    void testPut_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveFormDataController.BOOLEAN_ROUTE);
            String response = getStringForFormDataAndPut(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.INTEGER_ROUTE);
            String response = getStringForFormDataAndPut(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.DOUBLE_ROUTE);
            String response = getStringForFormDataAndPut(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.BYTE_ROUTE);
            String response = getStringForFormDataAndPut(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.SHORT_ROUTE);
            String response = getStringForFormDataAndPut(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.FLOAT_ROUTE);
            String response = getStringForFormDataAndPut(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveFormDataController.CHAR_ROUTE);
            String response = getStringForFormDataAndPut(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPut_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.LONG_ROUTE);
            String response = getStringForFormDataAndPut(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region PATCH

    @Test
    void testPatch_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveFormDataController.BOOLEAN_ROUTE);
            String response = getStringForFormDataAndPatch(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.INTEGER_ROUTE);
            String response = getStringForFormDataAndPatch(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.DOUBLE_ROUTE);
            String response = getStringForFormDataAndPatch(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.BYTE_ROUTE);
            String response = getStringForFormDataAndPatch(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.SHORT_ROUTE);
            String response = getStringForFormDataAndPatch(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.FLOAT_ROUTE);
            String response = getStringForFormDataAndPatch(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveFormDataController.CHAR_ROUTE);
            String response = getStringForFormDataAndPatch(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testPatch_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.LONG_ROUTE);
            String response = getStringForFormDataAndPatch(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region DELETE

    @Test
    void testDelete_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveFormDataController.BOOLEAN_ROUTE);
            String response = getStringForFormDataAndDelete(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.INTEGER_ROUTE);
            String response = getStringForFormDataAndDelete(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.DOUBLE_ROUTE);
            String response = getStringForFormDataAndDelete(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.BYTE_ROUTE);
            String response = getStringForFormDataAndDelete(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.SHORT_ROUTE);
            String response = getStringForFormDataAndDelete(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.FLOAT_ROUTE);
            String response = getStringForFormDataAndDelete(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveFormDataController.CHAR_ROUTE);
            String response = getStringForFormDataAndDelete(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDelete_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.LONG_ROUTE);
            String response = getStringForFormDataAndDelete(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region HEAD

    @Test
    void testHead_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveFormDataController.BOOLEAN_ROUTE);
            String response = getStringForFormDataAndHead(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.INTEGER_ROUTE);
            String response = getStringForFormDataAndHead(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.DOUBLE_ROUTE);
            String response = getStringForFormDataAndHead(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.BYTE_ROUTE);
            String response = getStringForFormDataAndHead(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.SHORT_ROUTE);
            String response = getStringForFormDataAndHead(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.FLOAT_ROUTE);
            String response = getStringForFormDataAndHead(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveFormDataController.CHAR_ROUTE);
            String response = getStringForFormDataAndHead(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testHead_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.LONG_ROUTE);
            String response = getStringForFormDataAndHead(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion

    // region OPTIONS

    @Test
    void testOptions_boolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveFormDataController.BOOLEAN_ROUTE);
            String response = getStringForFormDataAndOptions(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_int() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.INTEGER_ROUTE);
            String response = getStringForFormDataAndOptions(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_double() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.DOUBLE_ROUTE);
            String response = getStringForFormDataAndOptions(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_byte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.BYTE_ROUTE);
            String response = getStringForFormDataAndOptions(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_short() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.SHORT_ROUTE);
            String response = getStringForFormDataAndOptions(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_float() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.FLOAT_ROUTE);
            String response = getStringForFormDataAndOptions(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_char() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveFormDataController.CHAR_ROUTE);
            String response = getStringForFormDataAndOptions(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOptions_long() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.LONG_ROUTE);
            String response = getStringForFormDataAndOptions(route, formData(param("value", value)));
            Assertions.assertEquals(value, response);
        });
    }

    // endregion
}
