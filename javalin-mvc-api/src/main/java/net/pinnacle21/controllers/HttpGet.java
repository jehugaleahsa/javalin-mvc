package io.javalin.mvc.api;

import java.lang.annotation.*;

/**
 * Marks a method as an action responding to requests with GET methods against the provided URL path.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HttpGet {
    /**
     * The URL path (a.k.a, route) associated with the action to execute.
     * @return the route.
     */
    String route();
}
