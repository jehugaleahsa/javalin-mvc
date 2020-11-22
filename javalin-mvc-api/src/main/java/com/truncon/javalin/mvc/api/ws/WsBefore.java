package com.truncon.javalin.mvc.api.ws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a {@link WsBeforeActionHandler} to execute before a controller action executes.
 * String constant arguments will be passed to the handler.
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(WsBeforeContainer.class)
@Target(ElementType.METHOD)
public @interface WsBefore {

    /**
     * The {@link Class} of the handler to execute before the controller executes.
     * If the handler returns false, the request will be cancelled.
     * @return the Class of the handler to execute before the controller executes.
     */
    Class<? extends WsBeforeActionHandler> handler();

    /**
     * An optional list of String constant parameters used to customize the behavior of the handler.
     * @return the passed arguments.
     */
    String[] arguments() default {};
}
