package com.truncon.javalin.mvc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that an action method parameter should be bound to a different name.
 * This alters the default behavior of the model binder of looking for values with
 * the same name as the action method parameter.
 * @deprecated The {@link FromCookie}, {@link FromForm}, {@link FromHeader}, {@link FromPath},
 * and {@link FromQuery} annotations now accept the parameter, field or setter name, so
 * this annotation is obsolete.
 */
@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD})
public @interface Named {
    /**
     * The alternative name used to bind the parameter value.
     * @return the alternate name.
     */
    String value();
}
