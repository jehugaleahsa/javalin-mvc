package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.BindModelController;
import com.truncon.javalin.mvc.test.models.PrimitiveParamFieldNamedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveParamMethodModel;
import com.truncon.javalin.mvc.test.models.PrimitiveParamMethodNamedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamFieldModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamFieldNamedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamMethodModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamMethodNamedModel;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class BindModelTest {
    @Test
    public void testGet_path_setters_withSource() {
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
            Assert.assertNotNull(model);
            Assert.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assert.assertTrue(model.getBoolean());
            Assert.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assert.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assert.assertEquals(Short.MAX_VALUE, model.getShort());
            Assert.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assert.assertEquals(Character.MAX_VALUE, model.getChar());
            Assert.assertEquals(Long.MAX_VALUE, model.getLong());
        }).join();
    }

    @Test
    public void testGet_path_setters_named_withSource() {
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
            Assert.assertNotNull(model);
            Assert.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assert.assertTrue(model.getBoolean());
            Assert.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assert.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assert.assertEquals(Short.MAX_VALUE, model.getShort());
            Assert.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assert.assertEquals(Character.MAX_VALUE, model.getChar());
            Assert.assertEquals(Long.MAX_VALUE, model.getLong());
        }).join();
    }

    @Test
    public void testGet_path_fields_named_withSource() {
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
            Assert.assertNotNull(model);
            Assert.assertEquals(Integer.MAX_VALUE, model.intValue);
            Assert.assertTrue(model.booleanValue);
            Assert.assertEquals(Double.MAX_VALUE, model.doubleValue, 0.0);
            Assert.assertEquals(Byte.MAX_VALUE, model.byteValue);
            Assert.assertEquals(Short.MAX_VALUE, model.shortValue);
            Assert.assertEquals(Float.MAX_VALUE, model.floatValue, 0.0f);
            Assert.assertEquals(Character.MAX_VALUE, model.charValue);
            Assert.assertEquals(Long.MAX_VALUE, model.longValue);
        }).join();
    }

    @Test
    public void testGet_path_fields() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(BindModelController.GET_SETTERS_WITH_SOURCE_ROUTE, queryParams(
                param("intValue", Integer.toString(Integer.MAX_VALUE)),
                param("booleanValue", Boolean.toString(true)),
                param("doubleValue", Double.toString(Double.MAX_VALUE)),
                param("byteValue", Byte.toString(Byte.MAX_VALUE)),
                param("shortValue", Short.toString(Short.MAX_VALUE)),
                param("floatValue", Float.toString(Float.MAX_VALUE)),
                param("charValue", Character.toString(Character.MAX_VALUE)),
                param("longValue", Long.toString(Long.MAX_VALUE))
            ));
            PrimitiveParamMethodModel model = getJsonResponseForGet(route, PrimitiveParamMethodModel.class);
            Assert.assertNotNull(model);
            Assert.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assert.assertTrue(model.getBoolean());
            Assert.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assert.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assert.assertEquals(Short.MAX_VALUE, model.getShort());
            Assert.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assert.assertEquals(Character.MAX_VALUE, model.getChar());
            Assert.assertEquals(Long.MAX_VALUE, model.getLong());
        }).join();
    }

    @Test
    public void testGet_path_setters_noSource() {
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
            Assert.assertNotNull(model);
            Assert.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assert.assertTrue(model.getBoolean());
            Assert.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assert.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assert.assertEquals(Short.MAX_VALUE, model.getShort());
            Assert.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assert.assertEquals(Character.MAX_VALUE, model.getChar());
            Assert.assertEquals(Long.MAX_VALUE, model.getLong());
        }).join();
    }

    @Test
    public void testGet_path_setters_named_noSource() {
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
            Assert.assertNotNull(model);
            Assert.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assert.assertTrue(model.getBoolean());
            Assert.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assert.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assert.assertEquals(Short.MAX_VALUE, model.getShort());
            Assert.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assert.assertEquals(Character.MAX_VALUE, model.getChar());
            Assert.assertEquals(Long.MAX_VALUE, model.getLong());
        }).join();
    }

    @Test
    public void testGet_path_fields_noSource() {
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
            Assert.assertNotNull(model);
            Assert.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assert.assertTrue(model.getBoolean());
            Assert.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assert.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assert.assertEquals(Short.MAX_VALUE, model.getShort());
            Assert.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assert.assertEquals(Character.MAX_VALUE, model.getChar());
            Assert.assertEquals(Long.MAX_VALUE, model.getLong());
        }).join();
    }

    @Test
    public void testGet_path_fields_named_noSource() {
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
            Assert.assertNotNull(model);
            Assert.assertEquals(Integer.MAX_VALUE, model.getInteger());
            Assert.assertTrue(model.getBoolean());
            Assert.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
            Assert.assertEquals(Byte.MAX_VALUE, model.getByte());
            Assert.assertEquals(Short.MAX_VALUE, model.getShort());
            Assert.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0f);
            Assert.assertEquals(Character.MAX_VALUE, model.getChar());
            Assert.assertEquals(Long.MAX_VALUE, model.getLong());
        }).join();
    }
}
