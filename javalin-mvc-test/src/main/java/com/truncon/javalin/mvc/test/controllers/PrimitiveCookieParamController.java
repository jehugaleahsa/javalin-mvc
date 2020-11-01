package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.HttpGet;

@Controller
public class PrimitiveCookieParamController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/cookie/boolean";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromCookie boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    public static final String INTEGER_ROUTE = "/api/primitives/cookie/int";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromCookie int value) {
        return new ContentResult(Integer.toString(value));
    }

    public static final String DOUBLE_ROUTE = "/api/primitives/cookie/double";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromCookie double value) {
        return new ContentResult(Double.toString(value));
    }

    public static final String BYTE_ROUTE = "/api/primitives/cookie/byte";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getString(@FromCookie byte value) {
        return new ContentResult(Byte.toString(value));
    }

    public static final String SHORT_ROUTE = "/api/primitives/cookie/short";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromCookie short value) {
        return new ContentResult(Short.toString(value));
    }

    public static final String FLOAT_ROUTE = "/api/primitives/cookie/float";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromCookie float value) {
        return new ContentResult(Float.toString(value));
    }

    public static final String CHAR_ROUTE = "/api/primitives/cookie/char";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromCookie char value) {
        return new ContentResult(Character.toString(value));
    }

    public static final String LONG_ROUTE = "/api/primitives/cookie/long";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromCookie long value) {
        return new ContentResult(Long.toString(value));
    }
}
