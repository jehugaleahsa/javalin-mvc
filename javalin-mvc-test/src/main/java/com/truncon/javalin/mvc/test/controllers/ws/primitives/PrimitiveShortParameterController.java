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

@WsController(route = PrimitiveShortParameterController.ROUTE)
public final class PrimitiveShortParameterController {
    public static final String ROUTE = "/ws/params/primitives/short/:value";

    @WsConnect
    public void connect(WsConnectContext context, short value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, short value) {
    }

    @WsError
    public void error(WsErrorContext context, short value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, short value) {
        return new WsContentResult(Integer.toString(value));
    }
}
