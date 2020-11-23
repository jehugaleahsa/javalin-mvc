package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.UserController;
import com.truncon.javalin.mvc.test.models.UserSearch;
import org.junit.Assert;
import org.junit.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class UserSearchTest {
    @Test
    public void testGet_singleUser() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithPathParams(UserController.GET_USER_ROUTE, pathParams(
                param("userId", Integer.toString(123))
            ));
            UserSearch model = getJsonResponseForGet(route, UserSearch.class);
            Assert.assertNotNull(model);
            Assert.assertEquals((Integer) 123, model.getUserId());
        });
    }

    @Test
    public void testGet_searchUsers() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithQueryParams(UserController.GET_USERS_ROUTE, queryParams(
                param("search-value", "Doe"),
                param("offset", Integer.toString(100)),
                param("page-size", Integer.toString(10)),
                param("order-by", "lastName"),
                param("order-by", "firstName"),
                param("order-by", "hireDate")
            ));
            UserSearch model = getJsonResponseForGet(route, UserSearch.class);
            Assert.assertNotNull(model);
            Assert.assertNull(model.getUserId());
            Assert.assertEquals("Doe", model.getSearchValue());
            Assert.assertEquals(100, model.getOffset());
            Assert.assertEquals(10, model.getPageSize());
            Assert.assertArrayEquals(new String[] { "lastName", "firstName", "hireDate" }, model.getOrderByFields());
        });
    }
}
