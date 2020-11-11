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

@WsController(route = PrimitiveBooleanParameterController.ROUTE)
public final class PrimitiveBooleanParameterController {
    public static final String ROUTE = "/ws/params/primitives/boolean/:value";

    @WsConnect
    public void connect(WsConnectContext context, boolean value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, boolean value) {
    }

    @WsError
    public void error(WsErrorContext context, boolean value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, boolean value) {
        return new WsContentResult(Boolean.toString(value));
    }
}
