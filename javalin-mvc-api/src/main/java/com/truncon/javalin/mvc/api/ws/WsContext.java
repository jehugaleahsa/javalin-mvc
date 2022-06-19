package com.truncon.javalin.mvc.api.ws;

import java.io.InputStream;

/**
 * Represents the WebSocket request being processed by the web application. It provide access to the
 * request information and allows for sending a response. Other utilities for handling requests
 * are provided, as well.
 */
public interface WsContext {
    /**
     * Gets the unique ID of the session.
     * @return The unique session ID.
     */
    String getSessionId();

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
    String toJsonString(Object data);

    /**
     * Converts the given object to a JSON stream.
     * @param data The object to serialize into JSON.
     * @return The serialized JSON document.
     */
    InputStream toJsonStream(Object data);

    /**
     * Deserializes the given JSON document to the desired type.
     * @param json The JSON document to deserialize.
     * @param dataClass The {@link Class} of the object to deserialize to.
     * @param <T> The type of the object being deserialized.
     * @return the deserialized object.
     */
    <T> T fromJsonString(String json, Class<T>  dataClass);

    /**
     * Deserializes the given JSON document to the desired type.
     * @param json The JSON document to deserialize.
     * @param dataClass The {@link Class} of the object to deserialize to.
     * @param <T> The type of the object being deserialized.
     * @return The deserialized object.
     */
    <T> T fromJsonStream(InputStream json, Class<T>  dataClass);

    /**
     * Gets access to the underlying implementation of the context.
     * @return An object.
     */
    Object getHandle();
}
