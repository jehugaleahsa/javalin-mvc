package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.Set;

@WsController(route = SetController.ROUTE)
public final class SetController {
    public static final String ROUTE = "/ws/parameters/collections/set";

    @WsMessage
    public WsActionResult onMessage(@FromQuery("value") Set<String> values) {
        return new WsJsonResult(values);
    }
}
