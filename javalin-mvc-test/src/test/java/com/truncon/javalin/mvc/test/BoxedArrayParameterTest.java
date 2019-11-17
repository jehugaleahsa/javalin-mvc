package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.BoxedArrayParameterController;
import com.truncon.javalin.mvc.test.controllers.BoxedParameterController;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.truncon.javalin.mvc.test.QueryUtils.getGetJsonResponse;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

public final class BoxedArrayParameterTest {
    @Test
    public void testBoolean() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                BoxedArrayParameterController.BOOLEAN_ROUTE,
                pathParams(),
                queryParams(param("value", "false"), param("value", null), param("value", "true")));
            Boolean[] actual = getGetJsonResponse(route, Boolean[].class);
            Boolean[] expected = new Boolean[] { false, null, true };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testInteger() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                BoxedArrayParameterController.INTEGER_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Integer.toString(Integer.MIN_VALUE)),
                    param("value", null),
                    param("value", Integer.toString(Integer.MAX_VALUE))));
            Integer[] actual = getGetJsonResponse(route, Integer[].class);
            Integer[] expected = new Integer[] { Integer.MIN_VALUE, null, Integer.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testDouble() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                BoxedArrayParameterController.DOUBLE_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Double.toString(Double.MIN_VALUE)),
                    param("value", null),
                    param("value", Double.toString(Double.MAX_VALUE))));
            Double[] actual = getGetJsonResponse(route, Double[].class);
            Double[] expected = new Double[] { Double.MIN_VALUE, null, Double.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testByte() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                BoxedArrayParameterController.BYTE_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Byte.toString(Byte.MIN_VALUE)),
                    param("value", null),
                    param("value", Byte.toString(Byte.MAX_VALUE))));
            Byte[] actual = getGetJsonResponse(route, Byte[].class);
            Byte[] expected = new Byte[] { Byte.MIN_VALUE, null, Byte.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testShort() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                BoxedArrayParameterController.SHORT_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Short.toString(Short.MIN_VALUE)),
                    param("value", null),
                    param("value", Short.toString(Short.MAX_VALUE))));
            Short[] actual = getGetJsonResponse(route, Short[].class);
            Short[] expected = new Short[] { Short.MIN_VALUE, null, Short.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testFloat() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                BoxedArrayParameterController.FLOAT_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Float.toString(Float.MIN_VALUE)),
                    param("value", null),
                    param("value", Float.toString(Float.MAX_VALUE))));
            Float[] actual = getGetJsonResponse(route, Float[].class);
            Float[] expected = new Float[] { Float.MIN_VALUE, null, Float.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testChar() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                BoxedArrayParameterController.CHAR_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Character.toString(Character.MIN_VALUE)),
                    param("value", null),
                    param("value", Character.toString(Character.MAX_VALUE))));
            Character[] actual = getGetJsonResponse(route, Character[].class);
            Character[] expected = new Character[] { Character.MIN_VALUE, null, Character.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testLong() throws Exception {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(
                BoxedArrayParameterController.LONG_ROUTE,
                pathParams(),
                queryParams(
                    param("value", Long.toString(Long.MIN_VALUE)),
                    param("value", null),
                    param("value", Long.toString(Long.MAX_VALUE))));
            Long[] actual = getGetJsonResponse(route, Long[].class);
            Long[] expected = new Long[] { Long.MIN_VALUE, null, Long.MAX_VALUE };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testDate() throws Exception {
        AsyncTestUtils.runTest(app -> {
            Date value = DateUtils.truncate(new Date(), Calendar.SECOND);
            String route = buildRoute(
                BoxedArrayParameterController.DATE_ROUTE,
                pathParams(),
                queryParams(
                    param("value", BoxedParameterController.DATE_FORMAT.format(value)),
                    param("value", null)));
            Date[] actual = getGetJsonResponse(route, Date[].class);
            Date[] expected = new Date[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testInstant() throws Exception {
        AsyncTestUtils.runTest(app -> {
            Instant value = Instant.now();
            String route = buildRoute(
                BoxedArrayParameterController.INSTANT_ROUTE,
                pathParams(),
                queryParams(
                    param("value", BoxedParameterController.INSTANT_FORMAT.format(value)),
                    param("value", null)));
            Instant[] actual = getGetJsonResponse(route, Instant[].class);
            Instant[] expected = new Instant[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testZonedDateTime() throws Exception {
        AsyncTestUtils.runTest(app -> {
            // It appears Jackson normalizes all dates to UTC when round-tripping.
            // For now, I am just converting now to UTC to verify the behavior.
            ZonedDateTime value = ZonedDateTime.ofInstant(ZonedDateTime.now().toInstant(), ZoneId.of("UTC"));
            String route = buildRoute(
                BoxedArrayParameterController.ZONED_DATETIME_ROUTE,
                pathParams(),
                queryParams(
                    param("value", BoxedParameterController.ZONED_DATETIME_FORMAT.format(value)),
                    param("value", null)));
            ZonedDateTime[] actual = getGetJsonResponse(route, ZonedDateTime[].class);
            ZonedDateTime[] expected = new ZonedDateTime[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testOffsetDateTime() throws Exception {
        // It appears Jackson normalizes all dates to UTC when round-tripping.
        // For now, I am just converting now to UTC to verify the behavior.
        AsyncTestUtils.runTest(app -> {
            OffsetDateTime value = OffsetDateTime.ofInstant(OffsetDateTime.now().toInstant(), ZoneOffset.UTC);
            String route = buildRoute(
                BoxedArrayParameterController.OFFSET_DATETIME_ROUTE,
                pathParams(),
                queryParams(
                    param("value", BoxedParameterController.OFFSET_DATETIME_FORMAT.format(value)),
                    param("value", null)));
            OffsetDateTime[] actual = getGetJsonResponse(route, OffsetDateTime[].class);
            OffsetDateTime[] expected = new OffsetDateTime[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testLocalDateTime() throws Exception {
        AsyncTestUtils.runTest(app -> {
            LocalDateTime value = LocalDateTime.now();
            String route = buildRoute(
                BoxedArrayParameterController.LOCAL_DATETIME_ROUTE,
                pathParams(),
                queryParams(
                    param("value", BoxedParameterController.LOCAL_DATETIME_FORMAT.format(value)),
                    param("value", null)));
            LocalDateTime[] actual = getGetJsonResponse(route, LocalDateTime[].class);
            LocalDateTime[] expected = new LocalDateTime[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testLocalDate() throws Exception {
        AsyncTestUtils.runTest(app -> {
            LocalDate value = LocalDate.now();
            String route = buildRoute(
                BoxedArrayParameterController.LOCAL_DATE_ROUTE,
                pathParams(),
                queryParams(
                    param("value", BoxedParameterController.LOCAL_DATE_FORMAT.format(value)),
                    param("value", null)));
            LocalDate[] actual = getGetJsonResponse(route, LocalDate[].class);
            LocalDate[] expected = new LocalDate[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testBigInteger() throws Exception {
        AsyncTestUtils.runTest(app -> {
            BigInteger value = new BigInteger("12345678901234567890");
            String route = buildRoute(
                BoxedArrayParameterController.BIG_INTEGER_ROUTE,
                pathParams(),
                queryParams(
                    param("value", value.toString()),
                    param("value", null)));
            BigInteger[] actual = getGetJsonResponse(route, BigInteger[].class);
            BigInteger[] expected = new BigInteger[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testBigDecimal() throws Exception {
        AsyncTestUtils.runTest(app -> {
            BigDecimal value = new BigDecimal("12345678901234567890.123456789");
            String route = buildRoute(
                BoxedArrayParameterController.BIG_DECIMAL_ROUTE,
                pathParams(),
                queryParams(
                    param("value", value.toString()),
                    param("value", null)));
            BigDecimal[] actual = getGetJsonResponse(route, BigDecimal[].class);
            BigDecimal[] expected = new BigDecimal[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }

    @Test
    public void testUUID() throws Exception {
        AsyncTestUtils.runTest(app -> {
            UUID value = UUID.randomUUID();
            String route = buildRoute(
                BoxedArrayParameterController.UUID_ROUTE,
                pathParams(),
                queryParams(
                    param("value", value.toString()),
                    param("value", null)));
            UUID[] actual = getGetJsonResponse(route, UUID[].class);
            UUID[] expected = new UUID[] { value, null };
            Assert.assertArrayEquals(expected, actual);
        }).join();
    }
}
