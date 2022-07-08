package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.LookupUtils;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import io.javalin.websocket.WsContext;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class JavalinWsRequest implements WsRequest {
    private final WsContext context;

    public JavalinWsRequest(WsContext context) {
        this.context = context;
    }

    @Override
    public boolean hasPathValue(String name) {
        return context.pathParamMap().containsKey(name);
    }

    @Override
    public String getPathValue(String name) {
        return context.pathParam(name);
    }

    @Override
    public Map<String, List<String>> getPathLookup() {
        return LookupUtils.explode(context.pathParamMap());
    }

    @Override
    public boolean hasQueryValue(String name) {
        return context.queryParamMap().containsKey(name);
    }

    @Override
    public String getQueryValue(String name) {
        return context.queryParam(name);
    }

    @Override
    public List<String> getQueryValues(String name) {
        return Collections.unmodifiableList(context.queryParams(name));
    }

    @Override
    public Map<String, List<String>> getQueryLookup() {
        return LookupUtils.copy(context.queryParamMap());
    }

    @Override
    public boolean hasHeaderValue(String name) {
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
    public boolean hasCookieValue(String name) {
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
}
