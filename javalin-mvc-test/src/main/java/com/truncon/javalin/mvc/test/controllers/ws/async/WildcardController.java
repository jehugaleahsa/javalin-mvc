package com.truncon.javalin.mvc.test.controllers.ws.async;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsDisconnectContext;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

import java.util.concurrent.CompletableFuture;

@WsController(route = WildcardController.ROUTE)
public final class WildcardController {
    public static final String ROUTE = "/ws/async/wildcard";

    @WsConnect
    public CompletableFuture<Void> onConnect(WsConnectContext ctx) {
        return CompletableFuture.completedFuture(null);
    }

    @WsDisconnect
    public CompletableFuture<Void> onDisconnect(WsDisconnectContext ctx) {
        return CompletableFuture.completedFuture(null);
    }

    @WsError
    public CompletableFuture<Void> onError(WsErrorContext ctx) {
        return CompletableFuture.completedFuture(null);
    }

    @WsMessage
    public CompletableFuture<?> onMessage(PrimitiveModel model) {
        return CompletableFuture.supplyAsync(() -> new WsJsonResult(model));
    }
}
