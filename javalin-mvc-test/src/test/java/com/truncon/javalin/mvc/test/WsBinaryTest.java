package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.binary.ByteArrayController;
import com.truncon.javalin.mvc.test.controllers.ws.binary.ByteBufferController;
import com.truncon.javalin.mvc.test.controllers.ws.binary.ImplicitByteArrayController;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import static com.truncon.javalin.mvc.test.RouteBuilder.buildWsRoute;

public final class WsBinaryTest {
    @Test
    public void testByteArray() throws IOException {
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
    public void testByteArray_implicit() throws IOException {
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
    public void testByteBuffer() throws IOException {
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
}
