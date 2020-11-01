package com.truncon.javalin.mvc.test;

import io.javalin.plugin.json.JavalinJackson;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    public static String getGetStringResponseWithCookies(String route, Collection<Pair<String, String>> cookies) throws IOException {
        Request request = Request.Get(route);
        String cookieString = cookies.stream()
            .map(p -> p.getLeft() + "=" + p.getRight())
            .collect(Collectors.joining(";"));
        request = request.addHeader("Cookie", cookieString);
        return request.execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
    }

    public static String getGetStringResponseWithFormData(String route, Collection<Pair<String, String>> formData) throws IOException {
        HttpGetWithEntity request = new HttpGetWithEntity(route);
        List<NameValuePair> pairs = formData.stream()
            .map(p -> new BasicNameValuePair(p.getLeft(), p.getRight()))
            .collect(Collectors.toList());
        request.setEntity(new UrlEncodedFormEntity(pairs));
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
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
}
