package io.javalin.mvc.api.openapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpenApiRequestBody {
    String description() default "";
    boolean required() default false;
    Class<?> modelType() default void.class;
    String mimeType() default "";
}
