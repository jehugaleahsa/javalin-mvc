package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromForm;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpPost;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;

import javax.ws.rs.CookieParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

@Controller
public final class VariousSourcesController {
    public static final String VARIOUS_SOURCES_BUILTIN_ROUTE = "/api/various_sources/parameters/builtin/{value}";
    @HttpPost(route = VARIOUS_SOURCES_BUILTIN_ROUTE)
    public ActionResult postVariousSourcesBuiltin(
                @FromPath("value") String path,
                @FromQuery("value") String query,
                @FromHeader("value") String header,
                @FromCookie("value") String cookie,
                @FromForm("value") String form) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(path);
        model.setQuery(query);
        model.setHeader(header);
        model.setCookie(cookie);
        model.setForm(form);
        return new JsonResult(model);
    }

    public static final String VARIOUS_SOURCES_STANDARD_ROUTE = "/api/various_sources/parameters/standard/{value}";
    @HttpPost(route = VARIOUS_SOURCES_STANDARD_ROUTE)
    public ActionResult postVariousSourcesStandard(
            @PathParam("value") String path,
            @QueryParam("value") String query,
            @HeaderParam("value") String header,
            @CookieParam("value") String cookie,
            @FormParam("value") String form) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(path);
        model.setQuery(query);
        model.setHeader(header);
        model.setCookie(cookie);
        model.setForm(form);
        return new JsonResult(model);
    }
}
