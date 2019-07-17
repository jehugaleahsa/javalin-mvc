package io.javalin.mvc.api.openapi;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OpenApiCookieParamContainer.class)
@Target(ElementType.METHOD)
public @interface OpenApiCookieParam {
    String name();
    Class<?> type() default String.class;
    String description() default "";
    boolean deprecated() default false;
    boolean required() default false;
    boolean allowEmptyValue() default false;
}
