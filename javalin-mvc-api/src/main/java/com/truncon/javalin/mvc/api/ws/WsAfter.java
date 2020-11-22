package com.truncon.javalin.mvc.api.ws;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies a {@link WsAfterActionHandler} to execute after a controller action completes.
 * String constant arguments will be passed to the handler.
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(WsAfterContainer.class)
@Target(ElementType.METHOD)
public @interface WsAfter {

    /**
     * The {@link Class} of the handler to execute after the controller executes.
     * The handler will fire regardless of whether an exception is thrown.
     * If an exception is throw, the exception will be passed to the handler.
     * @return the Class of the handler to execute after the controller executes.
     */
    Class<? extends WsAfterActionHandler> handler();

    /**
     * An optional list of String constant parameters used to customize the behavior of the handler.
     * @return the passed arguments.
     */
    String[] arguments() default {};
}
