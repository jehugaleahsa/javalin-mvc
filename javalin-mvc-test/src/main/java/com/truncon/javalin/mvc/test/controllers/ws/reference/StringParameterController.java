package com.truncon.javalin.mvc.test.controllers.ws.reference;

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

@WsController(route = StringParameterController.ROUTE)
public final class StringParameterController {
    public static final String ROUTE = "/ws/params/reference/string/{value}";

    @WsConnect
    public void connect(WsConnectContext context, String value) {
    }

    @WsClose
    public void close(WsCloseContext context, String value) {
    }

    @WsError
    public void error(WsErrorContext context, String value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, String value) {
        return new WsContentResult(value);
    }
}
