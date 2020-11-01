package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.HttpGet;

@Controller
public class PrimitivePathParamController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/path/boolean/:value";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromPath boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    public static final String INTEGER_ROUTE = "/api/primitives/path/int/:value";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromPath int value) {
        return new ContentResult(Integer.toString(value));
    }

    public static final String DOUBLE_ROUTE = "/api/primitives/path/double/:value";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromPath double value) {
        return new ContentResult(Double.toString(value));
    }

    public static final String BYTE_ROUTE = "/api/primitives/path/byte/:value";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getString(@FromPath byte value) {
        return new ContentResult(Byte.toString(value));
    }

    public static final String SHORT_ROUTE = "/api/primitives/path/short/:value";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromPath short value) {
        return new ContentResult(Short.toString(value));
    }

    public static final String FLOAT_ROUTE = "/api/primitives/path/float/:value";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromPath float value) {
        return new ContentResult(Float.toString(value));
    }

    public static final String CHAR_ROUTE = "/api/primitives/path/char/:value";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromPath char value) {
        return new ContentResult(Character.toString(value));
    }

    public static final String LONG_ROUTE = "/api/primitives/path/long/:value";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromPath long value) {
        return new ContentResult(Long.toString(value));
    }
}
