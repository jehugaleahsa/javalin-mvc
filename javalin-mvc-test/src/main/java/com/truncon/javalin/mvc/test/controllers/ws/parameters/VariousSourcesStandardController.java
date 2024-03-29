package com.truncon.javalin.mvc.test.controllers.ws.parameters;

import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@WsController(route = VariousSourcesStandardController.ROUTE)
public final class VariousSourcesStandardController {
    public static final String ROUTE = "/ws/parameters/sources/standard/{value}";

    @WsMessage
    public WsActionResult onMessage(
            @PathParam("value") String path,
            @QueryParam("value") String query,
            @HeaderParam("value") String header,
            @CookieParam("value") String cookie) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(path);
        model.setQuery(query);
        model.setHeader(header);
        model.setCookie(cookie);
        return new WsJsonResult(model);
    }
}
