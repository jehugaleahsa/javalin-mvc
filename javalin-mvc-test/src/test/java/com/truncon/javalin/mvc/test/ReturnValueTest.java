package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ReturnValueController;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReturnValueTest {
    @Test
    void testReturningNonActionResult_returnsModel_convertsToJson() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_OBJECT_ROUTE);
            PrimitiveModel model = QueryUtils.getJsonResponseForGet(route, PrimitiveModel.class);
            Assertions.assertNotNull(model);
        });
    }

    @Test
    void testReturningNonActionResult_returnsInt_convertsToJson() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_PRIMITIVE_INTEGER_ROUTE);
            int value = QueryUtils.getJsonResponseForGet(route, int.class);
            Assertions.assertEquals(123, value);
        });
    }

    @Test
    void testReturningNonActionResult_returnsInteger_convertsToJson() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_BOXED_INTEGER_ROUTE);
            Integer value = QueryUtils.getJsonResponseForGet(route, Integer.class);
            Assertions.assertEquals((Integer) 123, value);
        });
    }

    @Test
    void testReturningNonActionResult_returnsNull_convertsToJson() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_NULL_OBJECT_ROUTE);
            Object value = QueryUtils.getJsonResponseForGet(route, Object.class);
            Assertions.assertNull(value);
        });
    }

    @Test
    void testReturningActionResultAsObject_returnsActionResult_executes() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_ACTION_RESULT_ROUTE);
            Integer value = QueryUtils.getJsonResponseForGet(route, Integer.class);
            Assertions.assertEquals((Integer) 123, value);
        });
    }

    @Test
    void testReturningVoid_executes() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_VOID_ROUTE);
            String value = QueryUtils.getStringForGet(route);
            Assertions.assertEquals("A-okay!", value);
        });
    }
}
