package com.truncon.javalin.mvc.test.controllers.ws.primitives;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsCloseContext;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContentResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
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

    @WsClose
    public void close(WsCloseContext context, double value) {
    }

    @WsError
    public void error(WsErrorContext context, double value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, double value) {
        return new WsContentResult(Double.toString(value));
    }
}
