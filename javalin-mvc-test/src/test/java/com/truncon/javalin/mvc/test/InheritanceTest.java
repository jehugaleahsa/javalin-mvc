package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.BindModelController;
import com.truncon.javalin.mvc.test.models.DerivedModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

final class InheritanceTest {
    @Test
    void testInheritance_bindsInheritedMembers() {
        AsyncTestUtils.runTest(app -> {
            UUID derivedUuid = UUID.randomUUID();
            String route = buildRouteWithQueryParams(BindModelController.GET_INHERITED_MODEL_ROUTE, queryParams(
                param("baseName", "name"),
                param("baseId", Integer.toString(Integer.MAX_VALUE)),
                param("derivedUuid", derivedUuid.toString()),
                param("derivedAmount", Double.toString(Double.MAX_VALUE))
            ));
            DerivedModel model = getJsonResponseForGet(route, DerivedModel.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals("name", model.getBaseName());
            Assertions.assertEquals(Integer.MAX_VALUE, model.baseId);
            Assertions.assertEquals(derivedUuid, model.getDerivedUuid());
            Assertions.assertEquals(Double.MAX_VALUE, model.derivedAmount, 0.0);
        });
    }
}
