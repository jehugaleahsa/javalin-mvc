package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.ConversionUtils;
import com.truncon.javalin.mvc.ParameterCache;
import com.truncon.javalin.mvc.ParameterLookup;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.api.ValueSource;
import com.truncon.javalin.mvc.api.ws.*;

import java.nio.ByteBuffer;

/**
 * Performs model binding from header, URL path parameter, query string, form fields and the request body.
 */
public class DefaultWsModelBinder implements WsModelBinder {
    private final WsContext context;
    private final ParameterCache headers;
    private final ParameterCache cookies;
    private final ParameterCache pathParameters;
    private final ParameterCache queryStrings;
    private final ParameterLookup lookup;

    /**
     * Instantiates a new instance of a DefaultModelBinder.
     * @param context the current context object.
     */
    public DefaultWsModelBinder(WsContext context) {
        this.context = context;
        WsRequest request = context.getRequest();
        this.headers = new ParameterCache(ValueSource.Header, request::getHeaderLookup);
        this.cookies = new ParameterCache(ValueSource.Cookie, request::getCookieLookup);
        this.pathParameters = new ParameterCache(ValueSource.Path, request::getPathLookup);
        this.queryStrings = new ParameterCache(ValueSource.QueryString, request::getQueryLookup);
        this.lookup = new ParameterLookup();
        this.lookup.addCache(this.headers);
        this.lookup.addCache(this.cookies);
        this.lookup.addCache(this.pathParameters);
        this.lookup.addCache(this.queryStrings);
    }

    /**
     * Gets a value from the request for a parameter with the given name and type.
     * If the value is coming from a header, URL path parameter, query string or form field,
     * the name of the parameter will be used to lookup the value; this is a case-insensitive
     * lookup that removes any leading or trailing whitespace. Normally,
     * the name of the parameter is whatever the parameter name in the action method is called; however,
     * this name can be overridden using the {@link Named} annotation. The conversion type
     * is also determined by looking at the action method parameter type. If the type is not a supported
     * primitive type, the value will be parsed as a JSON document and the name is ignored.
     * @param name The name of the parameter to search for, if applicable.
     * @param parameterClass The type to convert the parameter to.
     * @param valueSource Specifies where the value must be sourced.
     * @return the converted value or null if no value corresponds to the name or the parsing fails.
     */
    @Override
    public Object getValue(String name, Class<?> parameterClass, WsValueSource valueSource) {
        // First see if the value is supposed to come from the message.
        if (valueSource == WsValueSource.Message) {
            return getDeserializedBody(parameterClass);
        }
        // Next, we see if the parameter is associated with a specific source.
        ParameterCache cache = getParameterCache(valueSource);
        if (cache == null) {
            // Either the source is not specified explicitly or the value should come from the body.
            cache = getParameterCache(name);
            if (cache == null) {
                if (ParameterLookup.hasBindings(parameterClass)) {
                    return lookup.bindValues(parameterClass, ValueSource.Any);
                } else {
                    // There was not a specific source, so we fallback on JSON deserialization.
                    return getDeserializedBody(parameterClass);
                }
            } else {
                return ConversionUtils.toParameterValue(parameterClass, cache.getValues(name)).orElse(null);
            }
        }
        // A specific source was determined. It is either an array, a "primitive",
        // or we are being asked to bind values inside of an object.
        if (!cache.hasValue(name)) {
            // We are being asked to pull from a specific source.
            // However, that source does not contain a parameter with that name or its value is nonsense.
            // We are probably being asked to bind values inside of an object.
            return lookup.bindValues(parameterClass, cache.getValueSource());
        }
        // A value with that name exists in the specific source, so try to bind it directly.
        // We can bind the value if it is an array or a "primitive".
        // Otherwise, try to bind the value inside of an object.
        ParameterCache finalCache = cache;
        return ConversionUtils.toParameterValue(parameterClass, cache.getValues(name))
            .orElseGet(() -> lookup.bindValues(parameterClass, finalCache.getValueSource()));
    }

    private Object getDeserializedBody(Class<?> paramType) {
        try {
            if (context instanceof WsMessageContext) {
                WsMessageContext messageContext = (WsMessageContext)context;
                if (paramType.equals(String.class)) {
                   return messageContext.getMessage();
                } else {
                    return messageContext.getMessage(paramType);
                }
            } else if (context instanceof WsBinaryMessageContext) {
                WsBinaryMessageContext binaryContext = (WsBinaryMessageContext) context;
                if (isByteArray(paramType)) {
                    return getByteArray(binaryContext);
                } else if (paramType.equals(ByteBuffer.class)) {
                    return getByteBuffer(binaryContext);
                }
            }
            return null;
        } catch (Exception exception) {
            // swallow any deserialization errors
            return null;
        }
    }

    private byte[] getByteArray(WsBinaryMessageContext binaryContext) {
        byte[] data = binaryContext.getData();
        if (binaryContext.getOffset() == null) {
            return data;
        }
        int length = binaryContext.getLength() == null
            ? data.length - binaryContext.getOffset()
            : binaryContext.getLength();
        byte[] result = new byte[length];
        System.arraycopy(data, binaryContext.getOffset(), result, 0, length);
        return result;
    }

    private ByteBuffer getByteBuffer(WsBinaryMessageContext binaryContext) {
        byte[] data = binaryContext.getData();
        if (binaryContext.getOffset() == null) {
            return ByteBuffer.wrap(data);
        }
        int length = binaryContext.getLength() == null
            ? data.length - binaryContext.getOffset()
            : binaryContext.getLength();
        return ByteBuffer.wrap(data, binaryContext.getOffset(), length);
    }

    private static boolean isByteArray(Class<?> paramType) {
        if (!paramType.isArray()) {
            return false;
        }
        Class<?> elementType = paramType.getComponentType();
        return elementType.equals(byte.class) || elementType.equals(Byte.class);
    }

    private ParameterCache getParameterCache(WsValueSource source) {
        switch (source) {
            case Path:
                return pathParameters;
            case Header:
                return headers;
            case Cookie:
                return cookies;
            case QueryString:
                return queryStrings;
            default:
                return null;
        }
    }

    private ParameterCache getParameterCache(String name) {
        if (pathParameters.hasValue(name)) {
            return pathParameters;
        } else if (headers.hasValue(name)) {
            return headers;
        } else if (cookies.hasValue(name)) {
            return cookies;
        } else if (queryStrings.hasValue(name)) {
            return queryStrings;
        } else {
            return null;
        }
    }
}
