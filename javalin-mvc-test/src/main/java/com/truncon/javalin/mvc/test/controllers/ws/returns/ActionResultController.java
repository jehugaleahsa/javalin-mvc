package com.truncon.javalin.mvc.test.controllers.ws.returns;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

@WsController(route = ActionResultController.ROUTE)
public final class ActionResultController {
    public static final String ROUTE = "/ws/returns/action_result";

    @WsConnect
    public void onConnect() {
    }

    @WsClose
    public void onClose() {
    }

    @WsError
    public void onError() {
    }

    @WsMessage
    public WsActionResult onMessage(PrimitiveModel model) {
        return new WsJsonResult(model);
    }
}
