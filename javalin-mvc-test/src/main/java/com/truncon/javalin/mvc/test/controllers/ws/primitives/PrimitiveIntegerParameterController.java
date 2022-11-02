package com.truncon.javalin.mvc.test.controllers.ws.primitives;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContentResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
import com.truncon.javalin.mvc.api.ws.WsCloseContext;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;

@WsController(route = PrimitiveIntegerParameterController.ROUTE)
public final class PrimitiveIntegerParameterController {
    public static final String ROUTE = "/ws/params/primitives/integer/{value}";

    @WsConnect
    public void connect(WsConnectContext context, int value) {
    }

    @WsClose
    public void close(WsCloseContext context, int value) {
    }

    @WsError
    public void error(WsErrorContext context, int value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, int value) {
        return new WsContentResult(Integer.toString(value));
    }
}
