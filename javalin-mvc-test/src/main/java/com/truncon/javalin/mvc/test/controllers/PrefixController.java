package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.HttpPost;
import com.truncon.javalin.mvc.api.HttpPut;
import com.truncon.javalin.mvc.api.StatusCodeResult;

@Controller(prefix = PrefixController.ROUTE_PREFIX)
public final class PrefixController {
    public static final String ROUTE_PREFIX = "/api/prefix";

    public static final String GET_ROUTE = "/api/prefix";
    @HttpGet(route = "")
    public ActionResult get() {
        return new StatusCodeResult(200);
    }

    public static final String POST_ROUTE = "/api/prefix/post";
    @HttpPost(route = "/post")
    public ActionResult post() {
        return new StatusCodeResult(200);
    }

    public static final String PUT_ROUTE = "/api/prefix/put";
    @HttpPut(route = "put")
    public ActionResult put() {
        return new StatusCodeResult(200);
    }
}
