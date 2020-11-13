package com.truncon.javalin.mvc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that a field, method, class, interface, or action method parameter should be constructed by
 * calling the named converter. If a {@link UseConverter} annotation is applied to a class and on an
 * action method parameter, the action method parameter annotation takes precedence. Similarly, field and
 * method annotations override type annotations on their member types.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.TYPE })
public @interface UseConverter {
    /**
     * Gets the unique name of the converter to use.
     * @return The unique name of the converter to use.
     */
    String value();
}
