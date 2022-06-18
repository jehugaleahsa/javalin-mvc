package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.models.BindFromJsonModelController;
import com.truncon.javalin.mvc.test.controllers.ws.models.BindSettersFromQueryModelController;
import com.truncon.javalin.mvc.test.controllers.ws.models.ExplicitBindFromJsonModelController;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.QueryUtils.parseJson;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class WsBindModelTest {
    @Test
    public void testBindSettersFromQuery() throws IOException {
        String route = buildWsRouteWithQueryParams(BindSettersFromQueryModelController.ROUTE, queryParams(
            param("integer", Integer.toString(Integer.MAX_VALUE)),
            param("boolean", Boolean.toString(true)),
            param("double", Double.toString(Double.MAX_VALUE)),
            param("byte", Byte.toString(Byte.MAX_VALUE)),
            param("short", Short.toString(Short.MAX_VALUE)),
            param("float", Float.toString(Float.MAX_VALUE)),
            param("char", Character.toString('a')),
            param("long", Long.toString(Long.MAX_VALUE))
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response -> {
                PrimitiveModel model = parseJson(response, PrimitiveModel.class);
                Assert.assertNotNull(model);
                Assert.assertEquals(Integer.MAX_VALUE, model.getInteger());
                Assert.assertTrue(model.getBoolean());
                Assert.assertEquals(Double.MAX_VALUE, model.getDouble(), 0.0);
                Assert.assertEquals(Byte.MAX_VALUE, model.getByte());
                Assert.assertEquals(Short.MAX_VALUE, model.getShort());
                Assert.assertEquals(Float.MAX_VALUE, model.getFloat(), 0.0);
                Assert.assertEquals('a', model.getChar());
                Assert.assertEquals(Long.MAX_VALUE, model.getLong());
            }))
        );
    }

    @Test
    public void testBindFromJson() throws IOException {
        String route = buildWsRoute(BindFromJsonModelController.ROUTE);
        PrimitiveModel model = getPrimitiveModel();
        AsyncTestUtils.runTestAsync(app -> {
            String json = QueryUtils.getJsonString(model);
            return WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse(json).thenAccept(response -> {
                PrimitiveModel actual = parseJson(response, PrimitiveModel.class);
                assertPrimitiveModel(model, actual);
            }));
        });
    }

    @Test
    public void testBindFromJson_explicit() throws IOException {
        String route = buildWsRoute(ExplicitBindFromJsonModelController.ROUTE);
        PrimitiveModel model = getPrimitiveModel();
        AsyncTestUtils.runTestAsync(app -> {
            String json = QueryUtils.getJsonString(model);
            return WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse(json).thenAccept(response -> {
                PrimitiveModel actual = parseJson(response, PrimitiveModel.class);
                assertPrimitiveModel(model, actual);
            }));
        });
    }

    private static PrimitiveModel getPrimitiveModel() {
        PrimitiveModel model = new PrimitiveModel();
        model.setByte(Byte.MAX_VALUE);
        model.setShort(Short.MAX_VALUE);
        model.setInteger(Integer.MAX_VALUE);
        model.setLong(Long.MAX_VALUE);
        model.setFloat(Float.MAX_VALUE);
        model.setDouble(Double.MAX_VALUE);
        model.setChar('a');
        model.setBoolean(true);
        return model;
    }

    private static void assertPrimitiveModel(PrimitiveModel expected, PrimitiveModel actual) {
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected.getByte(), actual.getByte());
        Assert.assertEquals(expected.getShort(), actual.getShort());
        Assert.assertEquals(expected.getInteger(), actual.getInteger());
        Assert.assertEquals(expected.getLong(), actual.getLong());
        Assert.assertEquals(expected.getFloat(), actual.getFloat(), 0.0);
        Assert.assertEquals(expected.getDouble(), actual.getDouble(), 0.0);
        Assert.assertEquals(expected.getChar(), actual.getChar());
        Assert.assertEquals(expected.getBoolean(), actual.getBoolean());
    }
}
