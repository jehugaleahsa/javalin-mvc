package com.truncon.javalin.mvc.test.controllers.ws.returns;

import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

@WsController(route = ObjectController.ROUTE)
public final class ObjectController {
    public static final String ROUTE = "/ws/returns/object";

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
    public PrimitiveModel onMessage(PrimitiveModel model) {
        return model;
    }
}
