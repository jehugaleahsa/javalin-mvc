package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpDelete;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.HttpHead;
import com.truncon.javalin.mvc.api.HttpOptions;
import com.truncon.javalin.mvc.api.HttpPatch;
import com.truncon.javalin.mvc.api.HttpPost;
import com.truncon.javalin.mvc.api.HttpPut;
import com.truncon.javalin.mvc.api.HttpResponse;
import com.truncon.javalin.mvc.api.StatusCodeResult;

@Controller
public class PrimitiveQueryParamController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/query/boolean";
    public static final String INTEGER_ROUTE = "/api/primitives/query/int";
    public static final String DOUBLE_ROUTE = "/api/primitives/query/double";
    public static final String BYTE_ROUTE = "/api/primitives/query/byte";
    public static final String SHORT_ROUTE = "/api/primitives/query/short";
    public static final String FLOAT_ROUTE = "/api/primitives/query/float";
    public static final String CHAR_ROUTE = "/api/primitives/query/char";
    public static final String LONG_ROUTE = "/api/primitives/query/long";

    // region GET

    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromQuery boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromQuery int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromQuery double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getByte(@FromQuery byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromQuery short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromQuery float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromQuery char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromQuery long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region POST

    @HttpPost(route = BOOLEAN_ROUTE)
    public ActionResult postBoolean(@FromQuery boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPost(route = INTEGER_ROUTE)
    public ActionResult postInt(@FromQuery int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPost(route = DOUBLE_ROUTE)
    public ActionResult postDouble(@FromQuery double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPost(route = BYTE_ROUTE)
    public ActionResult postByte(@FromQuery byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPost(route = SHORT_ROUTE)
    public ActionResult postShort(@FromQuery short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPost(route = FLOAT_ROUTE)
    public ActionResult postFloat(@FromQuery float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPost(route = CHAR_ROUTE)
    public ActionResult postChar(@FromQuery char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPost(route = LONG_ROUTE)
    public ActionResult postLong(@FromQuery long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PUT

    @HttpPut(route = BOOLEAN_ROUTE)
    public ActionResult putBoolean(@FromQuery boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPut(route = INTEGER_ROUTE)
    public ActionResult putInt(@FromQuery int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPut(route = DOUBLE_ROUTE)
    public ActionResult putDouble(@FromQuery double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPut(route = BYTE_ROUTE)
    public ActionResult putByte(@FromQuery byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPut(route = SHORT_ROUTE)
    public ActionResult putShort(@FromQuery short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPut(route = FLOAT_ROUTE)
    public ActionResult putFloat(@FromQuery float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPut(route = CHAR_ROUTE)
    public ActionResult putChar(@FromQuery char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPut(route = LONG_ROUTE)
    public ActionResult putLong(@FromQuery long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PATCH

    @HttpPatch(route = BOOLEAN_ROUTE)
    public ActionResult patchBoolean(@FromQuery boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPatch(route = INTEGER_ROUTE)
    public ActionResult patchInt(@FromQuery int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPatch(route = DOUBLE_ROUTE)
    public ActionResult patchDouble(@FromQuery double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPatch(route = BYTE_ROUTE)
    public ActionResult patchByte(@FromQuery byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPatch(route = SHORT_ROUTE)
    public ActionResult patchShort(@FromQuery short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPatch(route = FLOAT_ROUTE)
    public ActionResult patchFloat(@FromQuery float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPatch(route = CHAR_ROUTE)
    public ActionResult patchChar(@FromQuery char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPatch(route = LONG_ROUTE)
    public ActionResult patchLong(@FromQuery long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region DELETE

    @HttpDelete(route = BOOLEAN_ROUTE)
    public ActionResult deleteBoolean(@FromQuery boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpDelete(route = INTEGER_ROUTE)
    public ActionResult deleteInt(@FromQuery int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpDelete(route = DOUBLE_ROUTE)
    public ActionResult deleteDouble(@FromQuery double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpDelete(route = BYTE_ROUTE)
    public ActionResult deleteByte(@FromQuery byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpDelete(route = SHORT_ROUTE)
    public ActionResult deleteShort(@FromQuery short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpDelete(route = FLOAT_ROUTE)
    public ActionResult deleteFloat(@FromQuery float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpDelete(route = CHAR_ROUTE)
    public ActionResult deleteChar(@FromQuery char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpDelete(route = LONG_ROUTE)
    public ActionResult deleteLong(@FromQuery long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region HEAD

    @HttpHead(route = BOOLEAN_ROUTE)
    public ActionResult headBoolean(HttpResponse response, @FromQuery boolean value) {
        response.setHeader("result", Boolean.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = INTEGER_ROUTE)
    public ActionResult headInt(HttpResponse response, @FromQuery int value) {
        response.setHeader("result", Integer.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = DOUBLE_ROUTE)
    public ActionResult headDouble(HttpResponse response, @FromQuery double value) {
        response.setHeader("result", Double.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = BYTE_ROUTE)
    public ActionResult headByte(HttpResponse response, @FromQuery byte value) {
        response.setHeader("result", Byte.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = SHORT_ROUTE)
    public ActionResult headShort(HttpResponse response, @FromQuery short value) {
        response.setHeader("result", Short.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = FLOAT_ROUTE)
    public ActionResult headFloat(HttpResponse response, @FromQuery float value) {
        response.setHeader("result", Float.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = CHAR_ROUTE)
    public ActionResult headChar(HttpResponse response, @FromQuery char value) {
        response.setHeader("result", Character.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = LONG_ROUTE)
    public ActionResult headLong(HttpResponse response, @FromQuery long value) {
        response.setHeader("result", Long.toString(value));
        return new StatusCodeResult(200);
    }

    // endregion

    // region OPTIONS

    @HttpOptions(route = BOOLEAN_ROUTE)
    public ActionResult optionsBoolean(@FromQuery boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpOptions(route = INTEGER_ROUTE)
    public ActionResult optionsInt(@FromQuery int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpOptions(route = DOUBLE_ROUTE)
    public ActionResult optionsDouble(@FromQuery double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpOptions(route = BYTE_ROUTE)
    public ActionResult optionsByte(@FromQuery byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpOptions(route = SHORT_ROUTE)
    public ActionResult optionsShort(@FromQuery short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpOptions(route = FLOAT_ROUTE)
    public ActionResult optionsFloat(@FromQuery float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpOptions(route = CHAR_ROUTE)
    public ActionResult optionsChar(@FromQuery char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpOptions(route = LONG_ROUTE)
    public ActionResult optionsLong(@FromQuery long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion
}
