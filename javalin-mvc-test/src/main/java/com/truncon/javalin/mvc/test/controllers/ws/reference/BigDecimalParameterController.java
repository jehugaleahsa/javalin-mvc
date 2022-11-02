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

import java.math.BigDecimal;

@WsController(route = BigDecimalParameterController.ROUTE)
public final class BigDecimalParameterController {
    public static final String ROUTE = "/ws/params/reference/big-decimal/{value}";

    @WsConnect
    public void connect(WsConnectContext context, BigDecimal value) {
    }

    @WsClose
    public void close(WsCloseContext context, BigDecimal value) {
    }

    @WsError
    public void error(WsErrorContext context, BigDecimal value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, BigDecimal value) {
        return new WsContentResult(value == null ? null : value.toString());
    }
}
