package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.ContentResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.FromCookie;
import com.truncon.javalin.mvc.api.FromForm;
import com.truncon.javalin.mvc.api.FromHeader;
import com.truncon.javalin.mvc.api.FromPath;
import com.truncon.javalin.mvc.api.FromQuery;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.HttpPost;
import com.truncon.javalin.mvc.api.Named;

@Controller(prefix = NamedController.PREFIX)
public final class NamedController {
    public static final String PREFIX = "api/named";

    public static final String FROM_QUERY_STRING_ROUTE = "from_query_string";
    @HttpGet(route = FROM_QUERY_STRING_ROUTE)
    public ActionResult getFromQueryString(@FromQuery("value") String x) {
        return new ContentResult(x);
    }

    public static final String NAMED_QUERY_STRING_ROUTE = "named_query_string";
    @HttpGet(route = NAMED_QUERY_STRING_ROUTE)
    public ActionResult getNamedQueryString(@FromQuery @Named("value") String x) {
        return new ContentResult(x);
    }

    public static final String FROM_PATH_ROUTE = "from_path/{value}";
    @HttpGet(route = FROM_PATH_ROUTE)
    public ActionResult getFromPath(@FromPath("value") String x) {
        return new ContentResult(x);
    }

    public static final String NAMED_PATH_ROUTE = "named_path/{value}";
    @HttpGet(route = NAMED_PATH_ROUTE)
    public ActionResult getNamedPath(@FromPath @Named("value") String x) {
        return new ContentResult(x);
    }

    public static final String FROM_HEADER_ROUTE = "from_header";
    @HttpGet(route = FROM_HEADER_ROUTE)
    public ActionResult getFromHeader(@FromHeader("value") String x) {
        return new ContentResult(x);
    }

    public static final String NAMED_HEADER_ROUTE = "named_header";
    @HttpGet(route = NAMED_HEADER_ROUTE)
    public ActionResult getNamedHeader(@FromHeader @Named("value") String x) {
        return new ContentResult(x);
    }

    public static final String FROM_COOKIE_ROUTE = "from_cookie";
    @HttpGet(route = FROM_COOKIE_ROUTE)
    public ActionResult getFromCookie(@FromCookie("value") String x) {
        return new ContentResult(x);
    }

    public static final String NAMED_COOKIE_ROUTE = "named_cookie";
    @HttpGet(route = NAMED_COOKIE_ROUTE)
    public ActionResult getNamedCookie(@FromCookie @Named("value") String x) {
        return new ContentResult(x);
    }

    public static final String FROM_FORM_ROUTE = "from_form";
    @HttpPost(route = FROM_FORM_ROUTE)
    public ActionResult getFromForm(@FromForm("value") String x) {
        return new ContentResult(x);
    }

    public static final String NAMED_FORM_ROUTE = "named_form";
    @HttpPost(route = NAMED_FORM_ROUTE)
    public ActionResult getNamedForm(@FromForm @Named("value") String x) {
        return new ContentResult(x);
    }
}
