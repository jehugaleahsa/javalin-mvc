package com.truncon.javalin.mvc.test.controllers.ws.conversion;

import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.ConversionModel;

@WsController(route = WsMultipleContextTypeController.ROUTE)
public final class WsMultipleContextTypeController {
    public static final String ROUTE = "/ws/bind/models/conversion/context-types";

    @WsConnect
    public void onConnect(@UseConverter("ws-connect-context-converter") ConversionModel model) {
    }

    @WsDisconnect
    public void onDisconnect(@UseConverter("ws-disconnect-context-converter") ConversionModel model) {
    }

    @WsError
    public void onError(@UseConverter("ws-error-context-converter") ConversionModel model) {
    }

    @WsMessage
    public WsJsonResult onMessage(@UseConverter("ws-message-context-converter") ConversionModel model) {
        return new WsJsonResult(model);
    }
}
