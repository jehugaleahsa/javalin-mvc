package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpGet;

@Controller
public class PrimitiveHeaderParamController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/header/boolean";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromHeader boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    public static final String INTEGER_ROUTE = "/api/primitives/header/int";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromHeader int value) {
        return new ContentResult(Integer.toString(value));
    }

    public static final String DOUBLE_ROUTE = "/api/primitives/header/double";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromHeader double value) {
        return new ContentResult(Double.toString(value));
    }

    public static final String BYTE_ROUTE = "/api/primitives/header/byte";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getString(@FromHeader byte value) {
        return new ContentResult(Byte.toString(value));
    }

    public static final String SHORT_ROUTE = "/api/primitives/header/short";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromHeader short value) {
        return new ContentResult(Short.toString(value));
    }

    public static final String FLOAT_ROUTE = "/api/primitives/header/float";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromHeader float value) {
        return new ContentResult(Float.toString(value));
    }

    public static final String CHAR_ROUTE = "/api/primitives/header/char";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromHeader char value) {
        return new ContentResult(Character.toString(value));
    }

    public static final String LONG_ROUTE = "/api/primitives/header/long";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromHeader long value) {
        return new ContentResult(Long.toString(value));
    }
}
