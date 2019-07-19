package io.javalin.mvc.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as an action responding to requests with CONNECT methods against the provided URL path.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpConnect {
    /**
     * The URL path (a.k.a, route) associated with the action to execute.
     * @return the route.
     */
    String route();
}
