package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.BoxedParameterController;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.*;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

public final class BoxedPathParamTest {
    @Test
    public void testString() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithPathParams(
                BoxedParameterController.STRING_ROUTE,
                pathParams(param("value", "Hello")));
            String response = getStringForGet(route);
            Assert.assertEquals("Hello", response);
        });
    }

    @Test
    public void testBoolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.BOOLEAN_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testInteger() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.INTEGER_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testDouble() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.DOUBLE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testByte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.BYTE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testShort() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.SHORT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testFloat() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.FLOAT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testChar() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRouteWithPathParams(
                BoxedParameterController.CHAR_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testLong() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.LONG_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testDate() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.DATE_FORMAT.format(new Date());
            String route = buildRouteWithPathParams(
                BoxedParameterController.DATE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testInstant() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.INSTANT_FORMAT.format(Instant.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.INSTANT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testZonedDateTime() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.ZONED_DATETIME_FORMAT.format(ZonedDateTime.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.ZONED_DATETIME_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testOffsetDateTime() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.OFFSET_DATETIME_FORMAT.format(OffsetDateTime.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.OFFSET_DATETIME_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testLocalDateTime() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.LOCAL_DATETIME_FORMAT.format(LocalDateTime.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.LOCAL_DATETIME_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testLocalDate() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.LOCAL_DATE_FORMAT.format(LocalDate.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.LOCAL_DATE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testYearMonth() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.YEAR_MONTH_FORMAT.format(YearMonth.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.YEAR_MONTH_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testYear() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.YEAR_FORMAT.format(YearMonth.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.YEAR_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testBigInteger() {
        AsyncTestUtils.runTest(app -> {
            String value = new BigInteger("12345678901234567890").toString();
            String route = buildRouteWithPathParams(
                BoxedParameterController.BIG_INTEGER_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testBigDecimal() {
        AsyncTestUtils.runTest(app -> {
            String value = new BigDecimal("12345678901234567890.123456789").toString();
            String route = buildRouteWithPathParams(
                BoxedParameterController.BIG_DECIMAL_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }

    @Test
    public void testUUID() {
        AsyncTestUtils.runTest(app -> {
            String value = UUID.randomUUID().toString();
            String route = buildRouteWithPathParams(
                BoxedParameterController.UUID_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assert.assertEquals(value, response);
        });
    }
}
