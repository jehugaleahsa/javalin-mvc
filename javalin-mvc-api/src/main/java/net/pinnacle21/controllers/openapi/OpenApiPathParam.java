package io.javalin.mvc.api.openapi;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OpenApiPathParamContainer.class)
@Target(ElementType.METHOD)
public @interface OpenApiPathParam {
    String name();
    Class<?> type() default String.class;
    String description() default "";
    boolean deprecated() default false;
    boolean required() default false;
    boolean allowEmptyValue() default false;
}
