package com.truncon.javalin.mvc.test.controllers.ws.returns;

import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsMessage;

@WsController(route = IntegerController.ROUTE)
public final class IntegerController {
    public static final String ROUTE = "/ws/returns/integer";

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
    public Integer onMessage() {
        return 123;
    }
}
