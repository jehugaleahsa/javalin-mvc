package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.HashSet;

@WsController(route = HashSetController.ROUTE)
public final class HashSetController {
    public static final String ROUTE = "/ws/parameters/collections/hash_set";

    @WsMessage
    public WsActionResult onMessage(@FromQuery @Named("value") HashSet<String> values) {
        return new WsJsonResult(values);
    }
}
