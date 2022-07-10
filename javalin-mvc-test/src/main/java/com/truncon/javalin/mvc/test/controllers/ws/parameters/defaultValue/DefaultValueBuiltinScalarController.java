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

@WsController(route = DefaultValueBuiltinScalarController.ROUTE)
public final class DefaultValueBuiltinScalarController {
    public static final String ROUTE = "/ws/parameters/default_values/builtin/scalar";

    @WsMessage
    public WsActionResult onMessage(
            @DefaultValue("path") @FromPath("value") String path,
            @DefaultValue("query") @FromQuery("value") String query,
            @DefaultValue("header") @FromHeader("value") String header,
            @DefaultValue("cookie") @FromCookie("value") String cookie,
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
