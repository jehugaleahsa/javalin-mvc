package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromPath;
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
public class PrimitivePathParamController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/path/boolean/:value";
    public static final String INTEGER_ROUTE = "/api/primitives/path/int/:value";
    public static final String DOUBLE_ROUTE = "/api/primitives/path/double/:value";
    public static final String BYTE_ROUTE = "/api/primitives/path/byte/:value";
    public static final String SHORT_ROUTE = "/api/primitives/path/short/:value";
    public static final String FLOAT_ROUTE = "/api/primitives/path/float/:value";
    public static final String CHAR_ROUTE = "/api/primitives/path/char/:value";
    public static final String LONG_ROUTE = "/api/primitives/path/long/:value";

    // region GET

    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromPath boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromPath int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromPath double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getByte(@FromPath byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromPath short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromPath float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromPath char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromPath long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region POST

    @HttpPost(route = BOOLEAN_ROUTE)
    public ActionResult postBoolean(@FromPath boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPost(route = INTEGER_ROUTE)
    public ActionResult postInt(@FromPath int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPost(route = DOUBLE_ROUTE)
    public ActionResult postDouble(@FromPath double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPost(route = BYTE_ROUTE)
    public ActionResult postByte(@FromPath byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPost(route = SHORT_ROUTE)
    public ActionResult postShort(@FromPath short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPost(route = FLOAT_ROUTE)
    public ActionResult postFloat(@FromPath float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPost(route = CHAR_ROUTE)
    public ActionResult postChar(@FromPath char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPost(route = LONG_ROUTE)
    public ActionResult postLong(@FromPath long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PUT

    @HttpPut(route = BOOLEAN_ROUTE)
    public ActionResult putBoolean(@FromPath boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPut(route = INTEGER_ROUTE)
    public ActionResult putInt(@FromPath int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPut(route = DOUBLE_ROUTE)
    public ActionResult putDouble(@FromPath double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPut(route = BYTE_ROUTE)
    public ActionResult putByte(@FromPath byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPut(route = SHORT_ROUTE)
    public ActionResult putShort(@FromPath short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPut(route = FLOAT_ROUTE)
    public ActionResult putFloat(@FromPath float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPut(route = CHAR_ROUTE)
    public ActionResult putChar(@FromPath char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPut(route = LONG_ROUTE)
    public ActionResult putLong(@FromPath long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PATCH

    @HttpPatch(route = BOOLEAN_ROUTE)
    public ActionResult patchBoolean(@FromPath boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPatch(route = INTEGER_ROUTE)
    public ActionResult patchInt(@FromPath int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPatch(route = DOUBLE_ROUTE)
    public ActionResult patchDouble(@FromPath double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPatch(route = BYTE_ROUTE)
    public ActionResult patchByte(@FromPath byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPatch(route = SHORT_ROUTE)
    public ActionResult patchShort(@FromPath short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPatch(route = FLOAT_ROUTE)
    public ActionResult patchFloat(@FromPath float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPatch(route = CHAR_ROUTE)
    public ActionResult patchChar(@FromPath char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPatch(route = LONG_ROUTE)
    public ActionResult patchLong(@FromPath long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region DELETE

    @HttpDelete(route = BOOLEAN_ROUTE)
    public ActionResult deleteBoolean(@FromPath boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpDelete(route = INTEGER_ROUTE)
    public ActionResult deleteInt(@FromPath int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpDelete(route = DOUBLE_ROUTE)
    public ActionResult deleteDouble(@FromPath double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpDelete(route = BYTE_ROUTE)
    public ActionResult deleteByte(@FromPath byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpDelete(route = SHORT_ROUTE)
    public ActionResult deleteShort(@FromPath short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpDelete(route = FLOAT_ROUTE)
    public ActionResult deleteFloat(@FromPath float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpDelete(route = CHAR_ROUTE)
    public ActionResult deleteChar(@FromPath char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpDelete(route = LONG_ROUTE)
    public ActionResult deleteLong(@FromPath long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region HEAD

    @HttpHead(route = BOOLEAN_ROUTE)
    public ActionResult headBoolean(HttpResponse response, @FromPath boolean value) {
        response.setHeader("result", Boolean.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = INTEGER_ROUTE)
    public ActionResult headInt(HttpResponse response, @FromPath int value) {
        response.setHeader("result", Integer.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = DOUBLE_ROUTE)
    public ActionResult headDouble(HttpResponse response, @FromPath double value) {
        response.setHeader("result", Double.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = BYTE_ROUTE)
    public ActionResult headByte(HttpResponse response, @FromPath byte value) {
        response.setHeader("result", Byte.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = SHORT_ROUTE)
    public ActionResult headShort(HttpResponse response, @FromPath short value) {
        response.setHeader("result", Short.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = FLOAT_ROUTE)
    public ActionResult headFloat(HttpResponse response, @FromPath float value) {
        response.setHeader("result", Float.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = CHAR_ROUTE)
    public ActionResult headChar(HttpResponse response, @FromPath char value) {
        response.setHeader("result", Character.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = LONG_ROUTE)
    public ActionResult headLong(HttpResponse response, @FromPath long value) {
        response.setHeader("result", Long.toString(value));
        return new StatusCodeResult(200);
    }

    // endregion

    // region OPTIONS

    @HttpOptions(route = BOOLEAN_ROUTE)
    public ActionResult optionsBoolean(@FromPath boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpOptions(route = INTEGER_ROUTE)
    public ActionResult optionsInt(@FromPath int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpOptions(route = DOUBLE_ROUTE)
    public ActionResult optionsDouble(@FromPath double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpOptions(route = BYTE_ROUTE)
    public ActionResult optionsByte(@FromPath byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpOptions(route = SHORT_ROUTE)
    public ActionResult optionsShort(@FromPath short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpOptions(route = FLOAT_ROUTE)
    public ActionResult optionsFloat(@FromPath float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpOptions(route = CHAR_ROUTE)
    public ActionResult optionsChar(@FromPath char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpOptions(route = LONG_ROUTE)
    public ActionResult optionsLong(@FromPath long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion
}
