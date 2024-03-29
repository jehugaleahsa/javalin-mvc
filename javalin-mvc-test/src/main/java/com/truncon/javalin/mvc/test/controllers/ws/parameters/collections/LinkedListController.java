package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.LinkedList;

@WsController(route = LinkedListController.ROUTE)
public final class LinkedListController {
    public static final String ROUTE = "/ws/parameters/collections/linked_list";

    @WsMessage
    public WsActionResult onMessage(@FromQuery("value") LinkedList<String> values) {
        return new WsJsonResult(values);
    }
}
