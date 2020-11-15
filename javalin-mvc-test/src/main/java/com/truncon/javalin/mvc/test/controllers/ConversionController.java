package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.test.models.ConversionModel;
import com.truncon.javalin.mvc.test.models.HttpModelWithConversionModel;

@Controller
public final class ConversionController {
    public static final String GET_CONVERSION_CONTEXT_ROUTE = "/api/bind/models/conversion/context";
    @HttpGet(route = GET_CONVERSION_CONTEXT_ROUTE)
    public ActionResult getConversionModelFromContext(@UseConverter("static-model-converter-context") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_REQUEST_ROUTE = "/api/bind/models/conversion/request";
    @HttpGet(route = GET_CONVERSION_REQUEST_ROUTE)
    public ActionResult getConversionModelFromRequest(@UseConverter("static-model-converter-request") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_CONTEXT_NAME_ROUTE = "/api/bind/models/conversion/context-name";
    @HttpGet(route = GET_CONVERSION_CONTEXT_NAME_ROUTE)
    public ActionResult getConversionModelFromContextName(@UseConverter("static-model-converter-context-name") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_REQUEST_NAME_ROUTE = "/api/bind/models/conversion/request-name";
    @HttpGet(route = GET_CONVERSION_REQUEST_NAME_ROUTE)
    public ActionResult getConversionModelFromRequestName(@UseConverter("static-model-converter-request-name") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_CONTEXT_SOURCE_ROUTE = "/api/bind/models/conversion/context-source";
    @HttpGet(route = GET_CONVERSION_CONTEXT_SOURCE_ROUTE)
    public ActionResult getConversionModelFromContextSource(@UseConverter("static-model-converter-context-source") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_REQUEST_SOURCE_ROUTE = "/api/bind/models/conversion/request-source";
    @HttpGet(route = GET_CONVERSION_REQUEST_SOURCE_ROUTE)
    public ActionResult getConversionModelFromRequestSource(@UseConverter("static-model-converter-request-source") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_CONTEXT_NAME_SOURCE_ROUTE = "/api/bind/models/conversion/context-name-source";
    @HttpGet(route = GET_CONVERSION_CONTEXT_NAME_SOURCE_ROUTE)
    public ActionResult getConversionModelFromContextNameSource(@UseConverter("static-model-converter-context-name-source") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_REQUEST_NAME_SOURCE_ROUTE = "/api/bind/models/conversion/request-name-source";
    @HttpGet(route = GET_CONVERSION_REQUEST_NAME_SOURCE_ROUTE)
    public ActionResult getConversionModelFromRequestNameSource(@UseConverter("static-model-converter-request-name-source") ConversionModel model) {
        return new JsonResult(model);
    }

    public static final String GET_CONVERSION_NESTED_ROUTE = "/api/bind/models/conversion/nested";
    @HttpGet(route = GET_CONVERSION_NESTED_ROUTE)
    public ActionResult getNestedConversionModel(@FromQuery HttpModelWithConversionModel model) {
        return new JsonResult(model);
    }
}
