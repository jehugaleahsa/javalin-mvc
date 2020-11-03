package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromHeader;
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
public class PrimitiveHeaderParamController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/header/boolean";
    public static final String INTEGER_ROUTE = "/api/primitives/header/int";
    public static final String DOUBLE_ROUTE = "/api/primitives/header/double";
    public static final String BYTE_ROUTE = "/api/primitives/header/byte";
    public static final String SHORT_ROUTE = "/api/primitives/header/short";
    public static final String FLOAT_ROUTE = "/api/primitives/header/float";
    public static final String CHAR_ROUTE = "/api/primitives/header/char";
    public static final String LONG_ROUTE = "/api/primitives/header/long";

    // region GET

    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromHeader boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromHeader int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromHeader double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getByte(@FromHeader byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromHeader short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromHeader float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromHeader char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromHeader long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region POST

    @HttpPost(route = BOOLEAN_ROUTE)
    public ActionResult postBoolean(@FromHeader boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPost(route = INTEGER_ROUTE)
    public ActionResult postInt(@FromHeader int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPost(route = DOUBLE_ROUTE)
    public ActionResult postDouble(@FromHeader double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPost(route = BYTE_ROUTE)
    public ActionResult postByte(@FromHeader byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPost(route = SHORT_ROUTE)
    public ActionResult postShort(@FromHeader short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPost(route = FLOAT_ROUTE)
    public ActionResult postFloat(@FromHeader float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPost(route = CHAR_ROUTE)
    public ActionResult postChar(@FromHeader char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPost(route = LONG_ROUTE)
    public ActionResult postLong(@FromHeader long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PUT

    @HttpPut(route = BOOLEAN_ROUTE)
    public ActionResult putBoolean(@FromHeader boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPut(route = INTEGER_ROUTE)
    public ActionResult putInt(@FromHeader int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPut(route = DOUBLE_ROUTE)
    public ActionResult putDouble(@FromHeader double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPut(route = BYTE_ROUTE)
    public ActionResult putByte(@FromHeader byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPut(route = SHORT_ROUTE)
    public ActionResult putShort(@FromHeader short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPut(route = FLOAT_ROUTE)
    public ActionResult putFloat(@FromHeader float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPut(route = CHAR_ROUTE)
    public ActionResult putChar(@FromHeader char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPut(route = LONG_ROUTE)
    public ActionResult putLong(@FromHeader long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PATCH

    @HttpPatch(route = BOOLEAN_ROUTE)
    public ActionResult patchBoolean(@FromHeader boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPatch(route = INTEGER_ROUTE)
    public ActionResult patchInt(@FromHeader int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPatch(route = DOUBLE_ROUTE)
    public ActionResult patchDouble(@FromHeader double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPatch(route = BYTE_ROUTE)
    public ActionResult patchByte(@FromHeader byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPatch(route = SHORT_ROUTE)
    public ActionResult patchShort(@FromHeader short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPatch(route = FLOAT_ROUTE)
    public ActionResult patchFloat(@FromHeader float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPatch(route = CHAR_ROUTE)
    public ActionResult patchChar(@FromHeader char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPatch(route = LONG_ROUTE)
    public ActionResult patchLong(@FromHeader long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region DELETE

    @HttpDelete(route = BOOLEAN_ROUTE)
    public ActionResult deleteBoolean(@FromHeader boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpDelete(route = INTEGER_ROUTE)
    public ActionResult deleteInt(@FromHeader int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpDelete(route = DOUBLE_ROUTE)
    public ActionResult deleteDouble(@FromHeader double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpDelete(route = BYTE_ROUTE)
    public ActionResult deleteByte(@FromHeader byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpDelete(route = SHORT_ROUTE)
    public ActionResult deleteShort(@FromHeader short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpDelete(route = FLOAT_ROUTE)
    public ActionResult deleteFloat(@FromHeader float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpDelete(route = CHAR_ROUTE)
    public ActionResult deleteChar(@FromHeader char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpDelete(route = LONG_ROUTE)
    public ActionResult deleteLong(@FromHeader long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region HEAD

    @HttpHead(route = BOOLEAN_ROUTE)
    public ActionResult headBoolean(HttpResponse response, @FromHeader boolean value) {
        response.setHeader("result", Boolean.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = INTEGER_ROUTE)
    public ActionResult headInt(HttpResponse response, @FromHeader int value) {
        response.setHeader("result", Integer.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = DOUBLE_ROUTE)
    public ActionResult headDouble(HttpResponse response, @FromHeader double value) {
        response.setHeader("result", Double.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = BYTE_ROUTE)
    public ActionResult headByte(HttpResponse response, @FromHeader byte value) {
        response.setHeader("result", Byte.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = SHORT_ROUTE)
    public ActionResult headShort(HttpResponse response, @FromHeader short value) {
        response.setHeader("result", Short.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = FLOAT_ROUTE)
    public ActionResult headFloat(HttpResponse response, @FromHeader float value) {
        response.setHeader("result", Float.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = CHAR_ROUTE)
    public ActionResult headChar(HttpResponse response, @FromHeader char value) {
        response.setHeader("result", Character.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = LONG_ROUTE)
    public ActionResult headLong(HttpResponse response, @FromHeader long value) {
        response.setHeader("result", Long.toString(value));
        return new StatusCodeResult(200);
    }

    // endregion

    // region OPTIONS

    @HttpOptions(route = BOOLEAN_ROUTE)
    public ActionResult optionsBoolean(@FromHeader boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpOptions(route = INTEGER_ROUTE)
    public ActionResult optionsInt(@FromHeader int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpOptions(route = DOUBLE_ROUTE)
    public ActionResult optionsDouble(@FromHeader double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpOptions(route = BYTE_ROUTE)
    public ActionResult optionsByte(@FromHeader byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpOptions(route = SHORT_ROUTE)
    public ActionResult optionsShort(@FromHeader short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpOptions(route = FLOAT_ROUTE)
    public ActionResult optionsFloat(@FromHeader float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpOptions(route = CHAR_ROUTE)
    public ActionResult optionsChar(@FromHeader char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpOptions(route = LONG_ROUTE)
    public ActionResult optionsLong(@FromHeader long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion
}
