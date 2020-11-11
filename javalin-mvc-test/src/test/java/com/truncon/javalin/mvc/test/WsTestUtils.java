package com.truncon.javalin.mvc.test;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public final class WsTestUtils {
    private Consumer<Session> onConnectHandler;
    private BiConsumer<Integer, String> onCloseHandler;
    private Consumer<String> onMessageHandler;
    private Consumer<Throwable> onErrorHandler;

    private WsTestUtils() {
    }

    public static CompletableFuture<Void> ws(String route, Function<SessionManager, CompletableFuture<Void>> testBody) {
        return ws(route, v -> {}, testBody);
    }

    public static CompletableFuture<Void> ws(
            String route,
            Consumer<WsTestUtils> consumer,
            Function<SessionManager, CompletableFuture<Void>> testBody) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                WebSocketClient client = new WebSocketClient();
                client.start();
                return client;
            } catch (Exception exception) {
                throw new CompletionException(exception);
            }
        }).thenCompose(client -> {
            WsTestUtils utils = new WsTestUtils();
            consumer.accept(utils);
            Socket socket = new Socket(utils);
            URI uri = URI.create(route);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            try {
                CompletableFuture<Session> future = (CompletableFuture<Session>) client.connect(socket, uri, request);
                return future.thenApply(session -> new SessionManager(client, socket, session))
                    .thenCompose(sm -> testBody.apply(sm).handleAsync((r, ex) -> {
                        sm.close();
                        if (ex != null) {
                            throw new CompletionException(ex);
                        }
                        return r;
                    }));
            } catch (IOException exception) {
                throw new UncheckedIOException(exception);
            }
        });
    }

    public void onConnect(Consumer<Session> consumer) {
        this.onConnectHandler = consumer;
    }

    public void onClose(BiConsumer<Integer, String> consumer) {
        this.onCloseHandler = consumer;
    }

    public void onMessage(Consumer<String> consumer) {
        this.onMessageHandler = consumer;
    }

    public void onError(Consumer<Throwable> consumer) {
        this.onErrorHandler = consumer;
    }

    public static class SessionManager {
        private final WebSocketClient client;
        private final Socket socket;
        private final Session session;

        private SessionManager(WebSocketClient client, Socket socket, Session session) {
            this.client = client;
            this.socket = socket;
            this.session = session;
        }

        public Session getSession() {
            return session;
        }

        public void close() {
            try {
                session.close(StatusCode.NORMAL, "OK");
                client.stop();
                client.destroy();
            } catch (Exception exception) {
                throw new CompletionException(exception);
            }
        }

        public void sendString(String message) {
            try {
                session.getRemote().sendString(message);
            } catch (IOException exception) {
                throw new UncheckedIOException(exception);
            }
        }

        public CompletableFuture<String> sendStringAndAwaitResponse(String message) {
            CompletableFuture<String> future = awaitMessage();
            sendString(message);
            return future;
        }

        public CompletableFuture<String> awaitMessage() {
            return socket.awaitMessage();
        }
    }

    @WebSocket
    public static final class Socket {
        private final WsTestUtils utils;
        private CountDownLatch messageLatch;
        private String message;

        private Socket(WsTestUtils utils) {
            this.utils = utils;
        }

        @OnWebSocketConnect
        public void onConnect(Session session) {
            if (utils.onConnectHandler != null) {
                utils.onConnectHandler.accept(session);
            }
        }

        @OnWebSocketClose
        public void onClose(int statusCode, String reason) {
            if (utils.onCloseHandler != null) {
                utils.onCloseHandler.accept(statusCode, reason);
            }
        }

        @OnWebSocketMessage
        public void onMessage(String message) {
            if (messageLatch != null) {
                messageLatch.countDown();
                this.message = message;
            }
            if (utils.onMessageHandler != null) {
                utils.onMessageHandler.accept(message);
            }
        }

        public CompletableFuture<String> awaitMessage() {
            messageLatch = new CountDownLatch(1);
            return CompletableFuture.supplyAsync(() -> {
                try {
                    messageLatch.await();
                    return message;
                } catch (InterruptedException exception) {
                    throw new CompletionException(exception);
                }

            });
        }

        @OnWebSocketError
        public void onError(Throwable cause) {
            if (utils.onErrorHandler != null) {
                utils.onErrorHandler.accept(cause);
            }
        }
    }
}
