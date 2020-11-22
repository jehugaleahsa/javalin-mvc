package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.After;
import com.truncon.javalin.mvc.api.Before;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.StatusCodeResult;
import com.truncon.javalin.mvc.test.handlers.ErrorHandler;
import com.truncon.javalin.mvc.test.handlers.Log;

@Controller
public final class BeforeAfterController {
    public static final String BEFORE_AFTER_ROUTE = "/api/before-after";
    @HttpGet(route = BEFORE_AFTER_ROUTE)
    @Before(handler = Log.class, arguments = { "before-after-controller" })
    @After(handler = Log.class, arguments = { "before-after-controller" })
    @After(handler = ErrorHandler.class, arguments = { "before-after-controller" })
    public ActionResult getWithBeforeAfter() {
        return new StatusCodeResult(200);
    }
}
