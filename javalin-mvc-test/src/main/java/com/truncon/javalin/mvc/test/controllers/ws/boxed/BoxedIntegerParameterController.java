package com.truncon.javalin.mvc.test.controllers.ws.boxed;

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

@WsController(route = BoxedIntegerParameterController.ROUTE)
public final class BoxedIntegerParameterController {
    public static final String ROUTE = "/ws/params/boxed/integer/{value}";

    @WsConnect
    public void connect(WsConnectContext context, Integer value) {
    }

    @WsClose
    public void close(WsCloseContext context, Integer value) {
    }

    @WsError
    public void error(WsErrorContext context, Integer value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, Integer value) {
        return new WsContentResult(value == null ? null : Integer.toString(value));
    }
}
