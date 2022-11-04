package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.test.controllers.VariousSourcesController;
import com.truncon.javalin.mvc.test.models.VariousSourcesModel;
import org.apache.http.client.fluent.Request;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.truncon.javalin.mvc.test.RouteBuilder.appendQueryString;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRoute;
import static com.truncon.javalin.mvc.test.RouteBuilder.buildRouteWithPathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.param;
import static com.truncon.javalin.mvc.test.RouteBuilder.pathParams;
import static com.truncon.javalin.mvc.test.RouteBuilder.queryParams;

public final class VariousSourcesTest {
    @Test
    void testVariousParameterSources_builtin_allSourcesBound() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithPathParams(VariousSourcesController.VARIOUS_SOURCES_BUILTIN_ROUTE, pathParams(
                param("value", "path")
            ));
            route = appendQueryString(route, queryParams(param("value", "query")));
            String response = Request.Post(route)
                .addHeader("value", "header")
                .addHeader("Cookie", "value=cookie")
                .bodyForm(new BasicNameValuePair("value", "form"))
                .execute()
                .returnContent()
                .asString();
            VariousSourcesModel model = QueryUtils.MAPPER.readValue(response, VariousSourcesModel.class);
            Assertions.assertEquals("path", model.getPath());
            Assertions.assertEquals("query", model.getQuery());
            Assertions.assertEquals("header", model.getHeader());
            Assertions.assertEquals("cookie", model.getCookie());
            Assertions.assertEquals("form", model.getForm());
        });
    }

    @Test
    void testVariousParameterSources_standard_allSourcesBound() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRouteWithPathParams(VariousSourcesController.VARIOUS_SOURCES_STANDARD_ROUTE, pathParams(
                param("value", "path")
            ));
            route = appendQueryString(route, queryParams(param("value", "query")));
            String response = Request.Post(route)
                .addHeader("value", "header")
                .addHeader("Cookie", "value=cookie")
                .bodyForm(new BasicNameValuePair("value", "form"))
                .execute()
                .returnContent()
                .asString();
            VariousSourcesModel model = QueryUtils.MAPPER.readValue(response, VariousSourcesModel.class);
            Assertions.assertEquals("path", model.getPath());
            Assertions.assertEquals("query", model.getQuery());
            Assertions.assertEquals("header", model.getHeader());
            Assertions.assertEquals("cookie", model.getCookie());
            Assertions.assertEquals("form", model.getForm());
        });
    }

    @Test
    void testVariousParameterSources_builtin_withDefaults_allSourcesBound() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(VariousSourcesController.VARIOUS_SOURCES_BUILTIN_DEFAULTS_ROUTE);
            String response = Request.Post(route)
                .execute()
                .returnContent()
                .asString();
            VariousSourcesModel model = QueryUtils.MAPPER.readValue(response, VariousSourcesModel.class);
            Assertions.assertEquals("path", model.getPath());
            Assertions.assertEquals("query", model.getQuery());
            Assertions.assertEquals("header", model.getHeader());
            Assertions.assertEquals("cookie", model.getCookie());
            Assertions.assertEquals("form", model.getForm());
            Assertions.assertEquals("any", model.getAny());
        });
    }

    @Test
    void testVariousParameterSources_standard_withDefaults_allSourcesBound() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(VariousSourcesController.VARIOUS_SOURCES_STANDARD_DEFAULTS_ROUTE);
            String response = Request.Post(route)
                .execute()
                .returnContent()
                .asString();
            VariousSourcesModel model = QueryUtils.MAPPER.readValue(response, VariousSourcesModel.class);
            Assertions.assertEquals("path", model.getPath());
            Assertions.assertEquals("query", model.getQuery());
            Assertions.assertEquals("header", model.getHeader());
            Assertions.assertEquals("cookie", model.getCookie());
            Assertions.assertEquals("form", model.getForm());
            Assertions.assertEquals("any", model.getAny());
        });
    }

    @Test
    void testVariousParameterSources_builtin_collections_withDefaults_allSourcesBound() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(VariousSourcesController.VARIOUS_SOURCES_COLLECTIONS_BUILTIN_DEFAULTS_ROUTE);
            String response = Request.Post(route)
                .execute()
                .returnContent()
                .asString();
            VariousSourcesModel model = QueryUtils.MAPPER.readValue(response, VariousSourcesModel.class);
            Assertions.assertEquals("path", model.getPath());
            Assertions.assertEquals("query", model.getQuery());
            Assertions.assertEquals("header", model.getHeader());
            Assertions.assertEquals("cookie", model.getCookie());
            Assertions.assertEquals("form", model.getForm());
            Assertions.assertEquals("any", model.getAny());
        });
    }

    @Test
    void testVariousParameterSources_standard_collections_withDefaults_allSourcesBound() {
        AsyncTestUtils.runTest(app -> {
            String route = buildRoute(VariousSourcesController.VARIOUS_SOURCES_COLLECTIONS_STANDARD_DEFAULTS_ROUTE);
            String response = Request.Post(route)
                .execute()
                .returnContent()
                .asString();
            VariousSourcesModel model = QueryUtils.MAPPER.readValue(response, VariousSourcesModel.class);
            Assertions.assertEquals("path", model.getPath());
            Assertions.assertEquals("query", model.getQuery());
            Assertions.assertEquals("header", model.getHeader());
            Assertions.assertEquals("cookie", model.getCookie());
            Assertions.assertEquals("form", model.getForm());
            Assertions.assertEquals("any", model.getAny());
        });
    }
}
