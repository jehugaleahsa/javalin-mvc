package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.DownloadResult;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.HttpResponse;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.test.models.PrimitiveModel;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

@Controller
public final class AsyncController {
    public static final String GET_CONTENT_ROUTE = "/api/async/content";
    @HttpGet(route = GET_CONTENT_ROUTE)
    public CompletableFuture<ActionResult> getContentAsync() {
        return CompletableFuture.supplyAsync(() -> new ContentResult("Hello"));
    }

    public static final String GET_DOWNLOAD_ROUTE = "/api/async/download";
    @HttpGet(route = GET_DOWNLOAD_ROUTE)
    public CompletableFuture<ActionResult> getDownloadAsync() {
        return CompletableFuture.supplyAsync(() -> {
            byte[] data = StandardCharsets.UTF_8.encode("Hello").array();
            return new DownloadResult(data, "text/plain");
        });
    }

    public static final String GET_WILDCARD_ROUTE = "/api/async/wildcard/raw";
    @HttpGet(route = GET_WILDCARD_ROUTE)
    public CompletableFuture<?> getWildcard() {
        return CompletableFuture.supplyAsync(() -> new JsonResult(123));
    }

    public static final String GET_WILDCARD_EXTENDS_ACTION_RESULT_ROUTE = "/api/async/wildcard/action_result";
    @HttpGet(route = GET_WILDCARD_EXTENDS_ACTION_RESULT_ROUTE)
    public CompletableFuture<? extends ActionResult> getWildcardExtendsActionResult() {
        return CompletableFuture.supplyAsync(() -> new JsonResult(123));
    }

    public static final String GET_OBJECT_ROUTE = "/api/async/object";
    @HttpGet(route = GET_OBJECT_ROUTE)
    public CompletableFuture<PrimitiveModel> getObject() {
        return CompletableFuture.supplyAsync(PrimitiveModel::new);
    }

    public static final String GET_PRIMITIVE_INT_ROUTE = "/api/async/int";
    @HttpGet(route = GET_PRIMITIVE_INT_ROUTE)
    public CompletableFuture<Integer> getPrimitiveInt() {
        return CompletableFuture.supplyAsync(() -> 123);
    }

    public static final String GET_VOID_ROUTE = "/api/async/void";
    @HttpGet(route = GET_VOID_ROUTE)
    public CompletableFuture<Void> getVoid(HttpResponse response) {
        response.setStatusCode(200);
        response.setTextBody("Everything's fine");
        return CompletableFuture.completedFuture(null);
    }
}
