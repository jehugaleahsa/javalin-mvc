package com.truncon.javalin.mvc;

import io.javalin.http.Context;

import io.javalin.http.UploadedFile;
import com.truncon.javalin.mvc.api.FileUpload;
import com.truncon.javalin.mvc.api.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class JavalinHttpRequest implements HttpRequest {
    private final Context context;

    public JavalinHttpRequest(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    @Override
    public boolean hasPathParameter(String name) {
        return context.pathParamMap().containsKey(name);
    }

    @Override
    public String getPathParameter(String name) {
        return context.pathParam(name);
    }

    @Override
    public Map<String, List<String>> getPathLookup() {
        return explode(context.pathParamMap());
    }

    @Override
    public boolean hasQueryParameter(String name) {
        return context.queryParamMap().containsKey(name);
    }

    @Override
    public String getQueryParameter(String name) {
        return context.queryParam(name);
    }

    @Override
    public Map<String, List<String>> getQueryLookup() {
        return copy(context.queryParamMap());
    }

    @Override
    public boolean hasFormParameter(String name) {
        return context.formParamMap().containsKey(name);
    }

    @Override
    public String getFormValue(String name) {
        return context.formParam(name);
    }

    @Override
    public Map<String, List<String>> getFormLookup() {
        return copy(context.formParamMap());
    }

    @Override
    public boolean hasHeader(String name) {
        return context.headerMap().containsKey(name);
    }

    @Override
    public String getHeaderValue(String name) {
        return context.header(name);
    }

    @Override
    public Map<String, List<String>> getHeaderLookup() {
        return explode(context.headerMap());
    }

    @Override
    public int getContentLength() {
        return context.contentLength();
    }

    @Override
    public String getContentType() {
        return context.contentType();
    }

    @Override
    public byte[] getBodyAsBytes() {
        return context.bodyAsBytes();
    }

    @Override
    public String getBodyAsText() {
        return context.body();
    }

    @Override
    public <T> T getBodyFromJson(Class<T> bodyCls) {
        return context.bodyAsClass(bodyCls);
    }

    @Override
    public String getUrl() {
        return context.url();
    }

    @Override
    public String getHttpVersion() {
        return context.protocol();
    }

    @Override
    public String getMethod() {
        return context.method();
    }

    @Override
    public String getUserAgent() {
        return context.userAgent();
    }

    @Override
    public boolean hasCookie(String name) {
        return context.cookieMap().containsKey(name);
    }

    @Override
    public String getCookieValue(String name) {
        return context.cookie(name);
    }

    @Override
    public Map<String, List<String>> getCookieLookup() {
        return explode(context.cookieMap());
    }

    @Override
    public boolean isMultipart() {
        return context.isMultipart();
    }

    @Override
    public FileUpload getFile(String name) {
        UploadedFile file = context.uploadedFile(name);
        if (file == null) {
            return null;
        }
        return new FileUpload(file.getContent(), file.getContentType(), file.getFilename());
    }

    private static Map<String, List<String>> explode(Map<String, String> map) {
        Map<String, List<String>> exploded = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {
            List<String> values = listOf(entry.getValue());
            exploded.put(entry.getKey(), values);
        }
        return exploded;
    }

    private static List<String> listOf(String item) {
        List<String> list = new ArrayList<>();
        list.add(StringUtils.trimToNull(item));
        return list;
    }

    private static Map<String, List<String>> copy(Map<String, List<String>> map) {
        Map<String, List<String>> copy = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            List<String> values = copy.computeIfAbsent(entry.getKey(), k -> new ArrayList<>(entry.getValue().size()));
            for (String value : entry.getValue()) {
                values.add(value.isEmpty() ? null : value);
            }
        }
        return copy;
    }
}
