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
            PrimitiveModel model = new PrimitiveModel();
            model.setBoolean(true);
            model.setInteger(Integer.MAX_VALUE);
            model.setDouble(Double.MAX_VALUE);
            model.setByte(Byte.MAX_VALUE);
            model.setShort(Short.MAX_VALUE);
            model.setFloat(Float.MAX_VALUE);
            model.setChar(Character.MAX_VALUE);
            model.setLong(Long.MAX_VALUE);
            String route = buildRoute(ObjectBodyController.PRIMITIVE_BODY_ROUTE);
            PrimitiveModel response = getJsonResponseForPost(route, model, PrimitiveModel.class);
            Assert.assertEquals(model.getBoolean(), response.getBoolean());
            Assert.assertEquals(model.getInteger(), response.getInteger());
            Assert.assertEquals(model.getDouble(), response.getDouble(), 1);
            Assert.assertEquals(model.getByte(), response.getByte());
            Assert.assertEquals(model.getShort(), response.getShort());
            Assert.assertEquals(model.getFloat(), response.getFloat(), 1);
            Assert.assertEquals(model.getChar(), response.getChar());
            Assert.assertEquals(model.getLong(), response.getLong());
        }).join();
    }

    @Test
    public void testBoxedModel() {
        AsyncTestUtils.runTest(app -> {
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
            model.setZonedDateTime(ZonedDateTime.ofInstant(ZonedDateTime.now().toInstant(), ZoneId.of("UTC")));
            model.setOffsetDateTime(OffsetDateTime.ofInstant(OffsetDateTime.now().toInstant(), ZoneOffset.UTC));
            model.setLocalDateTime(LocalDateTime.now());
            model.setLocalDate(LocalDate.now());
            model.setBigInteger(new BigInteger("12345678901234567890"));
            model.setBigDecimal(new BigDecimal("12345678901234567890.123"));
            model.setUuid(UUID.randomUUID());
            String route = buildRoute(ObjectBodyController.BOXED_BODY_ROUTE);
            BoxedModel response = getJsonResponseForPost(route, model, BoxedModel.class);
            Assert.assertEquals(model.getString(), response.getString());
            Assert.assertEquals(model.getBoolean(), response.getBoolean());
            Assert.assertEquals(model.getInteger(), response.getInteger());
            Assert.assertEquals(model.getDouble(), response.getDouble());
            Assert.assertEquals(model.getByte(), response.getByte());
            Assert.assertEquals(model.getShort(), response.getShort());
            Assert.assertEquals(model.getFloat(), response.getFloat());
            Assert.assertEquals(model.getCharacter(), response.getCharacter());
            Assert.assertEquals(model.getLong(), response.getLong());
            Assert.assertEquals(model.getDate(), response.getDate());
            Assert.assertEquals(model.getInstant(), response.getInstant());
            Assert.assertEquals(model.getZonedDateTime(), response.getZonedDateTime());
            Assert.assertEquals(model.getOffsetDateTime(), response.getOffsetDateTime());
            Assert.assertEquals(model.getLocalDateTime(), response.getLocalDateTime());
            Assert.assertEquals(model.getLocalDate(), response.getLocalDate());
            Assert.assertEquals(model.getBigInteger(), response.getBigInteger());
            Assert.assertEquals(model.getBigDecimal(), response.getBigDecimal());
            Assert.assertEquals(model.getUuid(), response.getUuid());
        }).join();
    }
}
