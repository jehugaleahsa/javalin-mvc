package com.truncon.javalin.mvc.api.ws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a class as a WebSocket controller.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface WsController {
    /**
     * Specifies the URL (a.k.a. route) that the annotated controller processes.
     * @return The route.
     */
    String route();
}
