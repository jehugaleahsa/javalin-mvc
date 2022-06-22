package com.truncon.javalin.mvc.test.controllers.ws.returns;

import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsMessage;

@WsController(route = PrimitiveIntController.ROUTE)
public final class PrimitiveIntController {
    public static final String ROUTE = "/ws/returns/int";

    @WsConnect
    public void onConnect() {
    }

    @WsDisconnect
    public void onDisconnect() {
    }

    @WsError
    public void onError() {
    }

    @WsMessage
    public int onMessage() {
        return 123;
    }
}
