package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.Collection;

@WsController(route = CollectionController.ROUTE)
public final class CollectionController {
    public static final String ROUTE = "/ws/parameters/collections/collection";

    @WsMessage
    public WsActionResult onMessage(@FromQuery("value") Collection<Double> values) {
        return new WsJsonResult(values);
    }
}
