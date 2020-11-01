package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.PrimitiveFormDataController;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getGetStringResponseWithFormData;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.formData;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;

public final class PrimitiveFormDataTest {
    @Test
    public void testBoolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(PrimitiveFormDataController.BOOLEAN_ROUTE);
            String response = getGetStringResponseWithFormData(route, formData(
                param("value", Boolean.toString(Boolean.TRUE))
            ));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testInteger() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.INTEGER_ROUTE);
            String response = getGetStringResponseWithFormData(route,formData(
                param("value", Integer.toString(Integer.MAX_VALUE))
            ));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDouble() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.DOUBLE_ROUTE);
            String response = getGetStringResponseWithFormData(route, formData(
                param("value", Double.toString(Double.MAX_VALUE))
            ));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testByte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.BYTE_ROUTE);
            String response = getGetStringResponseWithFormData(route, formData(
                param("value", Byte.toString(Byte.MAX_VALUE))
            ));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testShort() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.SHORT_ROUTE);
            String response = getGetStringResponseWithFormData(route, formData(
                param("value", Short.toString(Short.MAX_VALUE))
            ));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testFloat() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.FLOAT_ROUTE);
            String response = getGetStringResponseWithFormData(route, formData(
                param("value", Float.toString(Float.MAX_VALUE))
            ));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testChar() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRoute(PrimitiveFormDataController.CHAR_ROUTE);
            String response = getGetStringResponseWithFormData(route, formData(
                param("value", "a")
            ));
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testLong() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRoute(PrimitiveFormDataController.LONG_ROUTE);
            String response = getGetStringResponseWithFormData(route, formData(
                param("value", Long.toString(Long.MAX_VALUE))
            ));
            Assert.assertEquals(value, response);
        }).join();
    }
}
