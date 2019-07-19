package io.javalin.mvc.api.openapi;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OpenApiResponseContainer.class)
@Target(ElementType.METHOD)
public @interface OpenApiResponse {
    String status();
    String description() default "";
    Class<?> modelType() default void.class;
    String mimeType() default "";
}
