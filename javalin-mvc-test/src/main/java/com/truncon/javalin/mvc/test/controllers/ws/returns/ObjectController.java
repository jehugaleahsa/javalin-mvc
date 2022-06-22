package com.truncon.javalin.mvc.test.controllers.ws.returns;

import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

@WsController(route = ObjectController.ROUTE)
public final class ObjectController {
    public static final String ROUTE = "/ws/returns/object";

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
    public PrimitiveModel onMessage(PrimitiveModel model) {
        return model;
    }
}
