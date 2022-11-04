package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.injection.WsInjectedAfterHandlerController;
import com.truncon.javalin.mvc.test.controllers.ws.injection.WsInjectedBeforeHandlerController;
import com.truncon.javalin.mvc.test.controllers.ws.injection.WsInjectedControllerController;
import com.truncon.javalin.mvc.test.controllers.ws.injection.WsInjectedConverterController;
import com.truncon.javalin.mvc.test.controllers.ws.injection.WsInjectedModelController;
import com.truncon.javalin.mvc.test.utils.DependencyImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

final class WsDependencyInjectionTest {
    @Test
    void testInjectorUsed_controller_returnsDependencyValue() {
        String route = RouteBuilder.buildWsRoute(WsInjectedControllerController.ROUTE);
        AsyncTestUtils.runTestAsync(
            app -> WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitResponse("")
                .thenAccept(value -> Assertions.assertEquals(new DependencyImpl().getValue(), value))
            )
        );
    }

    @Test
    void testInjectorUsed_beforeHandler_returnsDependencyValue() {
        String route = RouteBuilder.buildWsRoute(WsInjectedBeforeHandlerController.ROUTE);
        AsyncTestUtils.runTestAsync(
            app -> WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitResponse("")
                .thenAccept(value -> Assertions.assertEquals(new DependencyImpl().getValue(), value))
            )
        );
    }

    @Test
    void testInjectorUsed_afterHandler_returnsDependencyValue() {
        String route = RouteBuilder.buildWsRoute(WsInjectedAfterHandlerController.ROUTE);
        AsyncTestUtils.runTestAsync(
            // This will result in two messages getting sent. One in the controller and one in
            // the after handler.
            app -> WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitResponse("")
                .thenCompose(value -> {
                    Assertions.assertEquals(WsInjectedAfterHandlerController.CONTROLLER_MESSAGE, value);
                    return sessionManager.awaitStringMessage();
                })
                .thenAccept(value -> Assertions.assertEquals(new DependencyImpl().getValue(), value))
            )
        );
    }

    @Test
    void testInjectorUsed_converter_returnsDependencyValue() {
        String route = RouteBuilder.buildWsRoute(WsInjectedConverterController.ROUTE);
        AsyncTestUtils.runTestAsync(
            app -> WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitResponse("")
                .thenAccept(value -> Assertions.assertEquals(new DependencyImpl().getValue(), value))
            )
        );
    }

    @Test
    void testInjectorUsed_model_returnsDependencyValue() {
        String route = RouteBuilder.buildWsRouteWithQueryParams(
            WsInjectedModelController.ROUTE,
            Collections.singletonList(Pair.of("x", "123"))
        );
        AsyncTestUtils.runTestAsync(
            app -> WsTestUtils.ws(route, sessionManager -> sessionManager.sendStringAndAwaitJsonResponse("", FakeInjectionModel.class)
                .thenAccept(model -> {
                    Assertions.assertNotNull(model);
                    Assertions.assertEquals(new DependencyImpl().getValue(), model.getValue());
                    Assertions.assertEquals((Integer) 123, model.getX());
                })
            )
        );
    }

    private static final class FakeInjectionModel {
        private String value;
        private Integer x;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public Integer getX() {
            return x;
        }

        public void setX(Integer x) {
            this.x = x;
        }
    }
}
