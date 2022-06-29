package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ObjectBodyController;
import com.truncon.javalin.mvc.test.models.BoxedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.*;
import java.util.Date;
import java.util.UUID;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForPost;
import static com.truncon.javalin.mvc.test.RouteBuilder.*;

public final class ObjectBodyTest {
    @Test
    public void testPrimitiveModel() {
        AsyncTestUtils.runTest(app -> {
            PrimitiveModel model = getPrimitiveModel();
            String route = buildRoute(ObjectBodyController.PRIMITIVE_BODY_ROUTE);
            PrimitiveModel response = getJsonResponseForPost(route, model, PrimitiveModel.class);
            assertPrimitiveModel(model, response);
        });
    }

    @Test
    public void testPrimitiveModel_explicit() {
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
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getBoolean(), actual.getBoolean());
        Assert.assertEquals(expected.getInteger(), actual.getInteger());
        Assert.assertEquals(expected.getDouble(), actual.getDouble(), 1);
        Assert.assertEquals(expected.getByte(), actual.getByte());
        Assert.assertEquals(expected.getShort(), actual.getShort());
        Assert.assertEquals(expected.getFloat(), actual.getFloat(), 1);
        Assert.assertEquals(expected.getChar(), actual.getChar());
        Assert.assertEquals(expected.getLong(), actual.getLong());
    }

    @Test
    public void testBoxedModel() {
        AsyncTestUtils.runTest(app -> {
            BoxedModel model = getBoxedModel();
            String route = buildRoute(ObjectBodyController.BOXED_BODY_ROUTE);
            BoxedModel response = getJsonResponseForPost(route, model, BoxedModel.class);
            assertBoxedModel(model, response);
        });
    }

    @Test
    public void testBoxedModel_explicit() {
        AsyncTestUtils.runTest(app -> {
            BoxedModel model = getBoxedModel();
            String route = buildRoute(ObjectBodyController.EXPLICIT_BOXED_BODY_ROUTE);
            BoxedModel response = getJsonResponseForPost(route, model, BoxedModel.class);
            assertBoxedModel(model, response);
        });
    }

    private void assertBoxedModel(BoxedModel expected, BoxedModel actual) {
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getString(), actual.getString());
        Assert.assertEquals(expected.getBoolean(), actual.getBoolean());
        Assert.assertEquals(expected.getInteger(), actual.getInteger());
        Assert.assertEquals(expected.getDouble(), actual.getDouble());
        Assert.assertEquals(expected.getByte(), actual.getByte());
        Assert.assertEquals(expected.getShort(), actual.getShort());
        Assert.assertEquals(expected.getFloat(), actual.getFloat());
        Assert.assertEquals(expected.getCharacter(), actual.getCharacter());
        Assert.assertEquals(expected.getLong(), actual.getLong());
        Assert.assertEquals(expected.getDate(), actual.getDate());
        Assert.assertEquals(expected.getInstant(), actual.getInstant());
        Assert.assertEquals(expected.getZonedDateTime(), actual.getZonedDateTime());
        Assert.assertEquals(expected.getOffsetDateTime(), actual.getOffsetDateTime());
        Assert.assertEquals(expected.getLocalDateTime(), actual.getLocalDateTime());
        Assert.assertEquals(expected.getLocalDate(), actual.getLocalDate());
        Assert.assertEquals(expected.getYearMonth(), actual.getYearMonth());
        Assert.assertEquals(expected.getYear(), actual.getYear());
        Assert.assertEquals(expected.getBigInteger(), actual.getBigInteger());
        Assert.assertEquals(expected.getBigDecimal(), actual.getBigDecimal());
        Assert.assertEquals(expected.getUuid(), actual.getUuid());
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
}
