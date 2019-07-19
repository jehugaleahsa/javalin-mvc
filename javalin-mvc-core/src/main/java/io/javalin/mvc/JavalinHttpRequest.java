package io.javalin.mvc;

import io.javalin.http.Context;

import io.javalin.http.UploadedFile;
import io.javalin.mvc.api.FileUpload;
import io.javalin.mvc.api.HttpRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class JavalinHttpRequest implements HttpRequest {
    private final Context context;

    public JavalinHttpRequest(Context context) {
        this.context = context;
    }

    public boolean hasPathParameter(String name) {
        return context.pathParamMap().containsKey(name);
    }

    public String getPathParameter(String name) {
        return context.pathParam(name);
    }

    public Map<String, List<String>> getPathLookup() {
        return explode(context.pathParamMap());
    }

    public boolean hasQueryParameter(String name) {
        return context.queryParamMap().containsKey(name);
    }

    public String getQueryParameter(String name) {
        return context.queryParam(name);
    }

    public Map<String, List<String>> getQueryLookup() {
        return context.queryParamMap();
    }

    public boolean hasFormParameter(String name) {
        return context.formParamMap().containsKey(name);
    }

    public String getFormParameter(String name) {
        return context.formParam(name);
    }

    public Map<String, List<String>> getFormLookup() {
        return context.formParamMap();
    }

    public boolean hasHeader(String name) {
        return context.headerMap().containsKey(name);
    }

    public String getHeader(String name) {
        return context.header(name);
    }

    public Map<String, List<String>> getHeaderLookup() {
        return explode(context.headerMap());
    }

    public int getContentLength() {
        return context.contentLength();
    }

    public String getContentType() {
        return context.contentType();
    }

    public byte[] getBodyAsBytes() {
        return context.bodyAsBytes();
    }

    public String getBodyAsText() {
        return context.body();
    }

    public <T> T getBodyFromJson(Class<T> bodyCls) {
        return context.bodyAsClass(bodyCls);
    }

    public String getUrl() {
        return context.url();
    }

    public String getHttpVersion() {
        return context.protocol();
    }

    public String getMethod() {
        return context.method();
    }

    public String getUserAgent() {
        return context.userAgent();
    }

    public String getCookieValue(String name) {
        return context.cookie(name);
    }

    public Map<String, List<String>> getCookieLookup() {
        return explode(context.cookieMap());
    }

    public boolean isMultipart() {
        return context.isMultipart();
    }

    public FileUpload getFile(String name) {
        UploadedFile file = context.uploadedFile(name);
        if (file == null) {
            return null;
        }
        return new FileUpload(file.getContent(), file.getContentType(), file.getFilename());
    }

    private static Map<String, List<String>> explode(Map<String, String> map) {
        return map.keySet().stream().collect(Collectors.toMap(k -> k, k -> listOf(map.get(k))));
    }

    private static List<String> listOf(String item) {
        List<String> list = new ArrayList<>();
        list.add(item);
        return list;
    }
}
