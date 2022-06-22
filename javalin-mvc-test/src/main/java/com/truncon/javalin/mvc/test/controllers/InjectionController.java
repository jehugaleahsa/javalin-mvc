package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.After;
import com.truncon.javalin.mvc.api.Before;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.test.handlers.InjectionHandler;
import com.truncon.javalin.mvc.test.models.InjectionModel;
import com.truncon.javalin.mvc.test.utils.Dependency;

import javax.inject.Inject;

@Controller
public class InjectionController {
    private final Dependency dependency;

    @Inject
    public InjectionController(Dependency dependency) {
        this.dependency = dependency;
    }

    public static final String GET_CONTROLLER_ROUTE = "/api/di/controller";
    @HttpGet(route = GET_CONTROLLER_ROUTE)
    public ActionResult getDependencyValue() {
        return new ContentResult(dependency.getValue());
    }

    public static final String GET_BEFORE_HANDLER_ROUTE = "/api/di/before_handler";
    @HttpGet(route = GET_BEFORE_HANDLER_ROUTE)
    @Before(handler = InjectionHandler.class)
    public ActionResult getBeforeHandler() {
        return new ContentResult("if you see this, then the before handler didn't cancel the request.");
    }

    public static final String GET_AFTER_HANDLER_ROUTE = "/api/di/after_handler";
    @HttpGet(route = GET_AFTER_HANDLER_ROUTE)
    @After(handler = InjectionHandler.class)
    public ActionResult getAfterHandler() {
        return new ContentResult("if you see this, then the after handler didn't override the response.");
    }

    public static final String GET_CONVERTER_ROUTE = "/api/di/converter";
    @HttpGet(route = GET_CONVERTER_ROUTE)
    public ActionResult getConverterHandler(@UseConverter("local-date-time-converter") String value) {
        return new ContentResult(value);
    }

    public static final String GET_MODEL_ROUTE = "/api/di/model";
    @HttpGet(route = GET_MODEL_ROUTE)
    public ActionResult getModelHandler(InjectionModel model) {
        return new JsonResult(model);
    }
}
