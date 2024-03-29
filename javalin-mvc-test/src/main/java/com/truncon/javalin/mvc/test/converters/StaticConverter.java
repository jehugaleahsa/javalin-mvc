package com.truncon.javalin.mvc.test.converters;

import com.truncon.javalin.mvc.api.Converter;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.ValueSource;
import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import com.truncon.javalin.mvc.api.ws.WsValueSource;
import com.truncon.javalin.mvc.test.models.ConversionModel;

import java.util.List;
import java.util.Map;

public final class StaticConverter {
    private StaticConverter() {
    }

    @Converter("static-model-converter-context")
    public static ConversionModel convert(HttpContext context) {
        HttpRequest request = context.getRequest();
        return convert(request, null, ValueSource.Any);
    }

    @Converter("static-model-converter-request")
    public static ConversionModel convert(HttpRequest request) {
        return convert(request, null, ValueSource.Any);
    }

    @Converter("static-model-converter-context-name")
    public static ConversionModel convert(HttpContext context, String name) {
        HttpRequest request = context.getRequest();
        return convert(request, name, ValueSource.Any);
    }

    @Converter("static-model-converter-request-name")
    public static ConversionModel convert(HttpRequest request, String name) {
        return convert(request, name, ValueSource.Any);
    }

    @Converter("static-model-converter-context-source")
    public static ConversionModel convert(HttpContext context, ValueSource source) {
        HttpRequest request = context.getRequest();
        return convert(request, null, source);
    }

    @Converter("static-model-converter-request-source")
    public static ConversionModel convert(HttpRequest request, ValueSource source) {
        return convert(request, null, source);
    }

    @Converter("static-model-converter-context-name-source")
    public static ConversionModel convert(HttpContext context, String name, ValueSource valueSource) {
        HttpRequest request = context.getRequest();
        return convert(request, name, valueSource);
    }

    @Converter("static-model-converter-request-name-source")
    public static ConversionModel convert(HttpRequest request, String name, ValueSource valueSource) {
        Map<String, List<String>> lookup = request.getSourceLookup(valueSource);
        ConversionModel model = new ConversionModel();
        model.setBoolean(parseBoolean(getString(lookup, "boolean")));
        model.setByte((byte) (parseByte(getString(lookup, "byte")) * 2));
        model.setChar(Character.toUpperCase(parseChar(getString(lookup, "char"))));
        model.setDouble(parseDouble(getString(lookup, "double")) * 2);
        model.setFloat(parseFloat(getString(lookup, "float")) * 2);
        model.setInteger(parseInt(getString(lookup, "int")) * 2);
        model.setLong(parseLong(getString(lookup, "long")) * 2);
        model.setShort((short) (parseShort(getString(lookup, "short")) * 2));
        return model;
    }

    @Converter("static-model-converter-ws-context")
    public static ConversionModel convert(WsContext context) {
        WsRequest request = context.getRequest();
        return convert(request, null, WsValueSource.Any);
    }

    @Converter("static-model-converter-ws-request")
    public static ConversionModel convert(WsRequest request) {
        return convert(request, null, WsValueSource.Any);
    }

    @Converter("static-model-converter-ws-context-name")
    public static ConversionModel convert(WsContext context, String name) {
        WsRequest request = context.getRequest();
        return convert(request, name, WsValueSource.Any);
    }

    @Converter("static-model-converter-ws-request-name")
    public static ConversionModel convert(WsRequest request, String name) {
        return convert(request, name, WsValueSource.Any);
    }

    @Converter("static-model-converter-ws-context-source")
    public static ConversionModel convert(WsContext context, WsValueSource source) {
        WsRequest request = context.getRequest();
        return convert(request, null, source);
    }

    @Converter("static-model-converter-ws-request-source")
    public static ConversionModel convert(WsRequest request, WsValueSource source) {
        return convert(request, null, source);
    }

    @Converter("static-model-converter-ws-context-name-source")
    public static ConversionModel convert(WsContext context, String name, WsValueSource valueSource) {
        WsRequest request = context.getRequest();
        return convert(request, name, valueSource);
    }

    @Converter("static-model-converter-ws-request-name-source")
    public static ConversionModel convert(WsRequest request, String name, WsValueSource valueSource) {
        Map<String, List<String>> lookup = request.getSourceLookup(valueSource);
        ConversionModel model = new ConversionModel();
        model.setBoolean(parseBoolean(getString(lookup, "boolean")));
        model.setByte((byte) (parseByte(getString(lookup, "byte")) * 2));
        model.setChar(Character.toUpperCase(parseChar(getString(lookup, "char"))));
        model.setDouble(parseDouble(getString(lookup, "double")) * 2);
        model.setFloat(parseFloat(getString(lookup, "float")) * 2);
        model.setInteger(parseInt(getString(lookup, "int")) * 2);
        model.setLong(parseLong(getString(lookup, "long")) * 2);
        model.setShort((short) (parseShort(getString(lookup, "short")) * 2));
        return model;
    }

    private static String getString(Map<String, List<String>> lookup, String name) {
        List<String> values = lookup.get(name);
        return (values == null || values.isEmpty()) ? null : values.get(0);
    }

    private static boolean parseBoolean(String value) {
        return Boolean.parseBoolean(value);
    }

    private static byte parseByte(String value) {
        return value == null ? (byte) 0 : Byte.parseByte(value);
    }

    private static char parseChar(String value) {
        return (value == null || value.length() != 1) ? '\0' : value.charAt(0);
    }

    private static double parseDouble(String value) {
        return value == null ? 0.0 : Double.parseDouble(value);
    }

    private static float parseFloat(String value) {
        return value == null ? 0.0f : Float.parseFloat(value);
    }

    private static int parseInt(String value) {
        return value == null ? 0 : Integer.parseInt(value);
    }

    private static long parseLong(String value) {
        return value == null ? 0L : Long.parseLong(value);
    }

    private static short parseShort(String value) {
        return value == null ? (short) 0 : Short.parseShort(value);
    }
}
