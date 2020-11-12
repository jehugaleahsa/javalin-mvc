package com.truncon.javalin.mvc.test.controllers.ws.models;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

@WsController(route = BindSettersFromQueryModelController.ROUTE)
public final class BindSettersFromQueryModelController {
    public static final String ROUTE = "/ws/bind/models/setters/with-source";

    @WsConnect
    public void onConnect(@FromQuery PrimitiveModel model) {
    }

    @WsDisconnect
    public void onDisconnect(@FromQuery PrimitiveModel model) {
    }

    @WsError
    public void onError(@FromQuery PrimitiveModel model) {
    }

    @WsMessage
    public WsActionResult onMessage(@FromQuery PrimitiveModel model) {
        return new WsJsonResult(model);
    }
}
