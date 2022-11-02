package com.truncon.javalin.mvc.test.controllers.ws.boxed;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsCloseContext;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContentResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;

@WsController(route = BoxedCharacterParameterController.ROUTE)
public final class BoxedCharacterParameterController {
    public static final String ROUTE = "/ws/params/boxed/character/{value}";

    @WsConnect
    public void connect(WsConnectContext context, Character value) {
    }

    @WsClose
    public void close(WsCloseContext context, Character value) {
    }

    @WsError
    public void error(WsErrorContext context, Character value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, Character value) {
        return new WsContentResult(value == null ? null : Character.toString(value));
    }
}
