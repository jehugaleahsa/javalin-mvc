package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromCookie;
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
public class PrimitiveCookieParamController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/cookie/boolean";
    public static final String INTEGER_ROUTE = "/api/primitives/cookie/int";
    public static final String DOUBLE_ROUTE = "/api/primitives/cookie/double";
    public static final String BYTE_ROUTE = "/api/primitives/cookie/byte";
    public static final String SHORT_ROUTE = "/api/primitives/cookie/short";
    public static final String FLOAT_ROUTE = "/api/primitives/cookie/float";
    public static final String CHAR_ROUTE = "/api/primitives/cookie/char";
    public static final String LONG_ROUTE = "/api/primitives/cookie/long";

    // region GET

    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromCookie boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromCookie int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromCookie double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getByte(@FromCookie byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromCookie short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromCookie float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromCookie char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromCookie long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region POST

    @HttpPost(route = BOOLEAN_ROUTE)
    public ActionResult postBoolean(@FromCookie boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPost(route = INTEGER_ROUTE)
    public ActionResult postInt(@FromCookie int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPost(route = DOUBLE_ROUTE)
    public ActionResult postDouble(@FromCookie double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPost(route = BYTE_ROUTE)
    public ActionResult postByte(@FromCookie byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPost(route = SHORT_ROUTE)
    public ActionResult postShort(@FromCookie short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPost(route = FLOAT_ROUTE)
    public ActionResult postFloat(@FromCookie float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPost(route = CHAR_ROUTE)
    public ActionResult postChar(@FromCookie char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPost(route = LONG_ROUTE)
    public ActionResult postLong(@FromCookie long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PUT

    @HttpPut(route = BOOLEAN_ROUTE)
    public ActionResult putBoolean(@FromCookie boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPut(route = INTEGER_ROUTE)
    public ActionResult putInt(@FromCookie int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPut(route = DOUBLE_ROUTE)
    public ActionResult putDouble(@FromCookie double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPut(route = BYTE_ROUTE)
    public ActionResult putByte(@FromCookie byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPut(route = SHORT_ROUTE)
    public ActionResult putShort(@FromCookie short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPut(route = FLOAT_ROUTE)
    public ActionResult putFloat(@FromCookie float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPut(route = CHAR_ROUTE)
    public ActionResult putChar(@FromCookie char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPut(route = LONG_ROUTE)
    public ActionResult putLong(@FromCookie long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PATCH

    @HttpPatch(route = BOOLEAN_ROUTE)
    public ActionResult patchBoolean(@FromCookie boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPatch(route = INTEGER_ROUTE)
    public ActionResult patchInt(@FromCookie int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPatch(route = DOUBLE_ROUTE)
    public ActionResult patchDouble(@FromCookie double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPatch(route = BYTE_ROUTE)
    public ActionResult patchByte(@FromCookie byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPatch(route = SHORT_ROUTE)
    public ActionResult patchShort(@FromCookie short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPatch(route = FLOAT_ROUTE)
    public ActionResult patchFloat(@FromCookie float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPatch(route = CHAR_ROUTE)
    public ActionResult patchChar(@FromCookie char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPatch(route = LONG_ROUTE)
    public ActionResult patchLong(@FromCookie long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region DELETE

    @HttpDelete(route = BOOLEAN_ROUTE)
    public ActionResult deleteBoolean(@FromCookie boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpDelete(route = INTEGER_ROUTE)
    public ActionResult deleteInt(@FromCookie int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpDelete(route = DOUBLE_ROUTE)
    public ActionResult deleteDouble(@FromCookie double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpDelete(route = BYTE_ROUTE)
    public ActionResult deleteByte(@FromCookie byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpDelete(route = SHORT_ROUTE)
    public ActionResult deleteShort(@FromCookie short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpDelete(route = FLOAT_ROUTE)
    public ActionResult deleteFloat(@FromCookie float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpDelete(route = CHAR_ROUTE)
    public ActionResult deleteChar(@FromCookie char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpDelete(route = LONG_ROUTE)
    public ActionResult deleteLong(@FromCookie long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region HEAD

    @HttpHead(route = BOOLEAN_ROUTE)
    public ActionResult headBoolean(HttpResponse response, @FromCookie boolean value) {
        response.setHeader("result", Boolean.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = INTEGER_ROUTE)
    public ActionResult headInt(HttpResponse response, @FromCookie int value) {
        response.setHeader("result", Integer.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = DOUBLE_ROUTE)
    public ActionResult headDouble(HttpResponse response, @FromCookie double value) {
        response.setHeader("result", Double.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = BYTE_ROUTE)
    public ActionResult headByte(HttpResponse response, @FromCookie byte value) {
        response.setHeader("result", Byte.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = SHORT_ROUTE)
    public ActionResult headShort(HttpResponse response, @FromCookie short value) {
        response.setHeader("result", Short.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = FLOAT_ROUTE)
    public ActionResult headFloat(HttpResponse response, @FromCookie float value) {
        response.setHeader("result", Float.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = CHAR_ROUTE)
    public ActionResult headChar(HttpResponse response, @FromCookie char value) {
        response.setHeader("result", Character.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = LONG_ROUTE)
    public ActionResult headLong(HttpResponse response, @FromCookie long value) {
        response.setHeader("result", Long.toString(value));
        return new StatusCodeResult(200);
    }

    // endregion

    // region OPTIONS

    @HttpOptions(route = BOOLEAN_ROUTE)
    public ActionResult optionsBoolean(@FromCookie boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpOptions(route = INTEGER_ROUTE)
    public ActionResult optionsInt(@FromCookie int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpOptions(route = DOUBLE_ROUTE)
    public ActionResult optionsDouble(@FromCookie double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpOptions(route = BYTE_ROUTE)
    public ActionResult optionsByte(@FromCookie byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpOptions(route = SHORT_ROUTE)
    public ActionResult optionsShort(@FromCookie short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpOptions(route = FLOAT_ROUTE)
    public ActionResult optionsFloat(@FromCookie float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpOptions(route = CHAR_ROUTE)
    public ActionResult optionsChar(@FromCookie char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpOptions(route = LONG_ROUTE)
    public ActionResult optionsLong(@FromCookie long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion
}
