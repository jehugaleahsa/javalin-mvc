package com.truncon.javalin.mvc.test.controllers.ws.binary;

import com.truncon.javalin.mvc.api.ws.FromBinary;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsBinaryMessage;
import com.truncon.javalin.mvc.api.ws.WsBinaryResult;
import com.truncon.javalin.mvc.api.ws.WsController;

@WsController(route = ByteArrayController.ROUTE)
public final class ByteArrayController {
    public static final String ROUTE = "/ws/bind/binary/byte-array";

    @WsBinaryMessage
    public WsActionResult getBinaryData(@FromBinary byte[] data) {
        return new WsBinaryResult(data);
    }
}
