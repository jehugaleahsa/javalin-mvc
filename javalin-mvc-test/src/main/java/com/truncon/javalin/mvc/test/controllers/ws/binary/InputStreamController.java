package com.truncon.javalin.mvc.test.controllers.ws.binary;

import com.truncon.javalin.mvc.api.FromBody;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsBinaryMessage;
import com.truncon.javalin.mvc.api.ws.WsBinaryResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

@WsController(route = InputStreamController.ROUTE)
public final class InputStreamController {
    public static final String ROUTE = "/ws/bind/binary/input-stream";

    @WsBinaryMessage
    public WsActionResult getBinaryData(@FromBody InputStream stream) throws IOException {
        byte[] data = IOUtils.toByteArray(stream);
        return new WsBinaryResult(data);
    }
}
