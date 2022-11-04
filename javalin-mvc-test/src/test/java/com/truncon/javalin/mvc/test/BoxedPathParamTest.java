package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.BoxedParameterController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.*;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

final class BoxedPathParamTest {
    @Test
    void testString() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithPathParams(
                BoxedParameterController.STRING_ROUTE,
                pathParams(param("value", "Hello")));
            String response = getStringForGet(route);
            Assertions.assertEquals("Hello", response);
        });
    }

    @Test
    void testBoolean() {
        AsyncTestUtils.runTest(app -> {
            String value = Boolean.toString(Boolean.TRUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.BOOLEAN_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testInteger() {
        AsyncTestUtils.runTest(app -> {
            String value = Integer.toString(Integer.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.INTEGER_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDouble() {
        AsyncTestUtils.runTest(app -> {
            String value = Double.toString(Double.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.DOUBLE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testByte() {
        AsyncTestUtils.runTest(app -> {
            String value = Byte.toString(Byte.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.BYTE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testShort() {
        AsyncTestUtils.runTest(app -> {
            String value = Short.toString(Short.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.SHORT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testFloat() {
        AsyncTestUtils.runTest(app -> {
            String value = Float.toString(Float.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.FLOAT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testChar() {
        AsyncTestUtils.runTest(app -> {
            String value = "a";
            String route = buildRouteWithPathParams(
                BoxedParameterController.CHAR_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testLong() {
        AsyncTestUtils.runTest(app -> {
            String value = Long.toString(Long.MAX_VALUE);
            String route = buildRouteWithPathParams(
                BoxedParameterController.LONG_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testDate() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.DATE_FORMAT.format(new Date());
            String route = buildRouteWithPathParams(
                BoxedParameterController.DATE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testInstant() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.INSTANT_FORMAT.format(Instant.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.INSTANT_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testZonedDateTime() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.ZONED_DATETIME_FORMAT.format(ZonedDateTime.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.ZONED_DATETIME_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testOffsetDateTime() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.OFFSET_DATETIME_FORMAT.format(OffsetDateTime.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.OFFSET_DATETIME_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testLocalDateTime() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.LOCAL_DATETIME_FORMAT.format(LocalDateTime.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.LOCAL_DATETIME_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testLocalDate() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.LOCAL_DATE_FORMAT.format(LocalDate.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.LOCAL_DATE_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testYearMonth() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.YEAR_MONTH_FORMAT.format(YearMonth.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.YEAR_MONTH_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testYear() {
        AsyncTestUtils.runTest(app -> {
            String value = BoxedParameterController.YEAR_FORMAT.format(YearMonth.now());
            String route = buildRouteWithPathParams(
                BoxedParameterController.YEAR_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testBigInteger() {
        AsyncTestUtils.runTest(app -> {
            String value = new BigInteger("12345678901234567890").toString();
            String route = buildRouteWithPathParams(
                BoxedParameterController.BIG_INTEGER_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testBigDecimal() {
        AsyncTestUtils.runTest(app -> {
            String value = new BigDecimal("12345678901234567890.123456789").toString();
            String route = buildRouteWithPathParams(
                BoxedParameterController.BIG_DECIMAL_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }

    @Test
    void testUUID() {
        AsyncTestUtils.runTest(app -> {
            String value = UUID.randomUUID().toString();
            String route = buildRouteWithPathParams(
                BoxedParameterController.UUID_ROUTE,
                pathParams(param("value", value)));
            String response = getStringForGet(route);
            Assertions.assertEquals(value, response);
        });
    }
}
