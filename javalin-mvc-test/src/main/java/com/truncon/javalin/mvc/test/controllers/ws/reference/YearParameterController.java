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

import java.time.Year;
import java.time.format.DateTimeFormatter;

@WsController(route = YearParameterController.ROUTE)
public final class YearParameterController {
    public static final String ROUTE = "/ws/params/reference/year/:value";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy");

    @WsConnect
    public void connect(WsConnectContext context, Year value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, Year value) {
    }

    @WsError
    public void error(WsErrorContext context, Year value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, Year value) {
        return new WsContentResult(FORMATTER.format(value));
    }
}
