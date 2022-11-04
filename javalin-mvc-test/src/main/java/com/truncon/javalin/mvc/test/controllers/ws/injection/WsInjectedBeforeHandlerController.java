package com.truncon.javalin.mvc.test.controllers.ws.injection;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsBefore;
import com.truncon.javalin.mvc.api.ws.WsContentResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.handlers.WsInjectionHandler;

@WsController(route = WsInjectedBeforeHandlerController.ROUTE)
public final class WsInjectedBeforeHandlerController {
    public static final String ROUTE = "/ws/bind/injection/before-handler";

    @WsMessage
    @WsBefore(handler = WsInjectionHandler.class)
    public WsActionResult onMessage() {
        return new WsContentResult("If you see this, the request did not get cancelled.");
    }
}
