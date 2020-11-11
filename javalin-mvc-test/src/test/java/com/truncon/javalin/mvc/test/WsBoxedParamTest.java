package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedBooleanParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedByteParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedCharacterParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedDoubleParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedFloatParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedIntegerParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedLongParameterController;
import com.truncon.javalin.mvc.test.controllers.ws.boxed.BoxedShortParameterController;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;

public final class WsBoxedParamTest {
    @Test
    public void testByte() throws IOException {
        String value = Byte.toString(Byte.MAX_VALUE);
        String route = buildWsRouteWithPathParams(BoxedByteParameterController.ROUTE, pathParams(
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
        String route = buildWsRouteWithPathParams(BoxedShortParameterController.ROUTE, pathParams(
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
        String route = buildWsRouteWithPathParams(BoxedIntegerParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testLong() throws IOException {
        String value = Long.toString(Long.MAX_VALUE);
        String route = buildWsRouteWithPathParams(BoxedLongParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testFloat() throws IOException {
        String value = Float.toString(Float.MAX_VALUE);
        String route = buildWsRouteWithPathParams(BoxedFloatParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testDouble() throws IOException {
        String value = Double.toString(Double.MAX_VALUE);
        String route = buildWsRouteWithPathParams(BoxedDoubleParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testBoolean() throws IOException {
        String value = Boolean.toString(true);
        String route = buildWsRouteWithPathParams(BoxedBooleanParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }

    @Test
    public void testCharacter() throws IOException {
        String value = Character.toString(Character.MAX_VALUE);
        String route = buildWsRouteWithPathParams(BoxedCharacterParameterController.ROUTE, pathParams(
            param("value", value)
        ));
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendStringAndAwaitResponse("").thenAccept(response ->
                Assert.assertEquals(value, response)
            ))
        );
    }
}
