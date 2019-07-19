package io.javalin.mvc.api;

/**
 * Performs model binding for retrieving values from the request.
 */
public interface ModelBinder {
    /**
     * Gets a value from the request for a parameter with the given name and type. Normally,
     * the name of the parameter is whatever the parameter name in the action method is called; however,
     * this name can be overridden using the {@link Named} annotation. The conversion type
     * is also determined by looking at the action method parameter type. If the type is not a supported
     * primitive type, the value will be parsed as a JSON document and the name is ignored.
     * @param name The name of the parameter to search for, if applicable.
     * @param parameterClass The type to convert the value to.
     * @param valueSource Specifies where the value must be sourced.
     * @return the converted value or null if no value corresponds to the name or the parsing fails.
     */
    Object getValue(String name, Class<?> parameterClass, ValueSource valueSource);
}
