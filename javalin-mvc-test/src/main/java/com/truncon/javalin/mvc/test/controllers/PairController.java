package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.test.models.Pair;

@Controller
public final class PairController {
    public static final String ROUTE = "/api/convert/pair/:value";
    @HttpGet(route = ROUTE)
    public ActionResult getPair(@FromPath Pair value) {
        return new ContentResult(value.toString());
    }
}
