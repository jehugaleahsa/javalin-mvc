package io.javalin.mvc.api.ws;

public interface WsMessageContext {
    WsContext getContext();

    /**
     * Gets the message that was sent.
     * @return The sent message.
     */
    String getMessage();

    /**
     * Deserializes the JSON message as the desired object.
     * @param dataClass The {@link Class} of the object to deserialize into.
     * @param <T> The type of the object being deserialized.
     * @return the deserialized object.
     */
    default <T> T getMessage(Class<T>  dataClass) {
        WsContext context = getContext();
        return context.fromJson(getMessage(), dataClass);
    }
}
