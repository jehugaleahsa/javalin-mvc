package com.truncon.javalin.mvc.api;

/**
 * Specifies where a value should be sourced by model binding.
 */
public enum ValueSource {
    /**
     * The value can be retrieved from any value source.
     */
    Any,
    /**
     * The value must be retrieved from the request header.
     */
    Header,
    /**
     * The value must be retrieved from a cookie.
     */
    Cookie,
    /**
     * The value must be retrieved from the URL path.
     */
    Path,
    /**
     * The value must be retrieved from the query string.
     */
    QueryString,
    /**
     * The value must be retrieved from URL encoded form data.
     */
    FormData,
    /**
     * The value must be retrieved from the request body that is formatted JSON.
     */
    Json
}
