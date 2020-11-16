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
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
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
import java.util.function.Supplier;
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

    public static <T> T getJsonResponseForGet(String route, Class<T> clz) throws Exception {
        String json = Request.Get(route)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
        return parseJson(json, clz);
    }

    public static <T> T getJsonResponseForPost(String route, Object body, Class<T> clz) throws Exception {
        String json = Request.Post(route)
            .bodyString(getJsonString(body), ContentType.APPLICATION_JSON)
            .execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
        return parseJson(json, clz);
    }

    public static DownloadDetails downloadForGet(String route) throws Exception {
        Content content = Request.Get(route)
            .execute()
            .returnContent();
        return new DownloadDetails(content.asBytes(), content.getType().getMimeType());
    }

    public static int getStatusCodeForGet(String route) throws IOException {
        return getStatusCodeForMethod(() -> Request.Get(route));
    }

    public static int getStatusCodeForPost(String route) throws IOException {
        return getStatusCodeForMethod(() -> Request.Post(route));
    }

    public static int getStatusCodeForPut(String route) throws IOException {
        return getStatusCodeForMethod(() -> Request.Put(route));
    }

    private static int getStatusCodeForMethod(Supplier<Request> supplier) throws IOException {
        return supplier.get()
            .execute()
            .returnResponse()
            .getStatusLine()
            .getStatusCode();
    }

    // region URL

    public static String getStringForGet(String route) throws IOException {
        return getStringForHeadersAndGet(route, Collections.emptyList());
    }

    public static String getStringForPost(String route) throws IOException {
        return getStringForHeadersAndPost(route, Collections.emptyList());
    }

    public static String getStringForPut(String route) throws IOException {
        return getStringForHeadersAndPut(route, Collections.emptyList());
    }

    public static String getStringForPatch(String route) throws IOException {
        return getStringForHeadersAndPatch(route, Collections.emptyList());
    }

    public static String getStringForDelete(String route) throws IOException {
        return getStringForHeadersAndDelete(route, Collections.emptyList());
    }

    public static String getStringForHead(String route) throws IOException {
        return getStringForHeadersAndHead(route, Collections.emptyList());
    }

    public static String getStringForOptions(String route) throws IOException {
        return getStringForHeadersAndOptions(route, Collections.emptyList());
    }

    // endregion

    // region Headers

    public static String getStringForHeadersAndGet(String route, Collection<Pair<String, String>> headers) throws IOException {
        return getStringForHeadersForMethod(() -> Request.Get(route), headers);
    }

    public static String getStringForHeadersAndPost(String route, Collection<Pair<String, String>> headers) throws IOException {
        return getStringForHeadersForMethod(() -> Request.Post(route), headers);
    }

    public static String getStringForHeadersAndPut(String route, Collection<Pair<String, String>> headers) throws IOException {
        return getStringForHeadersForMethod(() -> Request.Put(route), headers);
    }

    public static String getStringForHeadersAndPatch(String route, Collection<Pair<String, String>> headers) throws IOException {
        return getStringForHeadersForMethod(() -> Request.Patch(route), headers);
    }

    public static String getStringForHeadersAndDelete(String route, Collection<Pair<String, String>> headers) throws IOException {
        return getStringForHeadersForMethod(() -> Request.Delete(route), headers);
    }

    public static String getStringForHeadersAndHead(String route, Collection<Pair<String, String>> headers) throws IOException {
        // HEAD is different in that the body will always be blank. It seems this is either
        // enforced by the Jetty server or is the Apache HTTP library. Rather than fight it,
        // we just implement HEAD to return the response as a header.
        Request request = Request.Head(route);
        for (Pair<String, String> header : headers) {
            request = request.addHeader(header.getLeft(), header.getRight());
        }
        Response response = request.execute();
        HttpResponse rawResponse = response.returnResponse();
        Header header = rawResponse.getFirstHeader("result");
        return header.getValue();
    }

    public static String getStringForHeadersAndOptions(String route, Collection<Pair<String, String>> headers) throws IOException {
        return getStringForHeadersForMethod(() -> Request.Options(route), headers);
    }

    private static String getStringForHeadersForMethod(
            Supplier<Request> requestGetter,
            Collection<Pair<String, String>> headers) throws IOException {
        Request request = requestGetter.get();
        for (Pair<String, String> header : headers) {
            request = request.addHeader(header.getKey(), header.getValue());
        }
        return request.execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
    }

    // endregion

    // region Cookies

    public static String getStringForCookiesAndGet(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringForCookiesAndMethod(() -> Request.Get(route), cookies);
    }

    public static String getStringForCookiesAndPost(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringForCookiesAndMethod(() -> Request.Post(route), cookies);
    }

    public static String getStringForCookiesAndPut(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringForCookiesAndMethod(() -> Request.Put(route), cookies);
    }

    public static String getStringForCookiesAndPatch(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringForCookiesAndMethod(() -> Request.Patch(route), cookies);
    }

    public static String getStringForCookiesAndDelete(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringForCookiesAndMethod(() -> Request.Delete(route), cookies);
    }

    public static String getStringForCookiesAndHead(String route, Collection<Pair<String, String>> cookies) throws IOException {
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

    public static String getStringForCookiesAndOptions(String route, Collection<Pair<String, String>> cookies) throws IOException {
        return getStringForCookiesAndMethod(() -> Request.Options(route), cookies);
    }

    private static String getStringForCookiesAndMethod(
            Supplier<Request> requestGetter,
            Collection<Pair<String, String>> cookies) throws IOException {
        Request request = requestGetter.get();
        String cookieString = cookies.stream()
            .map(p -> p.getLeft() + "=" + p.getRight())
            .collect(Collectors.joining(";"));
        request = request.addHeader("Cookie", cookieString);
        Response response = request.execute();
        Content content = response.returnContent();
        return content.asString(StandardCharsets.UTF_8);
    }

    // endregion

    // region Form Data

    public static String getStringForFormDataAndGet(String route, Collection<Pair<String, String>> formData) throws IOException {
        return getStringForFormDataAndMethodFake(() -> new HttpGetWithEntity(route), formData);
    }

    public static String getStringForFormDataAndPost(String route, Collection<Pair<String, String>> formData) throws IOException {
        return getStringForFormDataAndMethod(() -> Request.Post(route), formData);
    }

    public static String getStringForFormDataAndPut(String route, Collection<Pair<String, String>> formData) throws IOException {
        return getStringForFormDataAndMethod(() -> Request.Put(route), formData);
    }

    public static String getStringForFormDataAndPatch(String route, Collection<Pair<String, String>> formData) throws IOException {
        return getStringForFormDataAndMethod(() -> Request.Patch(route), formData);
    }

    public static String getStringForFormDataAndDelete(String route, Collection<Pair<String, String>> formData) throws IOException {
        return getStringForFormDataAndMethodFake(() -> new HttpDeleteWithEntity(route), formData);
    }

    public static String getStringForFormDataAndHead(String route, Collection<Pair<String, String>> formData) throws IOException {
        HttpEntityEnclosingRequestBase request = new HttpHeadWithEntity(route);
        List<NameValuePair> pairs = formData.stream()
            .map(p -> new BasicNameValuePair(p.getLeft(), p.getRight()))
            .collect(Collectors.toList());
        request.setEntity(new UrlEncodedFormEntity(pairs));
        try (CloseableHttpClient client = HttpClientBuilder.create().build();
             CloseableHttpResponse response = client.execute(request)) {
            return response.getFirstHeader("result").getValue();
        }
    }

    public static String getStringForFormDataAndOptions(String route, Collection<Pair<String, String>> formData) throws IOException {
        return getStringForFormDataAndMethodFake(() -> new HttpOptionsWithEntity(route), formData);
    }

    private static String getStringForFormDataAndMethod(
            Supplier<Request> requestGetter,
            Collection<Pair<String, String>> formData) throws IOException {
        Request request = requestGetter.get();
        List<NameValuePair> pairs = formData.stream()
            .map(d -> new BasicNameValuePair(d.getLeft(), d.getRight()))
            .collect(Collectors.toList());
        request.bodyForm(pairs);
        return request.execute()
            .returnContent()
            .asString(StandardCharsets.UTF_8);
    }

    private static String getStringForFormDataAndMethodFake(
            Supplier<HttpEntityEnclosingRequestBase> requestGetter,
            Collection<Pair<String, String>> formData) throws IOException {
        HttpEntityEnclosingRequestBase request = requestGetter.get();
        List<NameValuePair> pairs = formData.stream()
            .map(p -> new BasicNameValuePair(p.getLeft(), p.getRight()))
            .collect(Collectors.toList());
        request.setEntity(new UrlEncodedFormEntity(pairs));
        try (CloseableHttpClient client = HttpClientBuilder.create().build();
             CloseableHttpResponse response = client.execute(request)) {
            return EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        }
    }

    // endregion

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

    // This class makes it possible to perform a DELETE request with a body.
    // This is not a normally supported scenario, but who's to say people
    // won't try to do this?
    private static class HttpDeleteWithEntity extends HttpPost {
        public HttpDeleteWithEntity(String uri) {
            super(uri);
        }

        @Override
        public String getMethod() {
            return "DELETE";
        }
    }

    // This class makes it possible to perform a DELETE request with a body.
    // This is not a normally supported scenario, but who's to say people
    // won't try to do this?
    private static class HttpOptionsWithEntity extends HttpPost {
        public HttpOptionsWithEntity(String uri) {
            super(uri);
        }

        @Override
        public String getMethod() {
            return "OPTIONS";
        }
    }
}
