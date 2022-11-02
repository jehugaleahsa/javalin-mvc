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

@WsController(route = BoxedBooleanParameterController.ROUTE)
public final class BoxedBooleanParameterController {
    public static final String ROUTE = "/ws/params/boxed/boolean/{value}";

    @WsConnect
    public void connect(WsConnectContext context, Boolean value) {
    }

    @WsClose
    public void close(WsCloseContext context, Boolean value) {
    }

    @WsError
    public void error(WsErrorContext context, Boolean value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, Boolean value) {
        return new WsContentResult(value == null ? null : Boolean.toString(value));
    }
}
