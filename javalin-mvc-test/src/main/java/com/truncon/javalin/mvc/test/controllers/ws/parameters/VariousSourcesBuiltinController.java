package com.truncon.javalin.mvc.test.controllers.ws.parameters;

import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;

@WsController(route = VariousSourcesBuiltinController.ROUTE)
public final class VariousSourcesBuiltinController {
    public static final String ROUTE = "/ws/parameters/sources/builtin/{value}";

    @WsMessage
    public WsActionResult onMessage(
            @FromPath("value") String path,
            @FromQuery("value") String query,
            @FromHeader("value") String header,
            @FromCookie("value") String cookie) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(path);
        model.setQuery(query);
        model.setHeader(header);
        model.setCookie(cookie);
        return new WsJsonResult(model);
    }
}
