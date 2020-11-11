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

@WsController(route = BoxedByteParameterController.ROUTE)
public final class BoxedByteParameterController {
    public static final String ROUTE = "/ws/params/boxed/byte/:value";

    @WsConnect
    public void connect(WsConnectContext context, Byte value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, Byte value) {
    }

    @WsError
    public void error(WsErrorContext context, Byte value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, Byte value) {
        return new WsContentResult(value == null ? null : Byte.toString(value));
    }
}
