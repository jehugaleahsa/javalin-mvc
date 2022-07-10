package com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue;

import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;

import javax.ws.rs.CookieParam;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.util.List;

@WsController(route = DefaultValueStandardCollectionController.ROUTE)
public final class DefaultValueStandardCollectionController {
    public static final String ROUTE = "/ws/parameters/default_values/standard/collection";

    @WsMessage
    public WsActionResult onMessage(
            @DefaultValue("path") @PathParam("value") List<String> paths,
            @DefaultValue("query") @QueryParam("value") List<String> queries,
            @DefaultValue("header") @HeaderParam("value") List<String> headers,
            @DefaultValue("cookie") @CookieParam("value") List<String> cookies,
            @DefaultValue("any") @Named("value") List<String> anyValues) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(paths.size() == 1 ? paths.get(0) : null);
        model.setQuery(queries.size() == 1 ? queries.get(0) : null);
        model.setHeader(headers.size() == 1 ? headers.get(0) : null);
        model.setCookie(cookies.size() == 1 ? cookies.get(0) : null);
        model.setAny(anyValues.size() == 1 ? anyValues.get(0) : null);
        return new WsJsonResult(model);
    }
}
