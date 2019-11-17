package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.BoxedParameterController;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.*;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringResponse;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

public final class BoxedParameterTest {
    @Test
    public void testString() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                BoxedParameterController.STRING_ROUTE,
                pathParams(param("value", "Hello")));
            String response = getStringResponse(route);
            Assert.assertEquals("Hello", response);
        }).join();
    }

    @Test
    public void testBoolean() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRoute(
                BoxedParameterController.BOOLEAN_ROUTE,
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
                BoxedParameterController.INTEGER_ROUTE,
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
                BoxedParameterController.DOUBLE_ROUTE,
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
                BoxedParameterController.BYTE_ROUTE,
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
                BoxedParameterController.SHORT_ROUTE,
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
                BoxedParameterController.FLOAT_ROUTE,
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
                BoxedParameterController.CHAR_ROUTE,
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
                BoxedParameterController.LONG_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testDate() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.DATE_FORMAT.format(new Date());
            String route = buildRoute(
                BoxedParameterController.DATE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testInstant() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.INSTANT_FORMAT.format(Instant.now());
            String route = buildRoute(
                BoxedParameterController.INSTANT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testZonedDateTime() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.ZONED_DATETIME_FORMAT.format(ZonedDateTime.now());
            String route = buildRoute(
                BoxedParameterController.ZONED_DATETIME_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testOffsetDateTime() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.OFFSET_DATETIME_FORMAT.format(OffsetDateTime.now());
            String route = buildRoute(
                BoxedParameterController.OFFSET_DATETIME_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testLocalDateTime() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.LOCAL_DATETIME_FORMAT.format(LocalDateTime.now());
            String route = buildRoute(
                BoxedParameterController.LOCAL_DATETIME_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testLocalDate() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.LOCAL_DATE_FORMAT.format(LocalDate.now());
            String route = buildRoute(
                BoxedParameterController.LOCAL_DATE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testBigInteger() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = new BigInteger("12345678901234567890").toString();
            String route = buildRoute(
                BoxedParameterController.BIG_INTEGER_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testBigDecimal() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = new BigDecimal("12345678901234567890.123456789").toString();
            String route = buildRoute(
                BoxedParameterController.BIG_DECIMAL_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }

    @Test
    public void testUUID() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String value = UUID.randomUUID().toString();
            String route = buildRoute(
                BoxedParameterController.UUID_ROUTE,
                pathParams(param("value", value)));
            String response = getStringResponse(route);
            Assert.assertEquals(value, response);
        }).join();
    }
}
