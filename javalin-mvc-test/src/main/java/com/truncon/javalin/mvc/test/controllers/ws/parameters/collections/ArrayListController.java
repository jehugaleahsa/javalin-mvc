package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.ArrayList;
import java.util.UUID;

@WsController(route = ArrayListController.ROUTE)
public final class ArrayListController {
    public static final String ROUTE = "/ws/parameters/collections/array_list";

    @WsMessage
    public WsActionResult onMessage(@FromQuery @Named("value") ArrayList<UUID> values) {
        return new WsJsonResult(values);
    }
}
