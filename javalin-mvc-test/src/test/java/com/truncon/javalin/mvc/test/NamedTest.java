package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.NamedController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.QueryUtils.getStringForCookiesAndGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForFormDataAndPost;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForGet;
import static com.truncon.javalin.mvc.test.QueryUtils.getStringForHeadersAndGet;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithQueryParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.cookieParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.formData;
import static com.truncon.javalin.mvc.test.RouteBuilder.headerParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

final class NamedTest {
    @Test
    void testFromQuery() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.FROM_QUERY_STRING_ROUTE;
            String route = buildRouteWithQueryParams(baseRoute, queryParams(
                param("value", "hello")
            ));
            String actual = getStringForGet(route);
            Assertions.assertEquals("hello", actual);
        });
    }

    @Test
    void testNamedQuery() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.NAMED_QUERY_STRING_ROUTE;
            String route = buildRouteWithQueryParams(baseRoute, queryParams(
                param("value", "hello")
            ));
            String actual = getStringForGet(route);
            Assertions.assertEquals("hello", actual);
        });
    }

    @Test
    void testFromPath() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.FROM_PATH_ROUTE;
            String route = buildRouteWithPathParams(baseRoute, pathParams(
                param("value", "hello")
            ));
            String actual = getStringForGet(route);
            Assertions.assertEquals("hello", actual);
        });
    }

    @Test
    void testNamedPath() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.NAMED_PATH_ROUTE;
            String route = buildRouteWithPathParams(baseRoute, pathParams(
                param("value", "hello")
            ));
            String actual = getStringForGet(route);
            Assertions.assertEquals("hello", actual);
        });
    }

    @Test
    void testFromHeader() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.FROM_HEADER_ROUTE;
            String route = buildRoute(baseRoute);
            String actual = getStringForHeadersAndGet(route, headerParams(
                param("value", "hello")
            ));
            Assertions.assertEquals("hello", actual);
        });
    }

    @Test
    void testNamedHeader() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.NAMED_HEADER_ROUTE;
            String route = buildRoute(baseRoute);
            String actual = getStringForHeadersAndGet(route, headerParams(
                param("value", "hello")
            ));
            Assertions.assertEquals("hello", actual);
        });
    }

    @Test
    void testFromCookie() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.FROM_COOKIE_ROUTE;
            String route = buildRoute(baseRoute);
            String actual = getStringForCookiesAndGet(route, cookieParams(
                param("value", "hello")
            ));
            Assertions.assertEquals("hello", actual);
        });
    }

    @Test
    void testNamedCookie() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.NAMED_COOKIE_ROUTE;
            String route = buildRoute(baseRoute);
            String actual = getStringForCookiesAndGet(route, cookieParams(
                param("value", "hello")
            ));
            Assertions.assertEquals("hello", actual);
        });
    }

    @Test
    void testFromForm() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.FROM_FORM_ROUTE;
            String route = buildRoute(baseRoute);
            String actual = getStringForFormDataAndPost(route, formData(
                param("value", "hello")
            ));
            Assertions.assertEquals("hello", actual);
        });
    }

    @Test
    void testNamedForm() {
        AsyncTestUtils.runTest(app -> {
            String baseRoute = NamedController.PREFIX + "/" + NamedController.NAMED_FORM_ROUTE;
            String route = buildRoute(baseRoute);
            String actual = getStringForFormDataAndPost(route, formData(
                param("value", "hello")
            ));
            Assertions.assertEquals("hello", actual);
        });
    }
}
