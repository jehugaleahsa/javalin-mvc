package com.truncon.javalin.mvc.test.controllers.ws.boxed;

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

@WsController(route = BoxedLongParameterController.ROUTE)
public final class BoxedLongParameterController {
    public static final String ROUTE = "/ws/params/boxed/long/:value";

    @WsConnect
    public void connect(WsConnectContext context, Long value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, Long value) {
    }

    @WsError
    public void error(WsErrorContext context, Long value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, Long value) {
        return new WsContentResult(value == null ? null : Long.toString(value));
    }
}
