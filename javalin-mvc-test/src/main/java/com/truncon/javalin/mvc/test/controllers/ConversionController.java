package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.test.models.ConversionModel;

@Controller
public final class ConversionController {
    public static final String GET_CONVERSION_CONTEXT1_QUERY_ROUTE = "/api/bind/models/conversion/context1/query";
    @HttpGet(route = GET_CONVERSION_CONTEXT1_QUERY_ROUTE)
    public ActionResult getConversionModelFromContext1Query(@UseConverter("static-model-converter-context") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_REQUEST1_QUERY_ROUTE = "/api/bind/models/conversion/request1/query";
    @HttpGet(route = GET_CONVERSION_REQUEST1_QUERY_ROUTE)
    public ActionResult getConversionModelFromRequest1Query(@UseConverter("static-model-converter-request") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_CONTEXT2_QUERY_ROUTE = "/api/bind/models/conversion/context2/query";
    @HttpGet(route = GET_CONVERSION_CONTEXT2_QUERY_ROUTE)
    public ActionResult getConversionModelFromContext2Query(@UseConverter("static-model-converter-context-name") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_REQUEST2_QUERY_ROUTE = "/api/bind/models/conversion/request2/query";
    @HttpGet(route = GET_CONVERSION_REQUEST2_QUERY_ROUTE)
    public ActionResult getConversionModelFromRequest2Query(@UseConverter("static-model-converter-request-name") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_CONTEXT3_QUERY_ROUTE = "/api/bind/models/conversion/context3/query";
    @HttpGet(route = GET_CONVERSION_CONTEXT3_QUERY_ROUTE)
    public ActionResult getConversionModelFromContext3Query(@UseConverter("static-model-converter-context-name-source") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_REQUEST3_QUERY_ROUTE = "/api/bind/models/conversion/request3/query";
    @HttpGet(route = GET_CONVERSION_REQUEST3_QUERY_ROUTE)
    public ActionResult getConversionModelFromRequest3Query(@UseConverter("static-model-converter-request-name-source") ConversionModel model) {
        return new JsonResult(model);
    }
}
