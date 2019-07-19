package io.javalin.mvc.api;

/**
 * Lists dependency injection dependencies used by the controllers. This interface must be extended
 * by the dependency injection container in the web project.
 */
public interface ControllerContainer {
    /**
     * Gets the {@link HttpContext} object wrapping of the current request.
     * @return the context object.
     */
    HttpContext getHttpContext();

    /**
     * Gets the {@link HttpRequest} representing the incoming request.
     * @return the request object.
     */
    HttpRequest getHttpRequest();

    /**
     * Gets the {@link HttpResponse} representing the response.
     * @return the response object.
     */
    HttpResponse getHttpResponse();

    /**
     * Gets the {@link ModelBinder} for converting request parameters to values and objects.
     * @return the model binder.
     */
    ModelBinder getModelBinder();
}
