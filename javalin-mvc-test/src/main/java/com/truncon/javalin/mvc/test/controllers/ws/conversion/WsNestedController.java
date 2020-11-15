package com.truncon.javalin.mvc.test.controllers.ws.conversion;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.WsModelWithConversionModel;

@WsController(route = WsNestedController.ROUTE)
public final class WsNestedController {
    public static final String ROUTE = "/ws/bind/models/conversion/nested";

    @WsConnect
    public void onConnect(@FromQuery WsModelWithConversionModel model) {
    }

    @WsDisconnect
    public void onDisconnect(@FromQuery WsModelWithConversionModel model) {
    }

    @WsError
    public void onError(@FromQuery WsModelWithConversionModel model) {
    }

    @WsMessage
    public WsJsonResult onMessage(@FromQuery WsModelWithConversionModel model) {
        return new WsJsonResult(model);
    }
}
