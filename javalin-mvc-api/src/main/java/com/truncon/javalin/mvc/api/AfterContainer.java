package com.truncon.javalin.mvc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Allows multiple after handlers to be defined on a single action method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AfterContainer {
    /**
     * The handlers associated with an action method.
     * @return the handlers associated with the action method.
     */
    After[] value() default {};
}
