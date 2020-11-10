package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.ws.PrimitiveIntegerParameterController;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.junit.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;

public final class WsPrimitiveParamTest {
    @Test
    public void test() {
        AppHost.startNew().thenCompose(a -> {
            Throwable ex = null;
            WebSocketClient client = new WebSocketClient();
            try {
                client.start();
                Socket socket = new Socket();
                URI uri = URI.create("ws://localhost:" + AppHost.PORT + PrimitiveIntegerParameterController.ROUTE);
                ClientUpgradeRequest request = new ClientUpgradeRequest();
                client.connect(socket, uri, request);
                socket.awaitClose();
            } catch (Throwable throwable) {
                ex = throwable;
            } finally {
                try {
                    client.stop();
                } catch (Exception exception) {
                    ex = exception;
                }
            }
            Throwable error = ex;
            return a.stop().handle((result, closeError) -> {
                if (error != null) {
                    throw new CompletionException(error);
                } else if (closeError != null) {
                    throw new CompletionException(closeError);
                }
                return result;
            });
        }).join();
    }

    @WebSocket
    public static final class Socket {
        private final CountDownLatch closeLatch = new CountDownLatch(1);
        private Session session;

        public void awaitClose() throws InterruptedException {
            this.closeLatch.await();
        }

        @OnWebSocketConnect
        public void onConnect(Session session) {
            System.out.println("client connected");
            this.session = session;
            try {
                this.session.getRemote().sendString("Hello");
                this.session.getRemote().flush();
            } catch (IOException exception) {
                throw new UncheckedIOException(exception);
            }
        }

        @OnWebSocketClose
        public void onClose(int statusCode, String reason) {
            System.out.println("client closed: " + statusCode + ": " + reason);
            this.session = null;
            this.closeLatch.countDown();
        }

        @OnWebSocketMessage
        public void onMessage(String message) {
            System.out.println("client message: " + message);
            session.close(StatusCode.NORMAL, "complete");
        }

        @OnWebSocketError
        public void onError(Throwable cause) {
            System.out.println("client error: " + cause.getMessage());
            throw new RuntimeException(cause);
        }
    }
}
