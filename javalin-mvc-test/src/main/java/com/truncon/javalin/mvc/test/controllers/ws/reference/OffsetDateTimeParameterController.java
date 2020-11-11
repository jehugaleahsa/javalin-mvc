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

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@WsController(route = OffsetDateTimeParameterController.ROUTE)
public final class OffsetDateTimeParameterController {
    public static final String ROUTE = "/ws/params/reference/offset-date-time/:value";

    @WsConnect
    public void connect(WsConnectContext context, OffsetDateTime value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, OffsetDateTime value) {
    }

    @WsError
    public void error(WsErrorContext context, OffsetDateTime value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, OffsetDateTime value) {
        return new WsContentResult(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value));
    }
}
