package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.UserController;
import com.truncon.javalin.mvc.test.models.UserSearch;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getJsonResponseForGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

final class UserSearchTest {
    @Test
    void testGet_singleUser() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithPathParams(UserController.GET_USER_ROUTE, pathParams(
                param("userId", Integer.toString(123))
            ));
            UserSearch model = getJsonResponseForGet(route, UserSearch.class);
            Assertions.assertNotNull(model);
            Assertions.assertEquals((Integer) 123, model.getUserId());
        });
    }

    @Test
    void testGet_searchUsers() {
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
            Assertions.assertNotNull(model);
            Assertions.assertNull(model.getUserId());
            Assertions.assertEquals("Doe", model.getSearchValue());
            Assertions.assertEquals(100, model.getOffset());
            Assertions.assertEquals(10, model.getPageSize());
            Assertions.assertArrayEquals(new String[] { "lastName", "firstName", "hireDate" }, model.getOrderByFields());
        });
    }
}
