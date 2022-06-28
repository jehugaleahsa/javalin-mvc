package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

@WsController(route = IterableController.ROUTE)
public final class IterableController {
    public static final String ROUTE = "/ws/parameters/collections/iterable";

    @WsMessage
    public WsActionResult onMessage(@FromQuery @Named("value") Iterable<Integer> values) {
        return new WsJsonResult(values);
    }
}
