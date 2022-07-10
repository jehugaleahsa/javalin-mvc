package com.truncon.javalin.mvc.test.controllers.ws.parameters.defaultValue;

import com.truncon.javalin.mvc.api.DefaultValue;
import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ws.WsActionResult;
import com.truncon.javalin.mvc.api.ws.WsController;
import com.truncon.javalin.mvc.api.ws.WsJsonResult;
import com.truncon.javalin.mvc.api.ws.WsMessage;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;

import java.util.List;

@WsController(route = DefaultValueBuiltinCollectionController.ROUTE)
public final class DefaultValueBuiltinCollectionController {
    public static final String ROUTE = "/ws/parameters/default_values/builtin/collection";

    @WsMessage
    public WsActionResult onMessage(
            @DefaultValue("path") @FromPath("value") List<String> paths,
            @DefaultValue("query") @FromQuery("value") List<String> queries,
            @DefaultValue("header") @FromHeader("value") List<String> headers,
            @DefaultValue("cookie") @FromCookie("value") List<String> cookies,
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
