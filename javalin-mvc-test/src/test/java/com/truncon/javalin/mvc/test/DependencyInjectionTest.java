package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.InjectionController;
import com.truncon.javalin.mvc.test.utils.DependencyImpl;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;

public final class DependencyInjectionTest {
    @Test
    public void testInjectorUsed_controller_returnsDependencyValue() {
        AsyncTestUtils.runTest(a -> {
            String route = InjectionController.GET_CONTROLLER_ROUTE;
            String value = QueryUtils.getStringForGet(route);
            Assert.assertEquals(new DependencyImpl().getValue(), value);
        });
    }

    @Test
    public void testInjectorUsed_beforeHandler_returnsDependencyValue() {
        AsyncTestUtils.runTest(a -> {
            String route = InjectionController.GET_BEFORE_HANDLER_ROUTE;
            String value = QueryUtils.getStringForGet(route);
            Assert.assertEquals(new DependencyImpl().getValue(), value);
        });
    }

    @Test
    public void testInjectorUsed_afterHandler_returnsDependencyValue() {
        AsyncTestUtils.runTest(a -> {
            String route = InjectionController.GET_AFTER_HANDLER_ROUTE;
            String value = QueryUtils.getStringForGet(route);
            Assert.assertEquals(new DependencyImpl().getValue(), value);
        });
    }

    @Test
    public void testInjectorUsed_converter_returnsDependencyValue() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRouteWithQueryParams(
                InjectionController.GET_CONVERTER_ROUTE,
                Collections.singletonList(Pair.of("value", "if you see this, the converter wasn't used"))
            );
            String value = QueryUtils.getStringForGet(route);
            Assert.assertEquals(new DependencyImpl().getValue(), value);
        });
    }
}
