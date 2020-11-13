package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.test.models.ConversionModel;

@Controller
public final class ConversionController {
    public static final String GET_CONVERSION_QUERY_ROUTE = "/api/bind/models/conversion/query";
    @HttpGet(route = GET_CONVERSION_QUERY_ROUTE)
    public ActionResult getConversionModelFromQuery(@UseConverter("static-model-converter") ConversionModel model) {
        return new JsonResult(model);
    }
}
