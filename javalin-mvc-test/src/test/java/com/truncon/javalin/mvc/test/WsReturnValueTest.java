package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.returns.ActionResultController;
import com.truncon.javalin.mvc.test.controllers.ws.returns.IntegerController;
import com.truncon.javalin.mvc.test.controllers.ws.returns.ObjectController;
import com.truncon.javalin.mvc.test.controllers.ws.returns.PrimitiveIntController;
import com.truncon.javalin.mvc.test.controllers.ws.returns.VoidController;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

final class WsReturnValueTest {
    @Test
    void testAsync_getWsActionResult() {
        String route = RouteBuilder.buildWsRoute(ActionResultController.ROUTE);
        PrimitiveModel model = new PrimitiveModel();
        model.setInteger(123);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendJsonAndAwaitJsonResponse(
                model,
                PrimitiveModel.class
            ).thenAccept(response -> Assertions.assertEquals(123, response.getInteger())))
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
            ).thenAccept(response -> Assertions.assertEquals(123, response.getInteger())))
        );
    }

    @Test
    void testAsync_getInteger() {
        String route = RouteBuilder.buildWsRoute(IntegerController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitJsonResponse(
                "",
                Integer.class
            ).thenAccept(response -> Assertions.assertEquals((Integer) 123, response)))
        );
    }

    @Test
    void testAsync_getPrimitiveInt() {
        String route = RouteBuilder.buildWsRoute(PrimitiveIntController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitJsonResponse(
                "",
                int.class
            ).thenAccept(response -> Assertions.assertEquals((Integer) 123, response)))
        );
    }

    @Test
    void testAsync_getVoid() {
        String route = RouteBuilder.buildWsRoute(VoidController.ROUTE);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitResponse(
                ""
            ).thenAccept(response -> Assertions.assertEquals("A-okay!", response)))
        );
    }
}
