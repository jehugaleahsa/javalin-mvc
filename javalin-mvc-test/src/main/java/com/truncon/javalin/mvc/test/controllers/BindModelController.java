package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.test.models.PrimitiveParamFieldNamedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveParamMethodModel;
import com.truncon.javalin.mvc.test.models.PrimitiveParamMethodNamedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamFieldModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamFieldNamedModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamMethodModel;
import com.truncon.javalin.mvc.test.models.PrimitiveQueryParamMethodNamedModel;

@Controller
public final class BindModelController {
    public static final String GET_SETTERS_WITH_SOURCE_ROUTE = "/api/bind/models/setters/with-source";
    public static final String GET_SETTERS_NAMED_WITH_SOURCE_ROUTE = "/api/bind/models/setters/named/with-source";
    public static final String GET_FIELDS_NAMED_WITH_SOURCE_ROUTE = "/api/bind/models/fields/named/with-source";
    public static final String GET_SETTERS_NO_SOURCE_ROUTE = "/api/bind/models/setters/no-source";
    public static final String GET_SETTERS_NAMED_NO_SOURCE_ROUTE = "/api/bind/models/setters/named/no-source";
    public static final String GET_FIELDS_NO_SOURCE_ROUTE = "/api/bind/models/fields/no-source";
    public static final String GET_FIELDS_NAMED_NO_SOURCE_ROUTE = "/api/bind/models/fields/named/no-source";

    @HttpGet(route = GET_SETTERS_WITH_SOURCE_ROUTE)
    public ActionResult getModelWithSource(@FromQuery PrimitiveParamMethodModel model) {
        return new JsonResult(model);
    }

    @HttpGet(route = GET_SETTERS_NAMED_WITH_SOURCE_ROUTE)
    public ActionResult getModelNamedWithSource(@FromQuery PrimitiveParamMethodNamedModel model) {
        return new JsonResult(model);
    }

    @HttpGet(route = GET_FIELDS_NAMED_WITH_SOURCE_ROUTE)
    public ActionResult getModelFieldsNamedWithSource(@FromQuery PrimitiveParamFieldNamedModel model) {
        return new JsonResult(model);
    }

    @HttpGet(route = GET_SETTERS_NO_SOURCE_ROUTE)
    public ActionResult getModelMethodsNoSource(PrimitiveQueryParamMethodModel model) {
        return new JsonResult(model);
    }

    @HttpGet(route = GET_SETTERS_NAMED_NO_SOURCE_ROUTE)
    public ActionResult getModelMethodsNamedNoSource(PrimitiveQueryParamMethodNamedModel model) {
        return new JsonResult(model);
    }

    @HttpGet(route = GET_FIELDS_NO_SOURCE_ROUTE)
    public ActionResult getModelFieldsNoSource(PrimitiveQueryParamFieldModel model) {
        return new JsonResult(model);
    }

    @HttpGet(route = GET_FIELDS_NAMED_NO_SOURCE_ROUTE)
    public ActionResult getModelFieldsNamedNoSource(PrimitiveQueryParamFieldNamedModel model) {
        return new JsonResult(model);
    }
}
