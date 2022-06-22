package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.HttpResponse;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

@Controller
public final class ReturnValueController {
    public static final String GET_OBJECT_ROUTE = "/api/returns/object";
    @HttpGet(route = GET_OBJECT_ROUTE)
    public PrimitiveModel getPrimitiveModel() {
        return new PrimitiveModel();
    }

    public static final String GET_PRIMITIVE_INTEGER_ROUTE = "/api/returns/int";
    @HttpGet(route = GET_PRIMITIVE_INTEGER_ROUTE)
    public int getPrimitiveInteger() {
        return 123;
    }

    public static final String GET_BOXED_INTEGER_ROUTE = "/api/returns/integer";
    @HttpGet(route = GET_BOXED_INTEGER_ROUTE)
    public Integer getBoxedInteger() {
        return 123;
    }

    public static final String GET_NULL_OBJECT_ROUTE = "/api/returns/null";
    @HttpGet(route = GET_NULL_OBJECT_ROUTE)
    public Object getNull() {
        return null;
    }

    public static final String GET_ACTION_RESULT_ROUTE = "/api/returns/action_result";
    @HttpGet(route = GET_ACTION_RESULT_ROUTE)
    public Object getActionResult() {
        return new JsonResult(123);
    }

    public static final String GET_VOID_ROUTE = "/api/returns/void";
    @HttpGet(route = GET_VOID_ROUTE)
    public void getVoid(HttpResponse response) {
        response.setStatusCode(200);
        response.setTextBody("A-okay!");
    }
}
