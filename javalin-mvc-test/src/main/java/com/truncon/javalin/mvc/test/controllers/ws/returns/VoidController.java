package com.truncon.javalin.mvc.test.controllers.ws.returns;

import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.api.ws.WsResponse;

@WsController(route = VoidController.ROUTE)
public final class VoidController {
    public static final String ROUTE = "/ws/returns/void";

    @WsConnect
    public void onConnect() {
    }

    @WsClose
    public void onClose() {
    }

    @WsError
    public void onError() {
    }

    @WsMessage
    public void onMessage(WsResponse response) {
        response.sendText("A-okay!");
    }
}
