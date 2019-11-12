package com.truncon.javalin.mvc.api;

import java.lang.annotation.*;

/**
 * Specifies a {@link BeforeActionHandler} to execute before a controller action executes.
 * String constant arguments will be passed to the handler.
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(BeforeContainer.class)
@Target(ElementType.METHOD)
public @interface Before {

    /**
     * The {@link Class} of the handler to execute before the controller executes.
     * If the handler returns false, the request will be cancelled.
     * @return the Class of the handler to execute before the controller executes.
     */
    Class<? extends BeforeActionHandler> handler();

    /**
     * An optional list of String constant parameters used to customize the behavior of the handler.
     * @return the passed arguments.
     */
    String[] arguments() default {};
}
