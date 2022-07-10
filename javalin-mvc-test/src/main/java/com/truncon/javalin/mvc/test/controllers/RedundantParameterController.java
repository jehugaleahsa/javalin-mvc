package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.test.models.RedundantParameterModel;

@Controller
public final class RedundantParameterController {
    public static final String DUPLICATE_PARAMETER_ROUTE = "/api/redundant/parameters";
    @HttpGet(route = DUPLICATE_PARAMETER_ROUTE)
    public ActionResult getSameThingMultipleTimes(
            @Named("value") String asString,
            @Named("value") Integer asInteger,
            @Named("value") Double asDouble) {
        RedundantParameterModel model = new RedundantParameterModel();
        model.setAsString(asString);
        model.setAsInteger(asInteger);
        model.setAsDouble(asDouble);
        return new JsonResult(model);
    }
}
