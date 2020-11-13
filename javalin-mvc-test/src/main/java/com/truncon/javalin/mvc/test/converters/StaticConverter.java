package com.truncon.javalin.mvc.test.converters;

import com.truncon.javalin.mvc.api.Converter;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.ValueSource;
import com.truncon.javalin.mvc.test.models.ConversionModel;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public final class StaticConverter {
    private StaticConverter() {
    }

    @Converter("static-model-converter")
    public static ConversionModel convert(HttpContext context, String name, ValueSource valueSource) {
        HttpRequest request = context.getRequest();
        Map<String, Collection<String>> lookup = getSourceLookup(request, valueSource);
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

    private static Map<String, Collection<String>> getSourceLookup(HttpRequest request, ValueSource valueSource) {
        switch (valueSource) {
            case Path:
                return request.getPathLookup();
            case QueryString:
                return request.getQueryLookup();
            case Header:
                return request.getHeaderLookup();
            case Cookie:
                return request.getCookieLookup();
            case FormData:
                return request.getFormLookup();
            default: {
                Map<String, Collection<String>> lookup = new LinkedHashMap<>();
                lookup.putAll(request.getFormLookup());
                lookup.putAll(request.getQueryLookup());
                lookup.putAll(request.getPathLookup());
                lookup.putAll(request.getCookieLookup());
                lookup.putAll(request.getHeaderLookup());
                return lookup;
            }
        }
    }

    private static String getString(Map<String, Collection<String>> lookup, String name) {
        Collection<String> values = lookup.get(name);
        return (values == null || values.isEmpty()) ? null : values.iterator().next();
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
