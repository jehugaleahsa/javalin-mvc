package com.truncon.javalin.mvc.test.controllers.ws.binary;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsBinaryMessage;
import com.truncon.javalin.mvc.api.ws.WsBinaryResult;
import com.truncon.javalin.mvc.api.ws.WsController;

@WsController(route = ImplicitByteArrayController.ROUTE)
public final class ImplicitByteArrayController {
    public static final String ROUTE = "/ws/bind/binary/byte-array/implicit";

    @WsBinaryMessage
    public WsActionResult getBinaryData(byte[] data) {
        return new WsBinaryResult(data);
    }
}
