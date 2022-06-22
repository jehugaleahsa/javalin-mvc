package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ReturnValueController;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;
import org.junit.Assert;
import org.junit.Test;

public class ReturnValueTest {
    @Test
    public void testReturningNonActionResult_returnsModel_convertsToJson() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_OBJECT_ROUTE);
            PrimitiveModel model = QueryUtils.getJsonResponseForGet(route, PrimitiveModel.class);
            Assert.assertNotNull(model);
        });
    }

    @Test
    public void testReturningNonActionResult_returnsInt_convertsToJson() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_PRIMITIVE_INTEGER_ROUTE);
            int value = QueryUtils.getJsonResponseForGet(route, int.class);
            Assert.assertEquals(123, value);
        });
    }

    @Test
    public void testReturningNonActionResult_returnsInteger_convertsToJson() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_BOXED_INTEGER_ROUTE);
            Integer value = QueryUtils.getJsonResponseForGet(route, Integer.class);
            Assert.assertEquals((Integer) 123, value);
        });
    }

    @Test
    public void testReturningNonActionResult_returnsNull_convertsToJson() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_NULL_OBJECT_ROUTE);
            Object value = QueryUtils.getJsonResponseForGet(route, Object.class);
            Assert.assertNull(value);
        });
    }

    @Test
    public void testReturningActionResultAsObject_returnsActionResult_executes() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_ACTION_RESULT_ROUTE);
            Integer value = QueryUtils.getJsonResponseForGet(route, Integer.class);
            Assert.assertEquals((Integer) 123, value);
        });
    }

    @Test
    public void testReturningVoid_executes() {
        AsyncTestUtils.runTest(a -> {
            String route = RouteBuilder.buildRoute(ReturnValueController.GET_VOID_ROUTE);
            String value = QueryUtils.getStringForGet(route);
            Assert.assertEquals("A-okay!", value);
        });
    }
}
