package com.truncon.javalin.mvc.test.controllers.ws.models;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsBinaryMessage;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.test.models.NestedBinaryModel;

@WsController(route = BindNestedBinaryModelController.ROUTE)
public final class BindNestedBinaryModelController {
    public static final String ROUTE = "/ws/bind/models/nested/binary";

    @WsBinaryMessage
    public WsActionResult onBinaryMessage(NestedBinaryModel model) {
        return new WsJsonResult(model);
    }
}
