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

@WsController(route = BoxedShortParameterController.ROUTE)
public final class BoxedShortParameterController {
    public static final String ROUTE = "/ws/params/boxed/short/{value}";

    @WsConnect
    public void connect(WsConnectContext context, Short value) {
    }

    @WsClose
    public void close(WsCloseContext context, Short value) {
    }

    @WsError
    public void error(WsErrorContext context, Short value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, Short value) {
        return new WsContentResult(value == null ? null : Short.toString(value));
    }
}
