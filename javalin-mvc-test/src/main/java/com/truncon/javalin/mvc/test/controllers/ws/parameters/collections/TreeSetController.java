package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.TreeSet;

@WsController(route = TreeSetController.ROUTE)
public final class TreeSetController {
    public static final String ROUTE = "/ws/parameters/collections/tree_set";

    @WsMessage
    public WsActionResult onMessage(@FromQuery("value") TreeSet<String> values) {
        return new WsJsonResult(values);
    }
}
