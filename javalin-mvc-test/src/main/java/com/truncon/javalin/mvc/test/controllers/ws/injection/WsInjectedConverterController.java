package com.truncon.javalin.mvc.test.controllers.ws.injection;

import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsContentResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsMessage;

@WsController(route = WsInjectedConverterController.ROUTE)
public final class WsInjectedConverterController {
    public static final String ROUTE = "/ws/bind/injection/converter";

    @WsMessage
    public WsActionResult onMessage(@UseConverter("ws-local-date-time-converter") String value) {
        return new WsContentResult(value);
    }
}
