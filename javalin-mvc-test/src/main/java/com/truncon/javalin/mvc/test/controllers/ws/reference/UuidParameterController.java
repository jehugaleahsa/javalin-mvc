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

import java.util.UUID;

@WsController(route = UuidParameterController.ROUTE)
public final class UuidParameterController {
    public static final String ROUTE = "/ws/params/reference/uuid/{value}";

    @WsConnect
    public void connect(WsConnectContext context, UUID value) {
    }

    @WsClose
    public void close(WsCloseContext context, UUID value) {
    }

    @WsError
    public void error(WsErrorContext context, UUID value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, UUID value) {
        return new WsContentResult(value == null ? null : value.toString());
    }
}
