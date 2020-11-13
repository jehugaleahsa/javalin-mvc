package com.truncon.javalin.mvc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a conversion method. Conversion methods must be methods accepting an {@link HttpContext},
 * {@link com.truncon.javalin.mvc.api.ws.WsContext}, {@link com.truncon.javalin.mvc.api.ws.WsConnectContext},
 * {@link com.truncon.javalin.mvc.api.ws.WsDisconnectContext}, {@link com.truncon.javalin.mvc.api.ws.WsErrorContext},
 * or a {@link com.truncon.javalin.mvc.api.ws.WsMessageContext} object, the name of the value being bound (often
 * the name of the path parameter, query string key, header name, cookie name, or the form data field) and, optionally, the
 * specified source, if applicable, or {@link ValueSource#Any} or {@link com.truncon.javalin.mvc.api.ws.WsValueSource#Any}
 * if no source is explicitly specified. If a parameter, method, or field is annotated using the {@link Named} annotation,
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
