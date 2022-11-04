package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.BindModelController;
import com.truncon.javalin.mvc.test.models.ArrayModel;
import com.truncon.javalin.mvc.test.models.CollectionModel;
import com.truncon.javalin.mvc.test.models.ContainerModel;
import com.truncon.javalin.mvc.test.models.NestedJsonModel;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;
import com.truncon.javalin.mvc.test.models.PrimitiveParamFieldModel;
import com.truncon.javalin.mvc.test.models.PrimitiveParamFieldNamedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveParamMethodModel;
import com.truncon.javalin.mvc.test.models.PrimitiveParamMethodNamedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamFieldModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamFieldNamedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamMethodModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamMethodNamedModel;
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
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.UUID;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForPost;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

final class BindModelTest {
    @Test
    void testGet_path_setters_withSource() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_SETTERS_WITH_SOURCE_ROUTE, queryParams(
                param("integer", Integer.toString(Integer.MAX_VALUE)),
                param("boolean", Boolean.toString(true)),
                param("double", Double.toString(Double.MAX_VALUE)),
                param("byte", Byte.toString(Byte.MAX_VALUE)),
                param("short", Short.toString(Short.MAX_VALUE)),
                param("float", Float.toString(Float.MAX_VALUE)),
                param("char", Character.toString(Character.MAX_VALUE)),
                param("long", Long.toString(Long.MAX_VALUE))
            ));
            PrimitiveParamMethodModel model = getJsonResponseForGet(route, PrimitiveParamMethodModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assertions.assertTrue(model.getBoolean());
            Assertions.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assertions.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assertions.assertEquals(Short.MAX_VALUE, model.getShort());
            Assertions.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assertions.assertEquals(Character.MAX_VALUE, model.getChar());
            Assertions.assertEquals(Long.MAX_VALUE, model.getLong());
        });
    }

    @Test
    void testGet_path_setters_named_withSource() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_SETTERS_NAMED_WITH_SOURCE_ROUTE, queryParams(
                param("aInteger", Integer.toString(Integer.MAX_VALUE)),
                param("aBoolean", Boolean.toString(true)),
                param("aDouble", Double.toString(Double.MAX_VALUE)),
                param("aByte", Byte.toString(Byte.MAX_VALUE)),
                param("aShort", Short.toString(Short.MAX_VALUE)),
                param("aFloat", Float.toString(Float.MAX_VALUE)),
                param("aChar", Character.toString(Character.MAX_VALUE)),
                param("aLong", Long.toString(Long.MAX_VALUE))
            ));
            PrimitiveParamMethodNamedModel model = getJsonResponseForGet(route, PrimitiveParamMethodNamedModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assertions.assertTrue(model.getBoolean());
            Assertions.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assertions.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assertions.assertEquals(Short.MAX_VALUE, model.getShort());
            Assertions.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assertions.assertEquals(Character.MAX_VALUE, model.getChar());
            Assertions.assertEquals(Long.MAX_VALUE, model.getLong());
        });
    }

    @Test
    void testGet_path_fields_named_withSource() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_FIELDS_NAMED_WITH_SOURCE_ROUTE, queryParams(
                param("aInteger", Integer.toString(Integer.MAX_VALUE)),
                param("aBoolean", Boolean.toString(true)),
                param("aDouble", Double.toString(Double.MAX_VALUE)),
                param("aByte", Byte.toString(Byte.MAX_VALUE)),
                param("aShort", Short.toString(Short.MAX_VALUE)),
                param("aFloat", Float.toString(Float.MAX_VALUE)),
                param("aChar", Character.toString(Character.MAX_VALUE)),
                param("aLong", Long.toString(Long.MAX_VALUE))
            ));
            PrimitiveParamFieldNamedModel model = getJsonResponseForGet(route, PrimitiveParamFieldNamedModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals(Integer.MAX_VALUE, model.intValue);
            Assertions.assertTrue(model.booleanValue);
            Assertions.assertEquals(Double.MAX_VALUE, model.doubleValue, 0.0);
            Assertions.assertEquals(Byte.MAX_VALUE, model.byteValue);
            Assertions.assertEquals(Short.MAX_VALUE, model.shortValue);
            Assertions.assertEquals(Float.MAX_VALUE, model.floatValue, 0.0f);
            Assertions.assertEquals(Character.MAX_VALUE, model.charValue);
            Assertions.assertEquals(Long.MAX_VALUE, model.longValue);
        });
    }

    @Test
    void testGet_path_fields() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_FIELDS_WITH_SOURCE_ROUTE, queryParams(
                param("intValue", Integer.toString(Integer.MAX_VALUE)),
                param("booleanValue", Boolean.toString(true)),
                param("doubleValue", Double.toString(Double.MAX_VALUE)),
                param("byteValue", Byte.toString(Byte.MAX_VALUE)),
                param("shortValue", Short.toString(Short.MAX_VALUE)),
                param("floatValue", Float.toString(Float.MAX_VALUE)),
                param("charValue", Character.toString(Character.MAX_VALUE)),
                param("longValue", Long.toString(Long.MAX_VALUE))
            ));
            PrimitiveParamFieldModel model = getJsonResponseForGet(route, PrimitiveParamFieldModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals(Integer.MAX_VALUE, model.intValue);
            Assertions.assertTrue(model.booleanValue);
            Assertions.assertEquals(Double.MAX_VALUE, model.doubleValue, 0.0);
            Assertions.assertEquals(Byte.MAX_VALUE, model.byteValue);
            Assertions.assertEquals(Short.MAX_VALUE, model.shortValue);
            Assertions.assertEquals(Float.MAX_VALUE, model.floatValue, 0.0f);
            Assertions.assertEquals(Character.MAX_VALUE, model.charValue);
            Assertions.assertEquals(Long.MAX_VALUE, model.longValue);
        });
    }

    @Test
    void testGet_path_setters_noSource() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_SETTERS_NO_SOURCE_ROUTE, queryParams(
                param("integer", Integer.toString(Integer.MAX_VALUE)),
                param("boolean", Boolean.toString(true)),
                param("double", Double.toString(Double.MAX_VALUE)),
                param("byte", Byte.toString(Byte.MAX_VALUE)),
                param("short", Short.toString(Short.MAX_VALUE)),
                param("float", Float.toString(Float.MAX_VALUE)),
                param("char", Character.toString(Character.MAX_VALUE)),
                param("long", Long.toString(Long.MAX_VALUE))
            ));
            PrimitiveQueryParamMethodModel model = getJsonResponseForGet(route, PrimitiveQueryParamMethodModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assertions.assertTrue(model.getBoolean());
            Assertions.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assertions.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assertions.assertEquals(Short.MAX_VALUE, model.getShort());
            Assertions.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assertions.assertEquals(Character.MAX_VALUE, model.getChar());
            Assertions.assertEquals(Long.MAX_VALUE, model.getLong());
        });
    }

    @Test
    void testGet_path_setters_named_noSource() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_SETTERS_NAMED_NO_SOURCE_ROUTE, queryParams(
                param("aInteger", Integer.toString(Integer.MAX_VALUE)),
                param("aBoolean", Boolean.toString(true)),
                param("aDouble", Double.toString(Double.MAX_VALUE)),
                param("aByte", Byte.toString(Byte.MAX_VALUE)),
                param("aShort", Short.toString(Short.MAX_VALUE)),
                param("aFloat", Float.toString(Float.MAX_VALUE)),
                param("aChar", Character.toString(Character.MAX_VALUE)),
                param("aLong", Long.toString(Long.MAX_VALUE))
            ));
            PrimitiveQueryParamMethodNamedModel model = getJsonResponseForGet(route, PrimitiveQueryParamMethodNamedModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assertions.assertTrue(model.getBoolean());
            Assertions.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assertions.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assertions.assertEquals(Short.MAX_VALUE, model.getShort());
            Assertions.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assertions.assertEquals(Character.MAX_VALUE, model.getChar());
            Assertions.assertEquals(Long.MAX_VALUE, model.getLong());
        });
    }

    @Test
    void testGet_path_fields_noSource() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_FIELDS_NO_SOURCE_ROUTE, queryParams(
                param("intValue", Integer.toString(Integer.MAX_VALUE)),
                param("booleanValue", Boolean.toString(true)),
                param("doubleValue", Double.toString(Double.MAX_VALUE)),
                param("byteValue", Byte.toString(Byte.MAX_VALUE)),
                param("shortValue", Short.toString(Short.MAX_VALUE)),
                param("floatValue", Float.toString(Float.MAX_VALUE)),
                param("charValue", Character.toString(Character.MAX_VALUE)),
                param("longValue", Long.toString(Long.MAX_VALUE))
            ));
            PrimitiveQueryParamFieldModel model = getJsonResponseForGet(route, PrimitiveQueryParamFieldModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals(Integer.MAX_VALUE, model.intValue);
            Assertions.assertTrue(model.booleanValue);
            Assertions.assertEquals(Double.MAX_VALUE, model.doubleValue, 0.0);
            Assertions.assertEquals(Byte.MAX_VALUE, model.byteValue);
            Assertions.assertEquals(Short.MAX_VALUE, model.shortValue);
            Assertions.assertEquals(Float.MAX_VALUE, model.floatValue, 0.0f);
            Assertions.assertEquals(Character.MAX_VALUE, model.charValue);
            Assertions.assertEquals(Long.MAX_VALUE, model.longValue);
        });
    }

    @Test
    void testGet_path_fields_named_noSource() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_FIELDS_NAMED_NO_SOURCE_ROUTE, queryParams(
                param("aInteger", Integer.toString(Integer.MAX_VALUE)),
                param("aBoolean", Boolean.toString(true)),
                param("aDouble", Double.toString(Double.MAX_VALUE)),
                param("aByte", Byte.toString(Byte.MAX_VALUE)),
                param("aShort", Short.toString(Short.MAX_VALUE)),
                param("aFloat", Float.toString(Float.MAX_VALUE)),
                param("aChar", Character.toString(Character.MAX_VALUE)),
                param("aLong", Long.toString(Long.MAX_VALUE))
            ));
            PrimitiveQueryParamFieldNamedModel model = getJsonResponseForGet(route, PrimitiveQueryParamFieldNamedModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals(Integer.MAX_VALUE, model.intValue);
            Assertions.assertTrue(model.booleanValue);
            Assertions.assertEquals(Double.MAX_VALUE, model.doubleValue, 0.0);
            Assertions.assertEquals(Byte.MAX_VALUE, model.byteValue);
            Assertions.assertEquals(Short.MAX_VALUE, model.shortValue);
            Assertions.assertEquals(Float.MAX_VALUE, model.floatValue, 0.0f);
            Assertions.assertEquals(Character.MAX_VALUE, model.charValue);
            Assertions.assertEquals(Long.MAX_VALUE, model.longValue);
        });
    }

    @Test
    void testGet_nestedModels_bindsRecursively() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_NESTED_MODELS_ROUTE, queryParams(
                param("container", "container"),
                param("field", "field"),
                param("setter", "setter")
            ));
            ContainerModel model = getJsonResponseForGet(route, ContainerModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals("container", model.container);
            Assertions.assertNotNull(model.field);
            Assertions.assertEquals("field", model.field.field);
            Assertions.assertNotNull(model.getSetter());
            Assertions.assertEquals("setter", model.getSetter().setter);
        });
    }

    @Test
    void testPost_nestedJsonModel() {
        PrimitiveModel model = new PrimitiveModel();
        model.setBoolean(true);
        model.setByte(Byte.MAX_VALUE);
        model.setChar('a');
        model.setDouble(Double.MAX_VALUE);
        model.setFloat(Float.MAX_VALUE);
        model.setInteger(Integer.MAX_VALUE);
        model.setLong(Long.MAX_VALUE);
        model.setShort(Short.MAX_VALUE);
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(BindModelController.POST_NESTED_JSON_MODEL_ROUTE);
            NestedJsonModel actual = getJsonResponseForPost(route, model, NestedJsonModel.class);
            Assertions.assertNotNull(actual);
            assertModel(model, actual.field);
            assertModel(model, actual.getSetter());
            assertModel(model, actual.getParameter());
        });
    }

    private static void assertModel(PrimitiveModel expected, PrimitiveModel actual) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.getBoolean(), actual.getBoolean());
        Assertions.assertEquals(expected.getByte(), actual.getByte());
        Assertions.assertEquals(expected.getChar(), actual.getChar());
        Assertions.assertEquals(expected.getDouble(), actual.getDouble(), 0.0);
        Assertions.assertEquals(expected.getFloat(), actual.getFloat(), 0.0f);
        Assertions.assertEquals(expected.getInteger(), actual.getInteger());
        Assertions.assertEquals(expected.getLong(), actual.getLong());
        Assertions.assertEquals(expected.getShort(), actual.getShort());
    }

    @Test
    void testPost_noBinding() {
        PrimitiveModel model = new PrimitiveModel();
        model.setBoolean(true);
        model.setByte(Byte.MAX_VALUE);
        model.setChar('a');
        model.setDouble(Double.MAX_VALUE);
        model.setFloat(Float.MAX_VALUE);
        model.setInteger(Integer.MAX_VALUE);
        model.setLong(Long.MAX_VALUE);
        model.setShort(Short.MAX_VALUE);
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(BindModelController.POST_NO_BINDING_ROUTE);
            PrimitiveModel actual = getJsonResponseForPost(route, model, PrimitiveModel.class);
            Assertions.assertNull(actual);
        });
    }

    @Test
    void testGet_arrayModel() {
        AsyncTestUtils.runTest(app -> {
            UUID uuid = UUID.randomUUID();
            String route = buildRouteWithQueryParams(BindModelController.GET_ARRAY_VALUES, queryParams(
                param("boolean", "true"),
                param("boolean", "false"),
                param("int", "1"),
                param("int", "2"),
                param("double", "3.14"),
                param("double", "4.25"),
                param("byte", "3"),
                param("byte", "4"),
                param("short", "5"),
                param("short", "6"),
                param("float", "5.36"),
                param("float", "6.47"),
                param("char", "a"),
                param("char", "b"),
                param("long", "7"),
                param("long", "8"),
                param("String", "hello"),
                param("String", null),
                param("Boolean", "true"),
                param("Boolean", null),
                param("Integer", "9"),
                param("Integer", null),
                param("Double", "7.58"),
                param("Double", null),
                param("Byte", "10"),
                param("Byte", null),
                param("Short", "11"),
                param("Short", null),
                param("Float", "8.69"),
                param("Float", null),
                param("Character", "c"),
                param("Character", null),
                param("Long", "12"),
                param("Long", null),
                param("Date", "2022-06-28T19:27:00Z"),
                param("Date", null),
                param("Instant", "2022-06-28T19:27:59Z"),
                param("Instant", null),
                param("ZonedDateTime", "2022-06-28T19:28:00Z"),
                param("ZonedDateTime", null),
                param("OffsetDateTime", "2022-06-28T19:28:59-04:00"),
                param("OffsetDateTime", null),
                param("LocalDateTime", "2022-06-28T19:30:00"),
                param("LocalDateTime", null),
                param("LocalDate", "2022-06-28"),
                param("LocalDate", null),
                param("YearMonth", "2022-06"),
                param("YearMonth", null),
                param("Year", "2022"),
                param("Year", null),
                param("BigInteger", "13"),
                param("BigInteger", null),
                param("BigDecimal", "9.70"),
                param("BigDecimal", null),
                param("UUID", uuid.toString()),
                param("UUID", null)
            ));
            ArrayModel model = getJsonResponseForGet(route, ArrayModel.class);
            Assertions.assertArrayEquals(new boolean[] { true, false }, model.getBooleanValues());
            Assertions.assertArrayEquals(new int[] { 1, 2 }, model.getIntValues());
            Assertions.assertArrayEquals(new double[] { 3.14, 4.25 }, model.getDoubleValues(), 0.0);
            Assertions.assertArrayEquals(new byte[] { 3, 4 }, model.getByteValues());
            Assertions.assertArrayEquals(new short[] { 5, 6 }, model.getShortValues());
            Assertions.assertArrayEquals(new float[] { 5.36f, 6.47f }, model.getFloatValues(), 0.0f);
            Assertions.assertArrayEquals(new char[] { 'a', 'b' }, model.getCharValues());
            Assertions.assertArrayEquals(new long[] { 7L, 8L }, model.getLongValues());
            Assertions.assertArrayEquals(new String[] { "hello", null }, model.getStringValues());
            Assertions.assertArrayEquals(new Boolean[] { true, null }, model.getBoxedBooleanValues());
            Assertions.assertArrayEquals(new Integer[] { 9, null }, model.getBoxedIntegerValues());
            Assertions.assertArrayEquals(new Double[] { 7.58, null }, model.getBoxedDoubleValues());
            Assertions.assertArrayEquals(new Byte[] { (byte) 10, null }, model.getBoxedByteValues());
            Assertions.assertArrayEquals(new Short[] { (short) 11, null }, model.getBoxedShortValues());
            Assertions.assertArrayEquals(new Float[] { 8.69f, null }, model.getBoxedFloatValues());
            Assertions.assertArrayEquals(new Character[] { 'c', null }, model.getBoxedCharacterValues());
            Assertions.assertArrayEquals(new Long[] { 12L, null }, model.getBoxedLongValues());
            Assertions.assertArrayEquals(new Date[] { Date.from(Instant.parse("2022-06-28T19:27:00Z")), null }, model.getDateValues());
            Assertions.assertArrayEquals(new Instant[] { Instant.parse("2022-06-28T19:27:59Z"), null }, model.getInstantValues());
            Assertions.assertArrayEquals(new ZonedDateTime[] { ZonedDateTime.of(2022, 6, 28, 19, 28, 0, 0, ZoneOffset.UTC), null }, model.getZonedDateTimeValues());
            Assertions.assertArrayEquals(new OffsetDateTime[] { OffsetDateTime.of(2022, 6, 28, 19, 28, 59, 0, ZoneOffset.ofHours(-4)), null }, model.getOffsetDateTimeValues());
            Assertions.assertArrayEquals(new LocalDateTime[] { LocalDateTime.of(2022, 6, 28, 19, 30, 0), null }, model.getLocalDateTimeValues());
            Assertions.assertArrayEquals(new LocalDate[] { LocalDate.of(2022, 6, 28), null }, model.getLocalDateValues());
            Assertions.assertArrayEquals(new YearMonth[] { YearMonth.of(2022, 6), null }, model.getYearMonthValues());
            Assertions.assertArrayEquals(new Year[] { Year.of(2022), null }, model.getYearValues());
            Assertions.assertArrayEquals(new BigInteger[] { new BigInteger("13"), null }, model.getBigIntegerValues());
            Assertions.assertArrayEquals(new BigDecimal[] { new BigDecimal("9.70"), null }, model.getBigDecimalValues());
            Assertions.assertArrayEquals(new UUID[] { uuid, null }, model.getUuidValues());
        });
    }

    @Test
    void testGet_collectionModel() {
        AsyncTestUtils.runTest(app -> {
            UUID uuid = UUID.randomUUID();
            String route = buildRouteWithQueryParams(BindModelController.GET_COLLECTION_VALUES, queryParams(
                param("String", "hello"),
                param("String", null),
                param("Boolean", "true"),
                param("Boolean", null),
                param("Integer", "9"),
                param("Integer", null),
                param("Double", "7.58"),
                param("Double", null),
                param("Byte", "10"),
                param("Byte", null),
                param("Short", "11"),
                param("Short", null),
                param("Float", "8.69"),
                param("Float", null),
                param("Character", "c"),
                param("Character", null),
                param("Long", "12"),
                param("Long", null),
                param("Date", "2022-06-28T19:27:00Z"),
                param("Date", null),
                param("Instant", "2022-06-28T19:27:59Z"),
                param("Instant", null),
                param("ZonedDateTime", "2022-06-28T19:28:00Z"),
                param("ZonedDateTime", null),
                param("OffsetDateTime", "2022-06-28T19:28:59-04:00"),
                param("OffsetDateTime", null),
                param("LocalDateTime", "2022-06-28T19:30:00"),
                param("LocalDateTime", null),
                param("LocalDate", "2022-06-28"),
                param("LocalDate", null),
                param("YearMonth", "2022-06"),
                param("YearMonth", null),
                param("Year", "2022"),
                param("Year", null),
                param("BigInteger", "13"),
                param("BigInteger", null),
                param("BigDecimal", "9.70"),
                param("BigDecimal", null),
                param("UUID", uuid.toString()),
                param("UUID", null)
            ));
            CollectionModel model = getJsonResponseForGet(route, CollectionModel.class);
            Assertions.assertEquals(Arrays.asList("hello", null), model.getStringValues());
            Assertions.assertEquals(Arrays.asList(true, null), model.getBooleanValues());
            Assertions.assertEquals(Arrays.asList(9, null), model.getIntegerValues());
            Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(7.58, null)), model.getDoubleValues());
            Assertions.assertEquals(Arrays.asList((byte) 10, null), model.getByteValues());
            Assertions.assertEquals(Arrays.asList((short) 11, null), model.getShortValues());
            Assertions.assertEquals(new HashSet<>(Arrays.asList(8.69f, null)), model.getFloatValues());
            Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList('c', null)), model.getCharacterValues());
            Assertions.assertEquals(Arrays.asList(12L, null), model.getBoxedLongValues());
            Assertions.assertEquals(Arrays.asList(Date.from(Instant.parse("2022-06-28T19:27:00Z")), null), model.getDateValues());
            Assertions.assertEquals(Arrays.asList(Instant.parse("2022-06-28T19:27:59Z"), null), model.getInstantValues());
            Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(ZonedDateTime.of(2022, 6, 28, 19, 28, 0, 0, ZoneOffset.UTC), null)), model.getZonedDateTimeValues());
            Assertions.assertEquals(Arrays.asList(OffsetDateTime.of(2022, 6, 28, 19, 28, 59, 0, ZoneOffset.ofHours(-4)), null), model.getOffsetDateTimeValues());
            Assertions.assertEquals(Arrays.asList(LocalDateTime.of(2022, 6, 28, 19, 30, 0), null), model.getLocalDateTimeValues());
            Assertions.assertEquals(new HashSet<>(Arrays.asList(LocalDate.of(2022, 6, 28), null)), model.getLocalDateValues());
            Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(YearMonth.of(2022, 6), null)), model.getYearMonthValues());
            Assertions.assertEquals(Arrays.asList(Year.of(2022), null), model.getYearValues());
            Assertions.assertEquals(Arrays.asList(new BigInteger("13"), null), model.getBigIntegerValues());
            Assertions.assertEquals(Arrays.asList(new BigDecimal("9.70"), null), model.getBigDecimalValues());
            Assertions.assertEquals(new LinkedHashSet<>(Arrays.asList(uuid, null)), model.getUuidValues());
        });
    }
}
