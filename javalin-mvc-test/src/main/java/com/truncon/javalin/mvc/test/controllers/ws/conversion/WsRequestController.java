package com.truncon.javalin.mvc.test.controllers.ws.conversion;

import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.ConversionModel;

@WsController(route = WsRequestController.ROUTE)
public final class WsRequestController {
    public static final String ROUTE = "/ws/bind/models/conversion/request";

    @WsConnect
    public void onConnect(@UseConverter("static-model-converter-ws-request") ConversionModel model) {
    }

    @WsClose
    public void onClose(@UseConverter("static-model-converter-ws-request") ConversionModel model) {
    }

    @WsError
    public void onError(@UseConverter("static-model-converter-ws-request") ConversionModel model) {
    }

    @WsMessage
    public WsJsonResult onMessage(@UseConverter("static-model-converter-ws-request") ConversionModel model) {
        return new WsJsonResult(model);
    }
}
