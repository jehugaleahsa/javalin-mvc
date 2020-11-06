package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.DownloadResult;
import com.truncon.javalin.mvc.api.HttpGet;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Controller
public final class AsyncController {
    public static final String GET_CONTENT_ROUTE = "/api/async/content";
    public static final String GET_DOWNLOAD_ROUTE = "/api/async/download";

    @HttpGet(route = GET_CONTENT_ROUTE)
    public CompletableFuture<ActionResult> getContentAsync() {
        return CompletableFuture.supplyAsync(() -> new ContentResult("Hello"));
    }

    @HttpGet(route = GET_DOWNLOAD_ROUTE)
    public CompletableFuture<ActionResult> getDownloadAsync() {
        return CompletableFuture.supplyAsync(() -> {
            byte[] data = StandardCharsets.UTF_8.encode("Hello").array();
            return new DownloadResult(data, "text/plain");
        });
    }
}
