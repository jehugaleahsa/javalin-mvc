package com.truncon.javalin.mvc.test;

import io.javalin.plugin.json.JavalinJackson;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class QueryUtils {
    private QueryUtils() {
    }

    public static String getJsonString(Object value) {
        return JavalinJackson.INSTANCE.toJson(value);
    }

    public static <T> T parseJson(String json, Class<T> clz) {
        return JavalinJackson.INSTANCE.fromJson(json, clz);
    }

    public static String getGetStringResponse(String route) throws IOException {
        return getGetStringResponseWithHeaders(route, Collections.emptyList());
    }

    public static String getGetStringResponseWithHeaders(String route, Collection<Pair<String, String>> headers) throws IOException {
        Request request = Request.Get(route);
        for (Pair<String, String> header : headers) {
            request = request.addHeader(header.getKey(), header.getValue());
        }
        return request.execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
    }

    public static String getStringResponseWithCookies(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringResponseWithCookiesForMethod(Request::Get, route, cookies);
    }

    public static String postStringResponseWithCookies(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringResponseWithCookiesForMethod(Request::Post, route, cookies);
    }

    public static String putStringResponseWithCookies(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringResponseWithCookiesForMethod(Request::Put, route, cookies);
    }

    public static String patchStringResponseWithCookies(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringResponseWithCookiesForMethod(Request::Patch, route, cookies);
    }

    public static String deleteStringResponseWithCookies(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringResponseWithCookiesForMethod(Request::Delete, route, cookies);
    }

    public static String headStringResponseWithCookies(String route, Collection<Pair<String, String>> cookies) throws IOException {
        // HEAD is different in that the body will always be blank. It seems this is either
        // enforced by the Jetty server or is the Apache HTTP library. Rather than fight it,
        // we just implement HEAD to return the response as a header.
        Request request = Request.Head(route);
        String cookieString = cookies.stream()
            .map(p -> p.getLeft() + "=" + p.getRight())
            .collect(Collectors.joining(";"));
        request = request.addHeader("Cookie", cookieString);
        Response response = request.execute();
        HttpResponse rawResponse = response.returnResponse();
        Header header = rawResponse.getFirstHeader("result");
        return header.getValue();
    }

    public static String optionsStringResponseWithCookies(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringResponseWithCookiesForMethod(Request::Options, route, cookies);
    }

    private static String getStringResponseWithCookiesForMethod(
            Function<String, Request> requestGetter,
            String route,
            Collection<Pair<String, String>> cookies) throws IOException {
        Request request = requestGetter.apply(route);
        String cookieString = cookies.stream()
            .map(p -> p.getLeft() + "=" + p.getRight())
            .collect(Collectors.joining(";"));
        request = request.addHeader("Cookie", cookieString);
        Response response = request.execute();
        Content content = response.returnContent();
        return content.asString(StandardCharsets.UTF_8);
    }

    public static String getGetStringResponseWithFormData(String route, Collection<Pair<String, String>> formData) throws IOException {
        HttpGetWithEntity request = new HttpGetWithEntity(route);
        List<NameValuePair> pairs = formData.stream()
            .map(p -> new BasicNameValuePair(p.getLeft(), p.getRight()))
            .collect(Collectors.toList());
        request.setEntity(new UrlEncodedFormEntity(pairs));
        try (CloseableHttpClient client = HttpClientBuilder.create().build();
                CloseableHttpResponse response = client.execute(request)) {
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
    }

    public static <T> T getGetJsonResponse(String route, Class<T> clz) throws Exception {
        String json = Request.Get(route)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
        return parseJson(json, clz);
    }

    public static <T> T getPostJsonResponse(String route, Object body, Class<T> clz) throws Exception {
        String json = Request.Post(route)
            .bodyString(getJsonString(body), ContentType.APPLICATION_JSON)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
        return parseJson(json, clz);
    }

    // This class makes it possible to perform a GET request with a body.
    // This is not a normally supported scenario, but who's to say people
    // won't try to do this?
    private static class HttpGetWithEntity extends HttpPost {
        public HttpGetWithEntity(String uri) {
            super(uri);
        }

        @Override
        public String getMethod() {
            return "GET";
        }
    }

    // This class makes it possible to perform a HEAD request with a body.
    // This is not a normally supported scenario, but who's to say people
    // won't try to do this?
    private static class HttpHeadWithEntity extends HttpPost {
        public HttpHeadWithEntity(String uri) {
            super(uri);
        }

        @Override
        public String getMethod() {
            return "HEAD";
        }
    }
}
