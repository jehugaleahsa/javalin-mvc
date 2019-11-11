package io.javalin.mvc.api.ws;

import io.javalin.mvc.api.HttpContext;
import io.javalin.mvc.api.HttpRequest;
import io.javalin.mvc.api.HttpResponse;
import io.javalin.mvc.api.ModelBinder;

/**
 * Lists dependency injection dependencies used by the WebSocket controllers. This interface must be extended
 * by the dependency injection container in the web project.
 */
public interface WsControllerContainer {
    /**
     * Gets the {@link WsContext} object wrapping of the current request.
     * @return the context object.
     */
    WsContext getWsContext();

    /**
     * Gets the {@link WsRequest} representing the incoming request.
     * @return the request object.
     */
    WsRequest getWsRequest();

    /**
     * Gets the {@link WsResponse} representing the response.
     * @return the response object.
     */
    WsResponse getWsResponse();
}
