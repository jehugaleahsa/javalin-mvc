package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.TimeZone;

@Controller
public class BoxedParameterController {
    public static final String BOOLEAN_ROUTE = "/api/boxed/boolean/:value";
    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(Boolean value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String INTEGER_ROUTE = "/api/boxed/int/:value";
    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(Integer value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String DOUBLE_ROUTE = "/api/boxed/double/:value";
    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(Double value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String BYTE_ROUTE = "/api/boxed/byte/:value";
    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getString(Byte value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String SHORT_ROUTE = "/api/boxed/short/:value";
    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(Short value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String FLOAT_ROUTE = "/api/boxed/float/:value";
    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(Float value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String CHAR_ROUTE = "/api/boxed/char/:value";
    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(Character value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String LONG_ROUTE = "/api/boxed/long/:value";
    @HttpGet(route = LONG_ROUTE)
    public ActionResult getChar(Long value) {
        return new ContentResult(Objects.toString(value));
    }

    public static final String DATE_ROUTE = "/api/boxed/date/:value";
    public static final DateFormat DATE_FORMAT = getDateFormatter();
    @HttpGet(route = DATE_ROUTE)
    public ActionResult getChar(Date value) {
        return new ContentResult(DATE_FORMAT.format(value));
    }

    private static DateFormat getDateFormatter() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        formatter.setTimeZone(timeZone);
        return formatter;
    }
}
