package com.truncon.javalin.mvc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a value should be bound from the URL path.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
public @interface FromPath {
    /**
     * Gets the name of the path parameter to retrieve the value(s) from, or blank
     * to use the name of the parameter, field, or setter.
     * @return The name of the path parameter.
     */
    String value() default "";
}
