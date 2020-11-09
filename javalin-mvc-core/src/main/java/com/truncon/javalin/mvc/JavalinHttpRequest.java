package com.truncon.javalin.mvc;

import io.javalin.http.Context;

import io.javalin.http.UploadedFile;
import com.truncon.javalin.mvc.api.FileUpload;
import com.truncon.javalin.mvc.api.HttpRequest;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class JavalinHttpRequest implements HttpRequest {
    private final Context context;

    public JavalinHttpRequest(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public boolean hasPathParameter(String name) {
        return context.pathParamMap().containsKey(name);
    }

    public String getPathParameter(String name) {
        return context.pathParam(name);
    }

    public Map<String, Collection<String>> getPathLookup() {
        return explode(context.pathParamMap());
    }

    public boolean hasQueryParameter(String name) {
        return context.queryParamMap().containsKey(name);
    }

    public String getQueryParameter(String name) {
        return context.queryParam(name);
    }

    public Map<String, Collection<String>> getQueryLookup() {
        return copy(context.queryParamMap());
    }

    public boolean hasFormParameter(String name) {
        return context.formParamMap().containsKey(name);
    }

    public String getFormValue(String name) {
        return context.formParam(name);
    }

    public Map<String, Collection<String>> getFormLookup() {
        return copy(context.formParamMap());
    }

    public boolean hasHeader(String name) {
        return context.headerMap().containsKey(name);
    }

    public String getHeaderValue(String name) {
        return context.header(name);
    }

    public Map<String, Collection<String>> getHeaderLookup() {
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

    public boolean hasCookie(String name) {
        return context.cookieMap().containsKey(name);
    }

    public String getCookieValue(String name) {
        return context.cookie(name);
    }

    public Map<String, Collection<String>> getCookieLookup() {
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

    private static Map<String, Collection<String>> explode(Map<String, String> map) {
        return map.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getKey, e -> listOf(e.getValue())));
    }

    private static List<String> listOf(String item) {
        List<String> list = new ArrayList<>();
        list.add(StringUtils.trimToNull(item));
        return list;
    }

    private static Map<String, Collection<String>> copy(Map<String, List<String>> map) {
        Map<String, Collection<String>> copy = new LinkedHashMap<>(map.size());
        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            Collection<String> values = copy.computeIfAbsent(entry.getKey(), k -> new ArrayList<>(entry.getValue().size()));
            for (String value : entry.getValue()) {
                values.add(value.isEmpty() ? null : value);
            }
        }
        return copy;
    }
}
