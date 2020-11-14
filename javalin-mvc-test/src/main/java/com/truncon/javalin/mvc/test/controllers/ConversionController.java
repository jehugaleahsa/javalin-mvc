package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.test.models.ConversionModel;

@Controller
public final class ConversionController {
    public static final String GET_CONVERSION_CONTEXT_QUERY_ROUTE = "/api/bind/models/conversion/context/query";
    @HttpGet(route = GET_CONVERSION_CONTEXT_QUERY_ROUTE)
    public ActionResult getConversionModelFromContextQuery(@UseConverter("static-model-converter-context") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_REQUEST_QUERY_ROUTE = "/api/bind/models/conversion/request/query";
    @HttpGet(route = GET_CONVERSION_REQUEST_QUERY_ROUTE)
    public ActionResult getConversionModelFromRequestQuery(@UseConverter("static-model-converter-request") ConversionModel model) {
        return new JsonResult(model);
    }
}
