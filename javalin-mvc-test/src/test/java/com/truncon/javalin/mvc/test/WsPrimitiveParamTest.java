package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.PrimitiveIntegerParameterController;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;

public final class WsPrimitiveParamTest {
    @Test
    public void testInteger() throws IOException {
        String route = buildWsRouteWithPathParams(PrimitiveIntegerParameterController.ROUTE, pathParams(
            param("value", "123")
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals("123", response)
            )).thenCompose(v ->
                WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                    Assert.assertEquals("123", response)
                ))
            )
        );
    }
}
