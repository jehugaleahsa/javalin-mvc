package com.truncon.javalin.mvc.test.converters;

import com.truncon.javalin.mvc.api.Converter;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsDisconnectContext;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import com.truncon.javalin.mvc.test.models.ConversionModel;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class WsContextConverters {
    public WsContextConverters() {
    }

    @Converter("ws-connect-context-converter")
    public ConversionModel convertConnect(WsConnectContext context) {
        return getConversionModel(context);
    }

    @Converter("ws-disconnect-context-converter")
    public ConversionModel convertDisconnect(WsDisconnectContext context) {
        return getConversionModel(context);
    }

    @Converter("ws-error-context-converter")
    public ConversionModel convertError(WsErrorContext context) {
        return getConversionModel(context);
    }

    @Converter("ws-message-context-converter")
    public ConversionModel convertMessage(WsMessageContext context) {
        return getConversionModel(context);
    }

    @NotNull
    private ConversionModel getConversionModel(WsContext context) {
        WsRequest request = context.getRequest();
        Map<String, List<String>> lookup = request.getQueryLookup();
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
