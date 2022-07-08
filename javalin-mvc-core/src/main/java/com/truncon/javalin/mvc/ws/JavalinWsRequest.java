package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.ValueSourceUtils;
import com.truncon.javalin.mvc.api.ws.WsRequest;
import io.javalin.websocket.WsContext;

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
}
