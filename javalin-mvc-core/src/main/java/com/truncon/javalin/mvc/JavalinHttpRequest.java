package com.truncon.javalin.mvc;

import io.javalin.http.Context;

import io.javalin.http.UploadedFile;
import com.truncon.javalin.mvc.api.FileUpload;
import com.truncon.javalin.mvc.api.HttpRequest;

import java.io.InputStream;
import java.util.Collections;
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
        return LookupUtils.explode(context.pathParamMap());
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
    public List<String> getQueryParameters(String name) {
        return Collections.unmodifiableList(context.queryParams(name));
    }

    @Override
    public Map<String, List<String>> getQueryLookup() {
        return LookupUtils.copy(context.queryParamMap());
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
    public List<String> getFormValues(String name) {
        return Collections.unmodifiableList(context.formParams(name));
    }

    @Override
    public Map<String, List<String>> getFormLookup() {
        return LookupUtils.copy(context.formParamMap());
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
        return LookupUtils.explode(context.headerMap());
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
    public boolean hasCookie(String name) {
        return context.cookieMap().containsKey(name);
    }

    @Override
    public String getCookieValue(String name) {
        return context.cookie(name);
    }

    @Override
    public Map<String, List<String>> getCookieLookup() {
        return LookupUtils.explode(context.cookieMap());
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
