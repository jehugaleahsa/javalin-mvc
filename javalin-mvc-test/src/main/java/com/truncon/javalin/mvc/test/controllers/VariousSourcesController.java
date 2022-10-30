package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.DefaultValue;
import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromForm;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpPost;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.api.Named;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;

import jakarta.ws.rs.CookieParam;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import java.util.List;

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

    public static final String VARIOUS_SOURCES_BUILTIN_DEFAULTS_ROUTE = "/api/various_sources/parameters/builtin-with-defaults";
    @HttpPost(route = VARIOUS_SOURCES_BUILTIN_DEFAULTS_ROUTE)
    public ActionResult postVariousSourcesBuiltinWithDefaults(
            @PathParam("value") @DefaultValue("path") String path,
            @QueryParam("value") @DefaultValue("query") String query,
            @HeaderParam("value") @DefaultValue("header") String header,
            @CookieParam("value") @DefaultValue("cookie") String cookie,
            @FormParam("value") @DefaultValue("form") String form,
            @DefaultValue("any") @Named("value") String any) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(path);
        model.setQuery(query);
        model.setHeader(header);
        model.setCookie(cookie);
        model.setForm(form);
        model.setAny(any);
        return new JsonResult(model);
    }

    public static final String VARIOUS_SOURCES_STANDARD_DEFAULTS_ROUTE = "/api/various_sources/parameters/standard-with-defaults";
    @HttpPost(route = VARIOUS_SOURCES_STANDARD_DEFAULTS_ROUTE)
    public ActionResult postVariousSourcesStandardWithDefaults(
            @PathParam("value") @jakarta.ws.rs.DefaultValue("path") String path,
            @QueryParam("value") @jakarta.ws.rs.DefaultValue("query") String query,
            @HeaderParam("value") @jakarta.ws.rs.DefaultValue("header") String header,
            @CookieParam("value") @jakarta.ws.rs.DefaultValue("cookie") String cookie,
            @FormParam("value") @jakarta.ws.rs.DefaultValue("form") String form,
            @jakarta.ws.rs.DefaultValue("any") @Named("value") String any) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(path);
        model.setQuery(query);
        model.setHeader(header);
        model.setCookie(cookie);
        model.setForm(form);
        model.setAny(any);
        return new JsonResult(model);
    }

    public static final String VARIOUS_SOURCES_COLLECTIONS_BUILTIN_DEFAULTS_ROUTE = "/api/various_sources/parameters/collections/builtin-with-defaults";
    @HttpPost(route = VARIOUS_SOURCES_COLLECTIONS_BUILTIN_DEFAULTS_ROUTE)
    public ActionResult postVariousSourcesCollectionsBuiltinWithDefaults(
            @PathParam("value") @DefaultValue("path") List<String> paths,
            @QueryParam("value") @DefaultValue("query") List<String> queries,
            @HeaderParam("value") @DefaultValue("header") List<String> headers,
            @CookieParam("value") @DefaultValue("cookie") List<String> cookies,
            @FormParam("value") @DefaultValue("form") List<String> forms,
            @Named("value") @DefaultValue("any") List<String> anyValues) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(paths.size() == 1 ? paths.get(0) : null);
        model.setQuery(queries.size() == 1 ? queries.get(0) : null);
        model.setHeader(headers.size() == 1 ? headers.get(0) : null);
        model.setCookie(cookies.size() == 1 ? cookies.get(0) : null);
        model.setForm(forms.size() == 1 ? forms.get(0) : null);
        model.setAny(anyValues.size() == 1 ? anyValues.get(0) : null);
        return new JsonResult(model);
    }

    public static final String VARIOUS_SOURCES_COLLECTIONS_STANDARD_DEFAULTS_ROUTE = "/api/various_sources/parameters/collections/standard-with-defaults";
    @HttpPost(route = VARIOUS_SOURCES_COLLECTIONS_STANDARD_DEFAULTS_ROUTE)
    public ActionResult postVariousSourcesCollectionsStandardWithDefaults(
            @PathParam("value") @jakarta.ws.rs.DefaultValue("path") List<String> paths,
            @QueryParam("value") @jakarta.ws.rs.DefaultValue("query") List<String> queries,
            @HeaderParam("value") @jakarta.ws.rs.DefaultValue("header") List<String> headers,
            @CookieParam("value") @jakarta.ws.rs.DefaultValue("cookie") List<String> cookies,
            @FormParam("value") @jakarta.ws.rs.DefaultValue("form") List<String> forms,
            @Named("value") @jakarta.ws.rs.DefaultValue("any") List<String> anyValues) {
        VariousSourcesModel model = new VariousSourcesModel();
        model.setPath(paths.size() == 1 ? paths.get(0) : null);
        model.setQuery(queries.size() == 1 ? queries.get(0) : null);
        model.setHeader(headers.size() == 1 ? headers.get(0) : null);
        model.setCookie(cookies.size() == 1 ? cookies.get(0) : null);
        model.setForm(forms.size() == 1 ? forms.get(0) : null);
        model.setAny(anyValues.size() == 1 ? anyValues.get(0) : null);
        return new JsonResult(model);
    }
}
