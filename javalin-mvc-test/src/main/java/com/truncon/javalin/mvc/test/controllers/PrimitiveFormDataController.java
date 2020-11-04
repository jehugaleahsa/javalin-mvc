package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromForm;
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
public class PrimitiveFormDataController {
    public static final String BOOLEAN_ROUTE = "/api/primitives/form-data/boolean";
    public static final String INTEGER_ROUTE = "/api/primitives/form-data/int";
    public static final String DOUBLE_ROUTE = "/api/primitives/form-data/double";
    public static final String BYTE_ROUTE = "/api/primitives/form-data/byte";
    public static final String SHORT_ROUTE = "/api/primitives/form-data/short";
    public static final String FLOAT_ROUTE = "/api/primitives/form-data/float";
    public static final String CHAR_ROUTE = "/api/primitives/form-data/char";
    public static final String LONG_ROUTE = "/api/primitives/form-data/long";

    // region GET

    @HttpGet(route = BOOLEAN_ROUTE)
    public ActionResult getBoolean(@FromForm boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpGet(route = INTEGER_ROUTE)
    public ActionResult getInt(@FromForm int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpGet(route = DOUBLE_ROUTE)
    public ActionResult getDouble(@FromForm double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpGet(route = BYTE_ROUTE)
    public ActionResult getByte(@FromForm byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpGet(route = SHORT_ROUTE)
    public ActionResult getShort(@FromForm short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpGet(route = FLOAT_ROUTE)
    public ActionResult getFloat(@FromForm float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpGet(route = CHAR_ROUTE)
    public ActionResult getChar(@FromForm char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpGet(route = LONG_ROUTE)
    public ActionResult getLong(@FromForm long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region POST

    @HttpPost(route = BOOLEAN_ROUTE)
    public ActionResult postBoolean(@FromForm boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPost(route = INTEGER_ROUTE)
    public ActionResult postInt(@FromForm int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPost(route = DOUBLE_ROUTE)
    public ActionResult postDouble(@FromForm double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPost(route = BYTE_ROUTE)
    public ActionResult postByte(@FromForm byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPost(route = SHORT_ROUTE)
    public ActionResult postShort(@FromForm short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPost(route = FLOAT_ROUTE)
    public ActionResult postFloat(@FromForm float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPost(route = CHAR_ROUTE)
    public ActionResult postChar(@FromForm char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPost(route = LONG_ROUTE)
    public ActionResult postLong(@FromForm long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PUT

    @HttpPut(route = BOOLEAN_ROUTE)
    public ActionResult putBoolean(@FromForm boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPut(route = INTEGER_ROUTE)
    public ActionResult putInt(@FromForm int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPut(route = DOUBLE_ROUTE)
    public ActionResult putDouble(@FromForm double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPut(route = BYTE_ROUTE)
    public ActionResult putByte(@FromForm byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPut(route = SHORT_ROUTE)
    public ActionResult putShort(@FromForm short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPut(route = FLOAT_ROUTE)
    public ActionResult putFloat(@FromForm float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPut(route = CHAR_ROUTE)
    public ActionResult putChar(@FromForm char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPut(route = LONG_ROUTE)
    public ActionResult putLong(@FromForm long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region PATCH

    @HttpPatch(route = BOOLEAN_ROUTE)
    public ActionResult patchBoolean(@FromForm boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpPatch(route = INTEGER_ROUTE)
    public ActionResult patchInt(@FromForm int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpPatch(route = DOUBLE_ROUTE)
    public ActionResult patchDouble(@FromForm double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpPatch(route = BYTE_ROUTE)
    public ActionResult patchByte(@FromForm byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpPatch(route = SHORT_ROUTE)
    public ActionResult patchShort(@FromForm short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpPatch(route = FLOAT_ROUTE)
    public ActionResult patchFloat(@FromForm float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpPatch(route = CHAR_ROUTE)
    public ActionResult patchChar(@FromForm char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpPatch(route = LONG_ROUTE)
    public ActionResult patchLong(@FromForm long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region DELETE

    @HttpDelete(route = BOOLEAN_ROUTE)
    public ActionResult deleteBoolean(@FromForm boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpDelete(route = INTEGER_ROUTE)
    public ActionResult deleteInt(@FromForm int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpDelete(route = DOUBLE_ROUTE)
    public ActionResult deleteDouble(@FromForm double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpDelete(route = BYTE_ROUTE)
    public ActionResult deleteByte(@FromForm byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpDelete(route = SHORT_ROUTE)
    public ActionResult deleteShort(@FromForm short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpDelete(route = FLOAT_ROUTE)
    public ActionResult deleteFloat(@FromForm float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpDelete(route = CHAR_ROUTE)
    public ActionResult deleteChar(@FromForm char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpDelete(route = LONG_ROUTE)
    public ActionResult deleteLong(@FromForm long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion

    // region HEAD

    @HttpHead(route = BOOLEAN_ROUTE)
    public ActionResult headBoolean(HttpResponse response, @FromForm boolean value) {
        response.setHeader("result", Boolean.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = INTEGER_ROUTE)
    public ActionResult headInt(HttpResponse response, @FromForm int value) {
        response.setHeader("result", Integer.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = DOUBLE_ROUTE)
    public ActionResult headDouble(HttpResponse response, @FromForm double value) {
        response.setHeader("result", Double.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = BYTE_ROUTE)
    public ActionResult headByte(HttpResponse response, @FromForm byte value) {
        response.setHeader("result", Byte.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = SHORT_ROUTE)
    public ActionResult headShort(HttpResponse response, @FromForm short value) {
        response.setHeader("result", Short.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = FLOAT_ROUTE)
    public ActionResult headFloat(HttpResponse response, @FromForm float value) {
        response.setHeader("result", Float.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = CHAR_ROUTE)
    public ActionResult headChar(HttpResponse response, @FromForm char value) {
        response.setHeader("result", Character.toString(value));
        return new StatusCodeResult(200);
    }

    @HttpHead(route = LONG_ROUTE)
    public ActionResult headLong(HttpResponse response, @FromForm long value) {
        response.setHeader("result", Long.toString(value));
        return new StatusCodeResult(200);
    }

    // endregion

    // region OPTIONS

    @HttpOptions(route = BOOLEAN_ROUTE)
    public ActionResult optionsBoolean(@FromForm boolean value) {
        return new ContentResult(Boolean.toString(value));
    }

    @HttpOptions(route = INTEGER_ROUTE)
    public ActionResult optionsInt(@FromForm int value) {
        return new ContentResult(Integer.toString(value));
    }

    @HttpOptions(route = DOUBLE_ROUTE)
    public ActionResult optionsDouble(@FromForm double value) {
        return new ContentResult(Double.toString(value));
    }

    @HttpOptions(route = BYTE_ROUTE)
    public ActionResult optionsByte(@FromForm byte value) {
        return new ContentResult(Byte.toString(value));
    }

    @HttpOptions(route = SHORT_ROUTE)
    public ActionResult optionsShort(@FromForm short value) {
        return new ContentResult(Short.toString(value));
    }

    @HttpOptions(route = FLOAT_ROUTE)
    public ActionResult optionsFloat(@FromForm float value) {
        return new ContentResult(Float.toString(value));
    }

    @HttpOptions(route = CHAR_ROUTE)
    public ActionResult optionsChar(@FromForm char value) {
        return new ContentResult(Character.toString(value));
    }

    @HttpOptions(route = LONG_ROUTE)
    public ActionResult optionsLong(@FromForm long value) {
        return new ContentResult(Long.toString(value));
    }

    // endregion
}
