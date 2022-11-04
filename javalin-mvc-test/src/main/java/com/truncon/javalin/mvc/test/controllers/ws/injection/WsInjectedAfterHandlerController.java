package com.truncon.javalin.mvc.test.controllers.ws.injection;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsAfter;
import com.truncon.javalin.mvc.api.ws.WsContentResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.handlers.WsInjectionHandler;

@WsController(route = WsInjectedAfterHandlerController.ROUTE)
public final class WsInjectedAfterHandlerController {
    public static final String ROUTE = "/ws/bind/injection/after-handler";
    public static final String CONTROLLER_MESSAGE = "controller-message";

    @WsMessage
    @WsAfter(handler = WsInjectionHandler.class)
    public WsActionResult onMessage() {
        return new WsContentResult(CONTROLLER_MESSAGE);
    }
}
