package com.truncon.javalin.mvc.test.controllers.ws.reference;

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

import java.util.UUID;

@WsController(route = UuidParameterController.ROUTE)
public final class UuidParameterController {
    public static final String ROUTE = "/ws/params/reference/uuid/:value";

    @WsConnect
    public void connect(WsConnectContext context, UUID value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, UUID value) {
    }

    @WsError
    public void error(WsErrorContext context, UUID value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, UUID value) {
        return new WsContentResult(value == null ? null : value.toString());
    }
}
