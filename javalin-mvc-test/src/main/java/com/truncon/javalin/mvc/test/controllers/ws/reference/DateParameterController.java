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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@WsController(route = DateParameterController.ROUTE)
public final class DateParameterController {
    public static final String ROUTE = "/ws/params/reference/date/:value";
    public static final SimpleDateFormat FORMATTER = getDateFormatter();

    private static SimpleDateFormat getDateFormatter() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter;
    }

    @WsConnect
    public void connect(WsConnectContext context, Date value) {
    }

    @WsDisconnect
    public void disconnect(WsDisconnectContext context, Date value) {
    }

    @WsError
    public void error(WsErrorContext context, Date value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, Date value) {
        return new WsContentResult(value == null ? null : FORMATTER.format(value));
    }
}
