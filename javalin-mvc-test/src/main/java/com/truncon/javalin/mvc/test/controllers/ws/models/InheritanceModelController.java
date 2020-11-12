package com.truncon.javalin.mvc.test.controllers.ws.models;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.DerivedModel;

@WsController(route = InheritanceModelController.ROUTE)
public final class InheritanceModelController {
    public static final String ROUTE = "/ws/bind/models/inheritance";

    @WsConnect
    public void onConnect(@FromQuery DerivedModel model) {
        // Parameter should be null
    }

    @WsDisconnect
    public void onDisconnect(@FromQuery DerivedModel model) {
        // Parameter should be null
    }

    @WsError
    public void onError(@FromQuery DerivedModel model) {
        // Parameter should be null
    }

    @WsMessage
    public WsActionResult onMessage(@FromQuery DerivedModel model) {
        return new WsJsonResult(model);
    }
}
