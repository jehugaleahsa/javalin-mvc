package com.truncon.javalin.mvc.api;

/**
 * Represents a generic interface that runtime dependency injection frameworks
 * must comply to.
 */
public interface Injector {
    /**
     * Gets an instance of the bound implementation for the requested class type.
     * @param clz The {@link Class} of object to retrieve.
     * @return The instance.
     * @param <T> The type of the object to retrieve.
     */
    <T> T getInstance(Class<T> clz);

    /**
     * Provides access to the underlying implementation of the interface.
     * @return The underlying DI container implementation.
     */
    Object getHandle();
}
