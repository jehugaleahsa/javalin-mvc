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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WsController(route = LocalDateTimeParameterController.ROUTE)
public final class LocalDateTimeParameterController {
    public static final String ROUTE = "/ws/params/reference/local-date-time/{value}";

    @WsConnect
    public void connect(WsConnectContext context, LocalDateTime value) {
    }

    @WsClose
    public void close(WsCloseContext context, LocalDateTime value) {
    }

    @WsError
    public void error(WsErrorContext context, LocalDateTime value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, LocalDateTime value) {
        return new WsContentResult(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value));
    }
}
