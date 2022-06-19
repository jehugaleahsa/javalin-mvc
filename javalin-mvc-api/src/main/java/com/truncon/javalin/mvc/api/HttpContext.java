package com.truncon.javalin.mvc.api;

import java.io.InputStream;

/**
 * Represents the HTTP request being processed by the web application. It provide access to the
 * request information and allows for sending a response. Other utilities for handling requests
 * are provided, as well.
 */
public interface HttpContext {
    /**
     * Gets the {@link HttpRequest} object for retrieving the request details.
     * @return the request object.
     */
    HttpRequest getRequest();

    /**
     * Gets the {@link HttpResponse} object for sending a response.
     * @return the response object.
     */
    HttpResponse getResponse();

    /**
     * Converts the given object to a JSON string.
     * @param data The object to serialize into JSON.
     * @return the serialized JSON document.
     */
    String toJsonString(Object data);

    /**
     * Converts the given object to a JSON stream.
     * @param data The object to serialize into JSON.
     * @return The serialized JSON content.
     */
    InputStream toJsonStream(Object data);

    /**
     * Deserializes the given JSON document as the desired type.
     * @param json The JSON document to deserialize.
     * @param dataClass The {@link Class} of the object to deserialize to.
     * @param <T> The type of the object being deserialized.
     * @return the deserialized object.
     */
    <T> T fromJsonString(String json, Class<T>  dataClass);

    /**
     * Deserializes the given JSON document as the desired type.
     * @param json An {@link InputStream} over the JSON document to deserialize.
     * @param dataClass The {@link Class} of the object to deserialize to.
     * @param <T> The type of the object being deserialized.
     * @return The deserialized object.
     */
    <T> T fromJsonStream(InputStream json, Class<T> dataClass);

    /**
     * Gets access to the underlying implementation of the context.
     * @return An object.
     */
    Object getHandle();
}
