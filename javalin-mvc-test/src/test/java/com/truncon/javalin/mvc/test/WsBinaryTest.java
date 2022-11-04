package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.binary.ByteArrayController;
import com.truncon.javalin.mvc.test.controllers.ws.binary.ByteBufferController;
import com.truncon.javalin.mvc.test.controllers.ws.binary.ImplicitByteArrayController;
import com.truncon.javalin.mvc.test.controllers.ws.binary.InputStreamController;
import com.truncon.javalin.mvc.test.controllers.ws.models.BindNestedBinaryModelController;
import com.truncon.javalin.mvc.test.models.NestedBinaryModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.truncon.javalin.mvc.test.QueryUtils.parseJson;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRoute;

final class WsBinaryTest {
    @Test
    void testByteArray() {
        String route = buildWsRoute(ByteArrayController.ROUTE);
        String message = "Hello, World!!!";
        ByteBuffer binary = StandardCharsets.UTF_8.encode(message);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendBinaryAndAwaitResponse(binary).thenAccept(response -> {
                String actual = StandardCharsets.UTF_8.decode(response).toString();
                Assertions.assertEquals(message, actual);
            }))
        );
    }

    @Test
    void testByteArray_implicit() {
        String route = buildWsRoute(ImplicitByteArrayController.ROUTE);
        String message = "Hello, World!!!";
        ByteBuffer binary = StandardCharsets.UTF_8.encode(message);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendBinaryAndAwaitResponse(binary).thenAccept(response -> {
                String actual = StandardCharsets.UTF_8.decode(response).toString();
                Assertions.assertEquals(message, actual);
            }))
        );
    }

    @Test
    void testByteBuffer() {
        String route = buildWsRoute(ByteBufferController.ROUTE);
        String message = "Hello, World!!!";
        ByteBuffer binary = StandardCharsets.UTF_8.encode(message);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendBinaryAndAwaitResponse(binary).thenAccept(response -> {
                String actual = StandardCharsets.UTF_8.decode(response).toString();
                Assertions.assertEquals(message, actual);
            }))
        );
    }

    @Test
    void testInputStream() {
        String route = buildWsRoute(InputStreamController.ROUTE);
        String message = "Hello, World!!!";
        ByteBuffer binary = StandardCharsets.UTF_8.encode(message);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendBinaryAndAwaitResponse(binary).thenAccept(response -> {
                String actual = StandardCharsets.UTF_8.decode(response).toString();
                Assertions.assertEquals(message, actual);
            }))
        );
    }

    @Test
    void testByteArray_nested() {
        String route = buildWsRoute(BindNestedBinaryModelController.ROUTE);
        String message = "Hello, World!!!";
        ByteBuffer binary = StandardCharsets.UTF_8.encode(message);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendBinaryAndAwaitStringResponse(binary).thenAccept(response -> {
                NestedBinaryModel model = parseJson(response, NestedBinaryModel.class);
                assertBinaryMessage(message, model.field);
                assertBinaryMessage(message, model.getSetter());
                assertBinaryMessage(message, model.getParameter());
            }))
        );
    }

    private void assertBinaryMessage(String expected, byte[] actual) {
        Assertions.assertNotNull(actual);
        String actualString = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(actual)).toString();
        Assertions.assertEquals(expected, actualString);
    }
}
