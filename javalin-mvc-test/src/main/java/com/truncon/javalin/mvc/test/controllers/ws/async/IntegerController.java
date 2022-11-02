package com.truncon.javalin.mvc.test.controllers.ws.async;

import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
import com.truncon.javalin.mvc.api.ws.WsCloseContext;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.concurrent.CompletableFuture;

@WsController(route = IntegerController.ROUTE)
public final class IntegerController {
    public static final String ROUTE = "/ws/async/integer";

    @WsConnect
    public CompletableFuture<Void> onConnect(WsConnectContext ctx) {
        return CompletableFuture.completedFuture(null);
    }

    @WsClose
    public CompletableFuture<Void> onClose(WsCloseContext ctx) {
        return CompletableFuture.completedFuture(null);
    }

    @WsError
    public CompletableFuture<Void> onError(WsErrorContext ctx) {
        return CompletableFuture.completedFuture(null);
    }

    @WsMessage
    public CompletableFuture<Integer> onMessage() {
        return CompletableFuture.supplyAsync(() -> 123);
    }
}
