package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.test.models.UserSearch;
import io.javalin.openapi.HttpMethod;
import io.javalin.openapi.OpenApi;
import io.javalin.openapi.OpenApiContent;
import io.javalin.openapi.OpenApiParam;
import io.javalin.openapi.OpenApiResponse;

@Controller
public final class UserController {
    public static final String GET_USER_ROUTE  = "/api/users/{userId}";
    @HttpGet(route = GET_USER_ROUTE)
    @OpenApi(
        path = GET_USER_ROUTE,
        methods = { HttpMethod.GET },
        summary = "Gets the user with the given ID",
        pathParams = {
            @OpenApiParam(name = "userId", required = true, description = "The ID of the user to retrieve", type = Integer.class)
        },
        operationId = "getUser",
        responses = {
            @OpenApiResponse(status = "200", description = "", content = @OpenApiContent(from=UserSearch.class, mimeType = "application/json"))
        }
    )
    public ActionResult getUser(UserSearch search) {
        // Write code to do search here
        return new JsonResult(search);
    }

    public static final String GET_USERS_ROUTE = "/api/users";
    @HttpGet(route = GET_USERS_ROUTE)
    public ActionResult getUsers(UserSearch search) {
        // Write code to do search here
        return new JsonResult(search);
    }
}
