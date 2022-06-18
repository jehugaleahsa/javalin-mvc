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

import java.math.BigInteger;

@WsController(route = BigIntegerParameterController.ROUTE)
public final class BigIntegerParameterController {
    public static final String ROUTE = "/ws/params/reference/big-integer/{value}";

    @WsConnect
    public void connect(WsConnectContext context, BigInteger value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, BigInteger value) {
    }

    @WsError
    public void error(WsErrorContext context, BigInteger value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, BigInteger value) {
        return new WsContentResult(value == null ? null : value.toString());
    }
}
