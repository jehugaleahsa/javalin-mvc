package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.BoxedArrayParameterController;
import com.truncon.javalin.mvc.test.controllers.BoxedParameterController;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

public final class BoxedArrayQueryParamTest {
    @Test
    public void testBoolean() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.BOOLEAN_ROUTE,
                queryParams(param("value", "false"), param("value", null), param("value", "true")));
            Boolean[] actual = getJsonResponseForGet(route, Boolean[].class);
            Boolean[] expected = new Boolean[] { false, null, true };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testInteger() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.INTEGER_ROUTE,
                queryParams(
                    param("value", Integer.toString(Integer.MIN_VALUE)),
                    param("value", null),
                    param("value", Integer.toString(Integer.MAX_VALUE))));
            Integer[] actual = getJsonResponseForGet(route, Integer[].class);
            Integer[] expected = new Integer[] { Integer.MIN_VALUE, null, Integer.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testDouble() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.DOUBLE_ROUTE,
                queryParams(
                    param("value", Double.toString(Double.MIN_VALUE)),
                    param("value", null),
                    param("value", Double.toString(Double.MAX_VALUE))));
            Double[] actual = getJsonResponseForGet(route, Double[].class);
            Double[] expected = new Double[] { Double.MIN_VALUE, null, Double.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testByte() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.BYTE_ROUTE,
                queryParams(
                    param("value", Byte.toString(Byte.MIN_VALUE)),
                    param("value", null),
                    param("value", Byte.toString(Byte.MAX_VALUE))));
            Byte[] actual = getJsonResponseForGet(route, Byte[].class);
            Byte[] expected = new Byte[] { Byte.MIN_VALUE, null, Byte.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testShort() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.SHORT_ROUTE,
                queryParams(
                    param("value", Short.toString(Short.MIN_VALUE)),
                    param("value", null),
                    param("value", Short.toString(Short.MAX_VALUE))));
            Short[] actual = getJsonResponseForGet(route, Short[].class);
            Short[] expected = new Short[] { Short.MIN_VALUE, null, Short.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testFloat() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.FLOAT_ROUTE,
                queryParams(
                    param("value", Float.toString(Float.MIN_VALUE)),
                    param("value", null),
                    param("value", Float.toString(Float.MAX_VALUE))));
            Float[] actual = getJsonResponseForGet(route, Float[].class);
            Float[] expected = new Float[] { Float.MIN_VALUE, null, Float.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testChar() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.CHAR_ROUTE,
                queryParams(
                    param("value", Character.toString(Character.MIN_VALUE)),
                    param("value", null),
                    param("value", Character.toString(Character.MAX_VALUE))));
            Character[] actual = getJsonResponseForGet(route, Character[].class);
            Character[] expected = new Character[] { Character.MIN_VALUE, null, Character.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testLong() {
        AsyncTestUtils.runTest(app -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.LONG_ROUTE,
                queryParams(
                    param("value", Long.toString(Long.MIN_VALUE)),
                    param("value", null),
                    param("value", Long.toString(Long.MAX_VALUE))));
            Long[] actual = getJsonResponseForGet(route, Long[].class);
            Long[] expected = new Long[] { Long.MIN_VALUE, null, Long.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testDate() {
        AsyncTestUtils.runTest(app -> {
            Date value = DateUtils.truncate(new Date(), Calendar.SECOND);
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.DATE_ROUTE,
                queryParams(
                    param("value", BoxedParameterController.DATE_FORMAT.format(value)),
                    param("value", null)));
            Date[] actual = getJsonResponseForGet(route, Date[].class);
            Date[] expected = new Date[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testInstant() {
        AsyncTestUtils.runTest(app -> {
            Instant value = Instant.now();
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.INSTANT_ROUTE,
                queryParams(
                    param("value", BoxedParameterController.INSTANT_FORMAT.format(value)),
                    param("value", null)));
            Instant[] actual = getJsonResponseForGet(route, Instant[].class);
            Instant[] expected = new Instant[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testZonedDateTime() {
        AsyncTestUtils.runTest(app -> {
            // It appears Jackson normalizes all dates to UTC when round-tripping.
            // For now, I am just converting now to UTC to verify the behavior.
            ZonedDateTime value = ZonedDateTime.of(2022, 6, 28, 22, 8, 0, 0, ZoneOffset.UTC);
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.ZONED_DATETIME_ROUTE,
                queryParams(
                    param("value", BoxedParameterController.ZONED_DATETIME_FORMAT.format(value)),
                    param("value", null)));
            ZonedDateTime[] actual = getJsonResponseForGet(route, ZonedDateTime[].class);
            ZonedDateTime[] expected = new ZonedDateTime[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testOffsetDateTime() {
        // It appears Jackson normalizes all dates to UTC when round-tripping.
        // For now, I am just converting now to UTC to verify the behavior.
        AsyncTestUtils.runTest(app -> {
            OffsetDateTime value = OffsetDateTime.ofInstant(OffsetDateTime.now().toInstant(), ZoneOffset.UTC);
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.OFFSET_DATETIME_ROUTE,
                queryParams(
                    param("value", BoxedParameterController.OFFSET_DATETIME_FORMAT.format(value)),
                    param("value", null)));
            OffsetDateTime[] actual = getJsonResponseForGet(route, OffsetDateTime[].class);
            OffsetDateTime[] expected = new OffsetDateTime[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testLocalDateTime() {
        AsyncTestUtils.runTest(app -> {
            LocalDateTime value = LocalDateTime.now();
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.LOCAL_DATETIME_ROUTE,
                queryParams(
                    param("value", BoxedParameterController.LOCAL_DATETIME_FORMAT.format(value)),
                    param("value", null)));
            LocalDateTime[] actual = getJsonResponseForGet(route, LocalDateTime[].class);
            LocalDateTime[] expected = new LocalDateTime[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testLocalDate() {
        AsyncTestUtils.runTest(app -> {
            LocalDate value = LocalDate.now();
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.LOCAL_DATE_ROUTE,
                queryParams(
                    param("value", BoxedParameterController.LOCAL_DATE_FORMAT.format(value)),
                    param("value", null)));
            LocalDate[] actual = getJsonResponseForGet(route, LocalDate[].class);
            LocalDate[] expected = new LocalDate[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testBigInteger() {
        AsyncTestUtils.runTest(app -> {
            BigInteger value = new BigInteger("12345678901234567890");
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.BIG_INTEGER_ROUTE,
                queryParams(
                    param("value", value.toString()),
                    param("value", null)));
            BigInteger[] actual = getJsonResponseForGet(route, BigInteger[].class);
            BigInteger[] expected = new BigInteger[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testBigDecimal() {
        AsyncTestUtils.runTest(app -> {
            BigDecimal value = new BigDecimal("12345678901234567890.123456789");
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.BIG_DECIMAL_ROUTE,
                queryParams(
                    param("value", value.toString()),
                    param("value", null)));
            BigDecimal[] actual = getJsonResponseForGet(route, BigDecimal[].class);
            BigDecimal[] expected = new BigDecimal[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        });
    }

    @Test
    public void testUUID() {
        AsyncTestUtils.runTest(app -> {
            UUID value = UUID.randomUUID();
            String route = RouteBuilder.buildRouteWithQueryParams(
                BoxedArrayParameterController.UUID_ROUTE,
                queryParams(
                    param("value", value.toString()),
                    param("value", null)));
            UUID[] actual = getJsonResponseForGet(route, UUID[].class);
            UUID[] expected = new UUID[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        });
    }
}
