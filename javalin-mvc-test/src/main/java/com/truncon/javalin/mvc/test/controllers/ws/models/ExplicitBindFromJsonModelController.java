package com.truncon.javalin.mvc.test.controllers.ws.models;

import com.truncon.javalin.mvc.api.FromJson;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

@WsController(route = ExplicitBindFromJsonModelController.ROUTE)
public final class ExplicitBindFromJsonModelController {
    public static final String ROUTE = "/ws/bind/models/json/explicit";

    @WsConnect
    public void onConnect(PrimitiveModel model) {
        // Parameter should be null
    }

    @WsDisconnect
    public void onDisconnect(PrimitiveModel model) {
        // Parameter should be null
    }

    @WsError
    public void onError(PrimitiveModel model) {
        // Parameter should be null
    }

    @WsMessage
    public WsActionResult onMessage(@FromJson PrimitiveModel model) {
        return new WsJsonResult(model);
    }
}
