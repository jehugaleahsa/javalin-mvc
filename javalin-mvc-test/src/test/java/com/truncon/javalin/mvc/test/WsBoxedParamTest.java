package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedBooleanParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedByteParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedCharacterParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedDoubleParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedFloatParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedIntegerParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedLongParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedShortParameterController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;

final class WsBoxedParamTest {
    @Test
    void testByte() throws IOException {
        String value = Byte.toString(Byte.MAX_VALUE);
        String route = buildWsRouteWithPathParams(BoxedByteParameterController.ROUTE, pathParams(
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
        String route = buildWsRouteWithPathParams(BoxedShortParameterController.ROUTE, pathParams(
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
        String route = buildWsRouteWithPathParams(BoxedIntegerParameterController.ROUTE, pathParams(
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
        String route = buildWsRouteWithPathParams(BoxedLongParameterController.ROUTE, pathParams(
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
        String route = buildWsRouteWithPathParams(BoxedFloatParameterController.ROUTE, pathParams(
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
        String route = buildWsRouteWithPathParams(BoxedDoubleParameterController.ROUTE, pathParams(
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
        String route = buildWsRouteWithPathParams(BoxedBooleanParameterController.ROUTE, pathParams(
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
        String route = buildWsRouteWithPathParams(BoxedCharacterParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assertions.assertEquals(value, response)
            ))
        );
    }
}
