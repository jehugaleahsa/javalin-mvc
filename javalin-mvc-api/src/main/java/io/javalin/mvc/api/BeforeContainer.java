package io.javalin.mvc.api;

import java.lang.annotation.*;

/**
 * Allows multiple before handlers to be defined on a single action method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface BeforeContainer {
    /**
     * The handlers associated with an action method.
     * @return the handlers associated with the action method.
     */
    Before[] value() default {};
}
