package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpDelete;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.HttpHead;
import com.truncon.javalin.mvc.api.HttpOptions;
import com.truncon.javalin.mvc.api.HttpPatch;
import com.truncon.javalin.mvc.api.HttpPost;
import com.truncon.javalin.mvc.api.HttpPut;

@Controller
public class HttpMethodController {
    public static final String GET_ROUTE = "/api/methods/get";
    @HttpGet(route = GET_ROUTE)
    public ActionResult get() {
        return new ContentResult("GET");
    }

    public static final String POST_ROUTE = "/api/methods/post";
    @HttpPost(route = POST_ROUTE)
    public ActionResult post() {
        return new ContentResult("POST");
    }

    public static final String PUT_ROUTE = "/api/methods/put";
    @HttpPut(route = PUT_ROUTE)
    public ActionResult put() {
        return new ContentResult("PUT");
    }

    public static final String DELETE_ROUTE = "/api/methods/delete";
    @HttpDelete(route = DELETE_ROUTE)
    public ActionResult delete() {
        return new ContentResult("DELETE");
    }

    public static final String PATCH_ROUTE = "/api/methods/patch";
    @HttpPatch(route = PATCH_ROUTE)
    public ActionResult patch() {
        return new ContentResult("PATCH");
    }

    public static final String HEAD_ROUTE = "/api/methods/head";
    @HttpHead(route = HEAD_ROUTE)
    public ActionResult head() {
        return new ContentResult("HEAD");
    }

    public static final String OPTIONS_ROUTE = "/api/methods/options";
    @HttpOptions(route = OPTIONS_ROUTE)
    public ActionResult options() {
        return new ContentResult("OPTIONS");
    }
}
