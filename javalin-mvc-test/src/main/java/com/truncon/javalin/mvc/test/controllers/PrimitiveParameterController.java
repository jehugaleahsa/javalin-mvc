package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;

@Controller
public class PrimitiveParameterController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/boolean/:value";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    public static final String INTEGER_ROUTE = "/api/primitives/int/:value";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(int value) {
        return new ContentResult(Integer.toString(value));
    }

    public static final String DOUBLE_ROUTE = "/api/primitives/double/:value";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(double value) {
        return new ContentResult(Double.toString(value));
    }

    public static final String BYTE_ROUTE = "/api/primitives/byte/:value";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getString(byte value) {
        return new ContentResult(Byte.toString(value));
    }

    public static final String SHORT_ROUTE = "/api/primitives/short/:value";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(short value) {
        return new ContentResult(Short.toString(value));
    }

    public static final String FLOAT_ROUTE = "/api/primitives/float/:value";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(float value) {
        return new ContentResult(Float.toString(value));
    }

    public static final String CHAR_ROUTE = "/api/primitives/char/:value";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(char value) {
        return new ContentResult(Character.toString(value));
    }

    public static final String LONG_ROUTE = "/api/primitives/long/:value";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(long value) {
        return new ContentResult(Long.toString(value));
    }
}
