package io.javalin.mvc.api;

import java.lang.annotation.*;

/**
 * Specifies a {@link AfterActionHandler} to execute after a controller action completes.
 * String constant arguments will be passed to the handler.
 */
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AfterContainer.class)
@Target(ElementType.METHOD)
public @interface After {

    /**
     * The {@link Class} of the handler to execute after the controller executes.
     * The handler will fire regardless of whether an exception is thrown.
     * If an exception is throw, the exception will be passed to the handler.
     * @return the Class of the handler to execute after the controller executes.
     */
    Class<? extends AfterActionHandler> handler();

    /**
     * An optional list of String constant parameters used to customize the behavior of the handler.
     * @return the passed arguments.
     */
    String[] arguments() default {};
}
