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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WsController(route = LocalDateParameterController.ROUTE)
public final class LocalDateParameterController {
    public static final String ROUTE = "/ws/params/reference/local-date/{value}";

    @WsConnect
    public void connect(WsConnectContext context, LocalDate value) {
    }

    @WsClose
    public void close(WsCloseContext context, LocalDate value) {
    }

    @WsError
    public void error(WsErrorContext context, LocalDate value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, LocalDate value) {
        return new WsContentResult(DateTimeFormatter.ISO_LOCAL_DATE.format(value));
    }
}
