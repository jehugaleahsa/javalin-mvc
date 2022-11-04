package com.truncon.javalin.mvc.test.controllers.ws.injection;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.utils.Dependency;

import javax.inject.Inject;

@WsController(route = WsInjectedControllerController.ROUTE)
public final class WsInjectedControllerController {
    public static final String ROUTE = "/ws/bind/injection/controller";
    private final Dependency dependency;

    @Inject
    public WsInjectedControllerController(Dependency dependency) {
        this.dependency = dependency;
    }

    @WsMessage
    public WsActionResult onMessage() {
        return new WsJsonResult(dependency.getValue());
    }
}
