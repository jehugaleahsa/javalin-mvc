package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.binary.ByteArrayController;
import com.truncon.javalin.mvc.test.controllers.ws.binary.ByteBufferController;
import com.truncon.javalin.mvc.test.controllers.ws.binary.ImplicitByteArrayController;
import com.truncon.javalin.mvc.test.controllers.ws.binary.InputStreamController;
import com.truncon.javalin.mvc.test.controllers.ws.models.BindNestedBinaryModelController;
import com.truncon.javalin.mvc.test.models.NestedBinaryModel;
import org.junit.Assert;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.truncon.javalin.mvc.test.QueryUtils.parseJson;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRoute;

public final class WsBinaryTest {
    @Test
    public void testByteArray() {
        String route = buildWsRoute(ByteArrayController.ROUTE);
        String message = "Hello, World!!!";
        ByteBuffer binary = StandardCharsets.UTF_8.encode(message);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendBinaryAndAwaitResponse(binary).thenAccept(response -> {
                String actual = StandardCharsets.UTF_8.decode(response).toString();
                Assert.assertEquals(message, actual);
            }))
        );
    }

    @Test
    public void testByteArray_implicit() {
        String route = buildWsRoute(ImplicitByteArrayController.ROUTE);
        String message = "Hello, World!!!";
        ByteBuffer binary = StandardCharsets.UTF_8.encode(message);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendBinaryAndAwaitResponse(binary).thenAccept(response -> {
                String actual = StandardCharsets.UTF_8.decode(response).toString();
                Assert.assertEquals(message, actual);
            }))
        );
    }

    @Test
    public void testByteBuffer() {
        String route = buildWsRoute(ByteBufferController.ROUTE);
        String message = "Hello, World!!!";
        ByteBuffer binary = StandardCharsets.UTF_8.encode(message);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendBinaryAndAwaitResponse(binary).thenAccept(response -> {
                String actual = StandardCharsets.UTF_8.decode(response).toString();
                Assert.assertEquals(message, actual);
            }))
        );
    }

    @Test
    public void testInputStream() {
        String route = buildWsRoute(InputStreamController.ROUTE);
        String message = "Hello, World!!!";
        ByteBuffer binary = StandardCharsets.UTF_8.encode(message);
        AsyncTestUtils.runTestAsync(app ->
            WsTestUtils.ws(route, session -> session.sendBinaryAndAwaitResponse(binary).thenAccept(response -> {
                String actual = StandardCharsets.UTF_8.decode(response).toString();
                Assert.assertEquals(message, actual);
            }))
        );
    }

    @Test
    public void testByteArray_nested() {
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
        Assert.assertNotNull(actual);
        String actualString = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(actual)).toString();
        Assert.assertEquals(expected, actualString);
    }
}
