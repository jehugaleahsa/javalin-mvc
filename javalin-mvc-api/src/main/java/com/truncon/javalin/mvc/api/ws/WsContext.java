package com.truncon.javalin.mvc.api.ws;

/**
 * Represents the WebSocket request being processed by the web application. It provide access to the
 * request information and allows for sending a response. Other utilities for handling requests
 * are provided, as well.
 */
public interface WsContext {
    /**
     * Gets the {@link WsRequest} object for retrieving the request details.
     * @return the request object.
     */
    WsRequest getRequest();

    /**
     * Gets the {@link WsResponse} object for sending a response.
     * @return the response object.
     */
    WsResponse getResponse();

    /**
     * Converts the given object to a JSON document.
     * @param data The object to serialize into JSON.
     * @return the serialized JSON document.
     */
    String toJson(Object data);

    /**
     * Deserializes the given JSON document as the desired object.
     * @param json The JSON document to deserialize.
     * @param dataClass The {@link Class} of the object to deserialize into.
     * @param <T> The type of the object being deserialized.
     * @return the deserialized object.
     */
    <T> T fromJson(String json, Class<T>  dataClass);

    /**
     * Gets access to the underlying implementation of the context.
     * @return An object.
     */
    Object getHandle();
}
