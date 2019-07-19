package io.javalin.mvc.api.openapi;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(OpenApiFileUploadContainer.class)
@Target(ElementType.METHOD)
public @interface OpenApiFileUpload {
    String name();
    String description() default "";
    boolean required() default false;
}
