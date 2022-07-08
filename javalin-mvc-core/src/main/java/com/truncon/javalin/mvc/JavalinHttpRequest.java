package com.truncon.javalin.mvc;

import io.javalin.http.Context;

import io.javalin.http.UploadedFile;
import com.truncon.javalin.mvc.api.FileUpload;
import com.truncon.javalin.mvc.api.HttpRequest;

import java.io.InputStream;
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
    public boolean hasPathValue(String name) {
        return context.pathParamMap().containsKey(name);
    }

    @Override
    public String getPathValue(String name) {
        if (context.pathParamMap().containsKey(name)) {
            return ValueSourceUtils.emptyToNull(context.pathParam(name));
        } else {
            return null;
        }
    }

    @Override
    public Map<String, List<String>> getPathLookup() {
        return ValueSourceUtils.explode(context.pathParamMap());
    }

    @Override
    public boolean hasQueryValue(String name) {
        return context.queryParamMap().containsKey(name);
    }

    @Override
    public String getQueryValue(String name) {
        return ValueSourceUtils.emptyToNull(context.queryParam(name));
    }

    @Override
    public List<String> getQueryValues(String name) {
        return ValueSourceUtils.copy(context.queryParams(name));
    }

    @Override
    public Map<String, List<String>> getQueryLookup() {
        return ValueSourceUtils.copy(context.queryParamMap());
    }

    @Override
    public boolean hasFormValue(String name) {
        return context.formParamMap().containsKey(name);
    }

    @Override
    public String getFormValue(String name) {
        return ValueSourceUtils.emptyToNull(context.formParam(name));
    }

    @Override
    public List<String> getFormValues(String name) {
        return ValueSourceUtils.copy(context.formParams(name));
    }

    @Override
    public Map<String, List<String>> getFormLookup() {
        return ValueSourceUtils.copy(context.formParamMap());
    }

    @Override
    public boolean hasHeaderValue(String name) {
        return context.headerMap().containsKey(name);
    }

    @Override
    public String getHeaderValue(String name) {
        return ValueSourceUtils.emptyToNull(context.header(name));
    }

    @Override
    public Map<String, List<String>> getHeaderLookup() {
        return ValueSourceUtils.explode(context.headerMap());
    }

    @Override
    public boolean hasCookieValue(String name) {
        return context.cookieMap().containsKey(name);
    }

    @Override
    public String getCookieValue(String name) {
        return ValueSourceUtils.emptyToNull(context.cookie(name));
    }

    @Override
    public Map<String, List<String>> getCookieLookup() {
        return ValueSourceUtils.explode(context.cookieMap());
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
    public <T> T getBodyFromJsonStream(Class<T> bodyCls) {
        return context.bodyStreamAsClass(bodyCls);
    }

    @Override
    public InputStream getBodyAsInputStream() {
        return context.bodyAsInputStream();
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
}
