package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.SortedSet;

@WsController(route = SortedSetController.ROUTE)
public final class SortedSetController {
    public static final String ROUTE = "/ws/parameters/collections/sorted_set";

    @WsMessage
    public WsActionResult onMessage(@FromQuery("value") SortedSet<String> values) {
        return new WsJsonResult(values);
    }
}
