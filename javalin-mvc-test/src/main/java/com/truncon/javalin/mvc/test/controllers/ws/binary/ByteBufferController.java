package com.truncon.javalin.mvc.test.controllers.ws.binary;

import com.truncon.javalin.mvc.api.FromBody;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsBinaryMessage;
import com.truncon.javalin.mvc.api.ws.WsBinaryResult;
import com.truncon.javalin.mvc.api.ws.WsController;

import java.nio.ByteBuffer;

@WsController(route = ByteBufferController.ROUTE)
public final class ByteBufferController {
    public static final String ROUTE = "/ws/bind/binary/byte-buffer";

    @WsBinaryMessage
    public WsActionResult getBinaryData(@FromBody ByteBuffer data) {
        return new WsBinaryResult(data);
    }
}
