package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.async.ActionResultController;
import com.truncon.javalin.mvc.test.controllers.ws.async.IntegerController;
import com.truncon.javalin.mvc.test.controllers.ws.async.ObjectController;
import com.truncon.javalin.mvc.test.controllers.ws.async.VoidController;
import com.truncon.javalin.mvc.test.controllers.ws.async.WildcardController;
import com.truncon.javalin.mvc.test.controllers.ws.async.WildcardExtendsActionResultController;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class WsAsyncTest {
    @Test
    void testAsync_getWsActionResult() {
        String route = RouteBuilder.buildWsRoute(ActionResultController.ROUTE);
        PrimitiveModel model = new PrimitiveModel();
        model.setInteger(123);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendJsonAndAwaitJsonResponse(
                model,
                PrimitiveModel.class
            ).thenAccept(response -> {
                Assertions.assertEquals(123, response.getInteger());
            }))
        );
    }

    @Test
    void testAsync_getWildcard() {
        String route = RouteBuilder.buildWsRoute(WildcardController.ROUTE);
        PrimitiveModel model = new PrimitiveModel();
        model.setInteger(123);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendJsonAndAwaitJsonResponse(
                model,
                PrimitiveModel.class
            ).thenAccept(response -> {
                Assertions.assertEquals(123, response.getInteger());
            }))
        );
    }

    @Test
    void testAsync_getWildcard_extendsActionResult() {
        String route = RouteBuilder.buildWsRoute(WildcardExtendsActionResultController.ROUTE);
        PrimitiveModel model = new PrimitiveModel();
        model.setInteger(123);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendJsonAndAwaitJsonResponse(
                model,
                PrimitiveModel.class
            ).thenAccept(response -> {
                Assertions.assertEquals(123, response.getInteger());
            }))
        );
    }

    @Test
    void testAsync_getObject() {
        String route = RouteBuilder.buildWsRoute(ObjectController.ROUTE);
        PrimitiveModel model = new PrimitiveModel();
        model.setInteger(123);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendJsonAndAwaitJsonResponse(
                model,
                PrimitiveModel.class
            ).thenAccept(response -> {
                Assertions.assertEquals(123, response.getInteger());
            }))
        );
    }

    @Test
    void testAsync_getInteger() {
        String route = RouteBuilder.buildWsRoute(IntegerController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitJsonResponse(
                "",
                Integer.class
            ).thenAccept(response -> {
                Assertions.assertEquals((Integer) 123, response);
            }))
        );
    }

    @Test
    void testAsync_getVoid() {
        String route = RouteBuilder.buildWsRoute(VoidController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitResponse(
                ""
            ).thenAccept(response -> {
                Assertions.assertEquals("A-okay!", response);
            }))
        );
    }
}
