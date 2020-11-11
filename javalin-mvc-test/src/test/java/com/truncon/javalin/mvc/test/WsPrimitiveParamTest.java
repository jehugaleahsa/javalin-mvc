package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveByteParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveIntegerParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveShortParameterController;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;

public final class WsPrimitiveParamTest {
    @Test
    public void testByte() throws IOException {
        String value = Byte.toString(Byte.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveByteParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testShort() throws IOException {
        String value = Short.toString(Short.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveShortParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testInteger() throws IOException {
        String value = Integer.toString(Integer.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveIntegerParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }
}
