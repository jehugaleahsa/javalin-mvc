package com.truncon.javalin.mvc.test.controllers.ws.models;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

@WsController(route = BindFromJsonModelController.ROUTE)
public final class BindFromJsonModelController {
    public static final String ROUTE = "/ws/bind/models/json";

    @WsConnect
    public void onConnect(PrimitiveModel model) {
        // Parameter should be null
    }

    @WsClose
    public void onClose(PrimitiveModel model) {
        // Parameter should be null
    }

    @WsError
    public void onError(PrimitiveModel model) {
        // Parameter should be null
    }

    @WsMessage
    public WsActionResult onMessage(PrimitiveModel model) {
        return new WsJsonResult(model);
    }
}
