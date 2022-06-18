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

@WsController(route = PrimitiveCharacterParameterController.ROUTE)
public final class PrimitiveCharacterParameterController {
    public static final String ROUTE = "/ws/params/primitives/character/{value}";

    @WsConnect
    public void connect(WsConnectContext context, char value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, char value) {
    }

    @WsError
    public void error(WsErrorContext context, char value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, char value) {
        return new WsContentResult(Character.toString(value));
    }
}
