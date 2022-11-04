package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ObjectBodyController;
import com.truncon.javalin.mvc.test.models.BoxedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForPost;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForStringBodyPost;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

final class ObjectBodyTest {
    @Test
    void testPrimitiveModel() {
        AsyncTestUtils.runTest(app -> {
            PrimitiveModel model = getPrimitiveModel();
            String route = buildRoute(ObjectBodyController.PRIMITIVE_BODY_ROUTE);
            PrimitiveModel response = getJsonResponseForPost(route, model, PrimitiveModel.class);
            assertPrimitiveModel(model, response);
        });
    }

    @Test
    void testPrimitiveModel_explicit() {
        AsyncTestUtils.runTest(app -> {
            PrimitiveModel model = getPrimitiveModel();
            String route = buildRoute(ObjectBodyController.EXPLICIT_PRIMITIVE_BODY_ROUTE);
            PrimitiveModel response = getJsonResponseForPost(route, model, PrimitiveModel.class);
            assertPrimitiveModel(model, response);
        });
    }

    private static PrimitiveModel getPrimitiveModel() {
        PrimitiveModel model = new PrimitiveModel();
        model.setBoolean(true);
        model.setInteger(Integer.MAX_VALUE);
        model.setDouble(Double.MAX_VALUE);
        model.setByte(Byte.MAX_VALUE);
        model.setShort(Short.MAX_VALUE);
        model.setFloat(Float.MAX_VALUE);
        model.setChar(Character.MAX_VALUE);
        model.setLong(Long.MAX_VALUE);
        return model;
    }

    private static void assertPrimitiveModel(PrimitiveModel expected, PrimitiveModel actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getBoolean(), actual.getBoolean());
        Assertions.assertEquals(expected.getInteger(), actual.getInteger());
        Assertions.assertEquals(expected.getDouble(), actual.getDouble(), 1);
        Assertions.assertEquals(expected.getByte(), actual.getByte());
        Assertions.assertEquals(expected.getShort(), actual.getShort());
        Assertions.assertEquals(expected.getFloat(), actual.getFloat(), 1);
        Assertions.assertEquals(expected.getChar(), actual.getChar());
        Assertions.assertEquals(expected.getLong(), actual.getLong());
    }

    @Test
    void testBoxedModel() {
        AsyncTestUtils.runTest(app -> {
            BoxedModel model = getBoxedModel();
            String route = buildRoute(ObjectBodyController.BOXED_BODY_ROUTE);
            BoxedModel response = getJsonResponseForPost(route, model, BoxedModel.class);
            assertBoxedModel(model, response);
        });
    }

    @Test
    void testBoxedModel_explicit() {
        AsyncTestUtils.runTest(app -> {
            BoxedModel model = getBoxedModel();
            String route = buildRoute(ObjectBodyController.EXPLICIT_BOXED_BODY_ROUTE);
            BoxedModel response = getJsonResponseForPost(route, model, BoxedModel.class);
            assertBoxedModel(model, response);
        });
    }

    private void assertBoxedModel(BoxedModel expected, BoxedModel actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getString(), actual.getString());
        Assertions.assertEquals(expected.getBoolean(), actual.getBoolean());
        Assertions.assertEquals(expected.getInteger(), actual.getInteger());
        Assertions.assertEquals(expected.getDouble(), actual.getDouble());
        Assertions.assertEquals(expected.getByte(), actual.getByte());
        Assertions.assertEquals(expected.getShort(), actual.getShort());
        Assertions.assertEquals(expected.getFloat(), actual.getFloat());
        Assertions.assertEquals(expected.getCharacter(), actual.getCharacter());
        Assertions.assertEquals(expected.getLong(), actual.getLong());
        Assertions.assertEquals(expected.getDate(), actual.getDate());
        Assertions.assertEquals(expected.getInstant(), actual.getInstant());
        Assertions.assertEquals(expected.getZonedDateTime(), actual.getZonedDateTime());
        Assertions.assertEquals(expected.getOffsetDateTime(), actual.getOffsetDateTime());
        Assertions.assertEquals(expected.getLocalDateTime(), actual.getLocalDateTime());
        Assertions.assertEquals(expected.getLocalDate(), actual.getLocalDate());
        Assertions.assertEquals(expected.getYearMonth(), actual.getYearMonth());
        Assertions.assertEquals(expected.getYear(), actual.getYear());
        Assertions.assertEquals(expected.getBigInteger(), actual.getBigInteger());
        Assertions.assertEquals(expected.getBigDecimal(), actual.getBigDecimal());
        Assertions.assertEquals(expected.getUuid(), actual.getUuid());
    }

    private BoxedModel getBoxedModel() {
        BoxedModel model = new BoxedModel();
        model.setString("Hello");
        model.setBoolean(true);
        model.setInteger(Integer.MAX_VALUE);
        model.setDouble(Double.MAX_VALUE);
        model.setByte(Byte.MAX_VALUE);
        model.setShort(Short.MAX_VALUE);
        model.setFloat(Float.MAX_VALUE);
        model.setCharacter(Character.MAX_VALUE);
        model.setLong(Long.MAX_VALUE);
        model.setDate(new Date());
        model.setInstant(Instant.now());
        // It appears Jackson normalizes all dates to UTC when round-tripping.
        // For now, I am just converting now to UTC to verify the behavior.
        model.setZonedDateTime(ZonedDateTime.ofInstant(ZonedDateTime.now().toInstant(), ZoneOffset.UTC));
        model.setOffsetDateTime(OffsetDateTime.ofInstant(OffsetDateTime.now().toInstant(), ZoneOffset.UTC));
        model.setLocalDateTime(LocalDateTime.now());
        model.setLocalDate(LocalDate.now());
        model.setYearMonth(YearMonth.now());
        model.setYear(Year.now());
        model.setBigInteger(new BigInteger("12345678901234567890"));
        model.setBigDecimal(new BigDecimal("12345678901234567890.123"));
        model.setUuid(UUID.randomUUID());
        return model;
    }

    @Test
    void testInputStreamParameter() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(ObjectBodyController.INPUT_STREAM_BODY_ROUTE);
            String actual = getStringForStringBodyPost(route, "Hello, world!!!");
            Assertions.assertEquals("Hello, world!!!", actual);
        });
    }
}
