package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.models.InheritanceModelController;
import com.truncon.javalin.mvc.test.models.DerivedModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletionException;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

final class WsInheritanceTest {
    @Test
    void testInheritance_bindsInheritedMembers() throws IOException {
        UUID derivedUuid = UUID.randomUUID();
        String route = buildWsRouteWithQueryParams(InheritanceModelController.ROUTE, queryParams(
            param("baseName", "name"),
            param("baseId", Integer.toString(Integer.MAX_VALUE)),
            param("derivedUuid", derivedUuid.toString()),
            param("derivedAmount", Double.toString(Double.MAX_VALUE))
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response -> {
                try {
                    DerivedModel model = QueryUtils.parseJson(response, DerivedModel.class);
                    Assertions.assertNotNull(model);
                    Assertions.assertEquals("name", model.getBaseName());
                    Assertions.assertEquals(Integer.MAX_VALUE, model.baseId);
                    Assertions.assertEquals(derivedUuid, model.getDerivedUuid());
                    Assertions.assertEquals(Double.MAX_VALUE, model.derivedAmount, 0.0);
                } catch (Exception exception) {
                    throw new CompletionException(exception);
                }
            }))
        );
    }
}
