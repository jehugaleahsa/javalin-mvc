package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromForm;
import com.truncon.javalin.mvc.api.HttpGet;

@Controller
public class PrimitiveFormDataController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/form-data/boolean";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromForm boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    public static final String INTEGER_ROUTE = "/api/primitives/form-data/int";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromForm int value) {
        return new ContentResult(Integer.toString(value));
    }

    public static final String DOUBLE_ROUTE = "/api/primitives/form-data/double";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromForm double value) {
        return new ContentResult(Double.toString(value));
    }

    public static final String BYTE_ROUTE = "/api/primitives/form-data/byte";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getString(@FromForm byte value) {
        return new ContentResult(Byte.toString(value));
    }

    public static final String SHORT_ROUTE = "/api/primitives/form-data/short";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromForm short value) {
        return new ContentResult(Short.toString(value));
    }

    public static final String FLOAT_ROUTE = "/api/primitives/form-data/float";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromForm float value) {
        return new ContentResult(Float.toString(value));
    }

    public static final String CHAR_ROUTE = "/api/primitives/form-data/char";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromForm char value) {
        return new ContentResult(Character.toString(value));
    }

    public static final String LONG_ROUTE = "/api/primitives/form-data/long";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromForm long value) {
        return new ContentResult(Long.toString(value));
    }
}
