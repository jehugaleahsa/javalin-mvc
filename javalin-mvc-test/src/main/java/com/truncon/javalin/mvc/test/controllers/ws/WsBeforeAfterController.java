package com.truncon.javalin.mvc.test.controllers.ws;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsAfter;
import com.truncon.javalin.mvc.api.ws.WsBefore;
import com.truncon.javalin.mvc.api.ws.WsContentResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.handlers.WsLog;

@WsController(route = WsBeforeAfterController.ROUTE)
public final class WsBeforeAfterController {
    public static final String ROUTE = "/ws/before-after";

    @WsMessage
    @WsBefore(handler = WsLog.class, arguments = { "before-after-controller" })
    @WsAfter(handler = WsLog.class, arguments = { "before-after-controller" })
    public WsActionResult onMessage() {
        return new WsContentResult("Hello");
    }
}
