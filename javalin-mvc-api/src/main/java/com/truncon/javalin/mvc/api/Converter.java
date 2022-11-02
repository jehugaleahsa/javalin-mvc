package com.truncon.javalin.mvc.api;

import com.truncon.javalin.mvc.api.ws.WsCloseContext;
import com.truncon.javalin.mvc.api.ws.WsConnectContext;
import com.truncon.javalin.mvc.api.ws.WsContext;
import com.truncon.javalin.mvc.api.ws.WsErrorContext;
import com.truncon.javalin.mvc.api.ws.WsMessageContext;
import com.truncon.javalin.mvc.api.ws.WsValueSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a conversion method. Conversion methods must be methods accepting an {@link HttpContext},
 * {@link WsContext}, {@link WsConnectContext}, {@link WsCloseContext}, {@link WsErrorContext},
 * or a {@link WsMessageContext} object, the name of the value being bound (often
 * the name of the path parameter, query string key, header name, cookie name, or the form data field) and, optionally, the
 * specified source, if applicable, or {@link ValueSource#Any} or {@link WsValueSource#Any}
 * if no source is explicitly specified. If a parameter, method, or field is named,
 * that name will be passed as the name parameter to the converter.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface Converter {
    /**
     * Gets the unique name for the converter.
     * @return The unique name of the converter.
     */
    String value();
}
