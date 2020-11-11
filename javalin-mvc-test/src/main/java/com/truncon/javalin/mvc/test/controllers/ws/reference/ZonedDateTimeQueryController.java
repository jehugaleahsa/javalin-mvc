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

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WsController(route = ZonedDateTimeQueryController.ROUTE)
public final class ZonedDateTimeQueryController {
    public static final String ROUTE = "/ws/params/reference/zoned-date-time";

    @WsConnect
    public void connect(WsConnectContext context, ZonedDateTime value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, ZonedDateTime value) {
    }

    @WsError
    public void error(WsErrorContext context, ZonedDateTime value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, ZonedDateTime value) {
        return new WsContentResult(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(value));
    }
}
