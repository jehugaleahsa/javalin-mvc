package com.truncon.javalin.mvc.test.controllers.ws.parameters.collections;

import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;

import java.util.List;

@WsController(route = ListController.ROUTE)
public final class ListController {
    public static final String ROUTE = "/ws/parameters/collections/list";

    @WsMessage
    public WsActionResult onMessage(@FromQuery @Named("value") List<String> values) {
        return new WsJsonResult(values);
    }
}
