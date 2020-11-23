package com.truncon.javalin.mvc.test.controllers;

import com.truncon.javalin.mvc.api.ActionResult;
import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.HttpGet;
import com.truncon.javalin.mvc.api.JsonResult;
import com.truncon.javalin.mvc.test.models.UserSearch;

@Controller
public final class UserController {
    public static final String GET_USER_ROUTE  = "/api/users/:userId";
    @HttpGet(route = GET_USER_ROUTE)
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
