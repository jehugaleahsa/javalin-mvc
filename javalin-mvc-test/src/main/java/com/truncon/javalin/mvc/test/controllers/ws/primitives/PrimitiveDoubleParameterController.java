package com.truncon.javalin.mvc.test.controllers.ws.primitives;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContentResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsDisconnect;
import com.truncon.javalin.mvc.api.ws.WsDisconnectContext;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;

@WsController(route = PrimitiveDoubleParameterController.ROUTE)
public final class PrimitiveDoubleParameterController {
    public static final String ROUTE = "/ws/params/primitives/double/{value}";

    @WsConnect
    public void connect(WsConnectContext context, double value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, double value) {
    }

    @WsError
    public void error(WsErrorContext context, double value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, double value) {
        return new WsContentResult(Double.toString(value));
    }
}
