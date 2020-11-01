package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpGet;

@Controller
public class PrimitiveQueryParamController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/query/boolean";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromQuery boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    public static final String INTEGER_ROUTE = "/api/primitives/query/int";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromQuery int value) {
        return new ContentResult(Integer.toString(value));
    }

    public static final String DOUBLE_ROUTE = "/api/primitives/query/double";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromQuery double value) {
        return new ContentResult(Double.toString(value));
    }

    public static final String BYTE_ROUTE = "/api/primitives/query/byte";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getString(@FromQuery byte value) {
        return new ContentResult(Byte.toString(value));
    }

    public static final String SHORT_ROUTE = "/api/primitives/query/short";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromQuery short value) {
        return new ContentResult(Short.toString(value));
    }

    public static final String FLOAT_ROUTE = "/api/primitives/query/float";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromQuery float value) {
        return new ContentResult(Float.toString(value));
    }

    public static final String CHAR_ROUTE = "/api/primitives/query/char";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromQuery char value) {
        return new ContentResult(Character.toString(value));
    }

    public static final String LONG_ROUTE = "/api/primitives/query/long";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromQuery long value) {
        return new ContentResult(Long.toString(value));
    }
}
