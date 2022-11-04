package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.InjectionController;
import com.truncon.javalin.mvc.test.utils.DependencyImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

final class DependencyInjectionTest {
    @Test
    void testInjectorUsed_controller_returnsDependencyValue() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(InjectionController.GET_CONTROLLER_ROUTE);
            String value = QueryUtils.getStringForGet(route);
            Assertions.assertEquals(new DependencyImpl().getValue(), value);
        });
    }

    @Test
    void testInjectorUsed_beforeHandler_returnsDependencyValue() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(InjectionController.GET_BEFORE_HANDLER_ROUTE);
            String value = QueryUtils.getStringForGet(route);
            Assertions.assertEquals(new DependencyImpl().getValue(), value);
        });
    }

    @Test
    void testInjectorUsed_afterHandler_returnsDependencyValue() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(InjectionController.GET_AFTER_HANDLER_ROUTE);
            String value = QueryUtils.getStringForGet(route);
            Assertions.assertEquals(new DependencyImpl().getValue(), value);
        });
    }

    @Test
    void testInjectorUsed_converter_returnsDependencyValue() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                InjectionController.GET_CONVERTER_ROUTE,
                Collections.singletonList(Pair.of("value", "if you see this, the converter wasn't used"))
            );
            String value = QueryUtils.getStringForGet(route);
            Assertions.assertEquals(new DependencyImpl().getValue(), value);
        });
    }

    @Test
    void testInjectorUsed_model_returnsDependencyValue() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                InjectionController.GET_MODEL_ROUTE,
                Collections.singletonList(Pair.of("x", "123"))
            );
            FakeInjectionModel model = QueryUtils.getJsonResponseForGet(route, FakeInjectionModel.class);
            Assertions.assertEquals(new DependencyImpl().getValue(), model.getValue());
            Assertions.assertEquals((Integer) 123, model.getX());
        });
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
