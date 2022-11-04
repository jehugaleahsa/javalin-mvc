package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.RedundantParameterController;
import com.truncon.javalin.mvc.test.models.RedundantParameterModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

final class RedundantParameterTest {
    @Test
    void testRedundantParameter_sameValueBoundMultipleTimes() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(RedundantParameterController.DUPLICATE_PARAMETER_ROUTE, queryParams(
                param("value", "123")
            ));
            RedundantParameterModel model = QueryUtils.getJsonResponseForGet(route, RedundantParameterModel.class);
            Assertions.assertEquals("123", model.getAsString());
            Assertions.assertEquals((Integer) 123, model.getAsInteger());
            Assertions.assertEquals((Double) 123.0, model.getAsDouble());
        });
    }
}
