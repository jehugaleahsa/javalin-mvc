package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveBooleanParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveByteParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveCharacterParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveDoubleParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveFloatParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveIntegerParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveLongParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.primitives.PrimitiveShortParameterController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;

final class WsPrimitiveParamTest {
    @Test
    void testByte() throws IOException {
        String value = Byte.toString(Byte.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveByteParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assertions.assertEquals(value, response)
            ))
        );
    }

    @Test
    void testShort() throws IOException {
        String value = Short.toString(Short.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveShortParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assertions.assertEquals(value, response)
            ))
        );
    }

    @Test
    void testInteger() throws IOException {
        String value = Integer.toString(Integer.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveIntegerParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assertions.assertEquals(value, response)
            ))
        );
    }

    @Test
    void testLong() throws IOException {
        String value = Long.toString(Long.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveLongParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assertions.assertEquals(value, response)
            ))
        );
    }

    @Test
    void testFloat() throws IOException {
        String value = Float.toString(Float.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveFloatParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assertions.assertEquals(value, response)
            ))
        );
    }

    @Test
    void testDouble() throws IOException {
        String value = Double.toString(Double.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveDoubleParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assertions.assertEquals(value, response)
            ))
        );
    }

    @Test
    void testBoolean() throws IOException {
        String value = Boolean.toString(true);
        String route = buildWsRouteWithPathParams(PrimitiveBooleanParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assertions.assertEquals(value, response)
            ))
        );
    }

    @Test
    void testCharacter() throws IOException {
        String value = Character.toString(Character.MAX_VALUE);
        String route = buildWsRouteWithPathParams(PrimitiveCharacterParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assertions.assertEquals(value, response)
            ))
        );
    }
}
