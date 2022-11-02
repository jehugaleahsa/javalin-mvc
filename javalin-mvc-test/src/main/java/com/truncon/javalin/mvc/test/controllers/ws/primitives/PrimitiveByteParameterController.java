package com.truncon.javalin.mvc.test.controllers.ws.primitives;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsConnect;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContentResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsClose;
import com.truncon.javalin.mvc.api.ws.WsCloseContext;
import com.truncon.javalin.mvc.api.ws.WsError;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;

@WsController(route = PrimitiveByteParameterController.ROUTE)
public final class PrimitiveByteParameterController {
    public static final String ROUTE = "/ws/params/primitives/byte/{value}";

    @WsConnect
    public void connect(WsConnectContext context, byte value) {
    }

    @WsClose
    public void close(WsCloseContext context, byte value) {
    }

    @WsError
    public void error(WsErrorContext context, byte value) {
    }

    @WsMessage
    public WsActionResult message(WsMessageContext context, byte value) {
        return new WsContentResult(Byte.toString(value));
    }
}
