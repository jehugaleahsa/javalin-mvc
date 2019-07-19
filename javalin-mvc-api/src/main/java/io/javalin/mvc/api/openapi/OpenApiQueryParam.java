package io.javalin.mvc.api.openapi;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OpenApiQueryParamContainer.class)
@Target(ElementType.METHOD)
public @interface OpenApiQueryParam {
    String name();
    Class<?> type() default void.class;
    String description() default "";
    boolean deprecated() default false;
    boolean required() default false;
    boolean allowEmptyValue() default false;
}
