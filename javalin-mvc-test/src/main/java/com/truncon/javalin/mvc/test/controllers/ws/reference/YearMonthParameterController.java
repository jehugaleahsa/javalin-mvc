package com.truncon.javalin.mvc.test.controllers.ws.reference;

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

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@WsController(route = YearMonthParameterController.ROUTE)
public final class YearMonthParameterController {
    public static final String ROUTE = "/ws/params/reference/year-month/{value}";
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");

    @WsConnect
    public void connect(WsConnectContext context, YearMonth value) {
    }

    @WsClose
    public void close(WsCloseContext context, YearMonth value) {
    }

    @WsError
    public void error(WsErrorContext context, YearMonth value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, YearMonth value) {
        return new WsContentResult(FORMATTER.format(value));
    }
}
