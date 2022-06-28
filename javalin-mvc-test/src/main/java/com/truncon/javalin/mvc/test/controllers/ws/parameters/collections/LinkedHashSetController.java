package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.LinkedHashSet;

@WsController(route = LinkedHashSetController.ROUTE)
public final class LinkedHashSetController {
    public static final String ROUTE = "/ws/parameters/collections/linked_hash_set";

    @WsMessage
    public WsActionResult onMessage(@FromQuery @Named("value") LinkedHashSet<String> values) {
        return new WsJsonResult(values);
    }
}
