package com.truncon.javalin.mvc.test.controllers.ws.injection;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.InjectionModel;

@WsController(route = WsInjectedModelController.ROUTE)
public final class WsInjectedModelController {
    public static final String ROUTE = "/ws/bind/injection/model";

    @WsMessage
    public WsActionResult onMessage(InjectionModel model) {
        return new WsJsonResult(model);
    }
}
