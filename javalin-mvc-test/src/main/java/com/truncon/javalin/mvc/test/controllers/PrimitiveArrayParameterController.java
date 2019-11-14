package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.*;

@Controller
public class PrimitiveArrayParameterController {
    public static final String BOOLEAN_ROUTE = "/api/primitive-arrays/boolean";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@Named("value") boolean[] values) {
        return new JsonResult(values);
    }

    public static final String INTEGER_ROUTE = "/api/primitive-arrays/int";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@Named("value") int[] values) {
        return new JsonResult(values);
    }

    public static final String DOUBLE_ROUTE = "/api/primitive-arrays/double";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@Named("value") double[] values) {
        return new JsonResult(values);
    }

    public static final String STRING_ROUTE = "/api/primitive-arrays/string";
    @HttpGet(route = STRING_ROUTE)
    public ActionResult getString(@Named("value") String[] values) {
        return new JsonResult(values);
    }

    public static final String BYTE_ROUTE = "/api/primitive-arrays/byte";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getString(@Named("value") byte[] values) {
        return new JsonResult(values);
    }

    public static final String SHORT_ROUTE = "/api/primitive-arrays/short";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@Named("value") short[] values) {
        return new JsonResult(values);
    }

    public static final String FLOAT_ROUTE = "/api/primitive-arrays/float";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@Named("value") float[] values) {
        return new JsonResult(values);
    }

    public static final String CHAR_ROUTE = "/api/primitive-arrays/char";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@Named("value") char[] values) {
        return new JsonResult(values);
    }

    public static final String LONG_ROUTE = "/api/primitive-arrays/long";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getChar(@Named("value") long[] values) {
        return new JsonResult(values);
    }
}
