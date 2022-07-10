package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.RedundantParameterController;
import com.truncon.javalin.mvc.test.models.RedundantParameterModel;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class RedundantParameterTest {
    @Test
    public void testRedundantParameter_sameValueBoundMultipleTimes() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(RedundantParameterController.DUPLICATE_PARAMETER_ROUTE, queryParams(
                param("value", "123")
            ));
            RedundantParameterModel model = QueryUtils.getJsonResponseForGet(route, RedundantParameterModel.class);
            Assert.assertEquals("123", model.getAsString());
            Assert.assertEquals((Integer) 123, model.getAsInteger());
            Assert.assertEquals((Double) 123.0, model.getAsDouble());
        });
    }
}
