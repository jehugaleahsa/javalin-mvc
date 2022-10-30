package com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue;

import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;

@WsController(route = DefaultValueStandardScalarController.ROUTE)
public final class DefaultValueStandardScalarController {
    public static final String ROUTE = "/ws/parameters/default_values/standard/scalar";

    @WsMessage
    public WsActionResult onMessage(
            @DefaultValue("path") @PathParam("value") String path,
            @DefaultValue("query") @QueryParam("value") String query,
            @DefaultValue("header") @HeaderParam("value") String header,
            @DefaultValue("cookie") @CookieParam("value") String cookie,
            @DefaultValue("any") @Named("value") String any) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(path);
        model.setQuery(query);
        model.setHeader(header);
        model.setCookie(cookie);
        model.setAny(any);
        return new WsJsonResult(model);
    }
}
