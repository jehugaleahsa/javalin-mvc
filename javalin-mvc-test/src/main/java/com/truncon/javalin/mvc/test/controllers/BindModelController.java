package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.HttpPost;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.test.models.ContainerModel;
import com.truncon.javalin.mvc.test.models.DerivedModel;
import com.truncon.javalin.mvc.test.models.NestedJsonModel;
import com.truncon.javalin.mvc.test.models.PrimitiveParamFieldModel;
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
    @HttpGet(route = GET_SETTERS_WITH_SOURCE_ROUTE)
    public ActionResult getSetterModelWithSource(@FromQuery PrimitiveParamMethodModel model) {
        return new JsonResult(model);
    }

    public static final String GET_FIELDS_WITH_SOURCE_ROUTE = "/api/bind/models/fields/with-source";
    @HttpGet(route = GET_FIELDS_WITH_SOURCE_ROUTE)
    public ActionResult getFieldModelWithSource(@FromQuery PrimitiveParamFieldModel model) {
        return new JsonResult(model);
    }

    public static final String GET_SETTERS_NAMED_WITH_SOURCE_ROUTE = "/api/bind/models/setters/named/with-source";
    @HttpGet(route = GET_SETTERS_NAMED_WITH_SOURCE_ROUTE)
    public ActionResult getModelNamedWithSource(@FromQuery PrimitiveParamMethodNamedModel model) {
        return new JsonResult(model);
    }

    public static final String GET_FIELDS_NAMED_WITH_SOURCE_ROUTE = "/api/bind/models/fields/named/with-source";
    @HttpGet(route = GET_FIELDS_NAMED_WITH_SOURCE_ROUTE)
    public ActionResult getModelFieldsNamedWithSource(@FromQuery PrimitiveParamFieldNamedModel model) {
        return new JsonResult(model);
    }

    public static final String GET_SETTERS_NO_SOURCE_ROUTE = "/api/bind/models/setters/no-source";
    @HttpGet(route = GET_SETTERS_NO_SOURCE_ROUTE)
    public ActionResult getModelMethodsNoSource(PrimitiveQueryParamMethodModel model) {
        return new JsonResult(model);
    }

    public static final String GET_SETTERS_NAMED_NO_SOURCE_ROUTE = "/api/bind/models/setters/named/no-source";
    @HttpGet(route = GET_SETTERS_NAMED_NO_SOURCE_ROUTE)
    public ActionResult getModelMethodsNamedNoSource(PrimitiveQueryParamMethodNamedModel model) {
        return new JsonResult(model);
    }

    public static final String GET_FIELDS_NO_SOURCE_ROUTE = "/api/bind/models/fields/no-source";
    @HttpGet(route = GET_FIELDS_NO_SOURCE_ROUTE)
    public ActionResult getModelFieldsNoSource(PrimitiveQueryParamFieldModel model) {
        return new JsonResult(model);
    }

    public static final String GET_FIELDS_NAMED_NO_SOURCE_ROUTE = "/api/bind/models/fields/named/no-source";
    @HttpGet(route = GET_FIELDS_NAMED_NO_SOURCE_ROUTE)
    public ActionResult getModelFieldsNamedNoSource(PrimitiveQueryParamFieldNamedModel model) {
        return new JsonResult(model);
    }

    public static final String GET_NESTED_MODELS_ROUTE = "/api/bind/models/nested";
    @HttpGet(route = GET_NESTED_MODELS_ROUTE)
    public ActionResult getNestedModels(ContainerModel model) {
        return new JsonResult(model);
    }

    public static final String GET_INHERITED_MODEL_ROUTE = "/api/bind/models/inherited";
    @HttpGet(route = GET_INHERITED_MODEL_ROUTE)
    public ActionResult getInheritedModel(@FromQuery DerivedModel model) {
        return new JsonResult(model);
    }

    public static final String POST_NESTED_JSON_MODEL_ROUTE = "/api/bind/models/nested/json";
    @HttpPost(route = POST_NESTED_JSON_MODEL_ROUTE)
    public ActionResult getNestedJsonModel(NestedJsonModel model) {
        return new JsonResult(model);
    }
}
