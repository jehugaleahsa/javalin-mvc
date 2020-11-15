package com.truncon.javalin.mvc.test.controllers.ws.conversion;

import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.ConversionModel;

@WsController(route = WsContextNameSourceController.ROUTE)
public final class WsContextNameSourceController {
    public static final String ROUTE = "/ws/bind/models/conversion/context-name-source";

    @WsConnect
    public void onConnect(@UseConverter("static-model-converter-ws-context-name-source") ConversionModel model) {
    }

    @WsDisconnect
    public void onDisconnect(@UseConverter("static-model-converter-ws-context-name-source") ConversionModel model) {
    }

    @WsError
    public void onError(@UseConverter("static-model-converter-ws-context-name-source") ConversionModel model) {
    }

    @WsMessage
    public WsJsonResult onMessage(@UseConverter("static-model-converter-ws-context-name-source") ConversionModel model) {
        return new WsJsonResult(model);
    }
}
