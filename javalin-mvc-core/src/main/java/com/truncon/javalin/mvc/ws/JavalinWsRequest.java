package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsRequest;
import io.javalin.websocket.WsContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class JavalinWsRequest implements WsRequest {
    private final WsContext context;

    public JavalinWsRequest(WsContext context) {
        this.context = context;
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
    public Map<String, Collection<String>> getPathLookup() {
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
    public Map<String, Collection<String>> getQueryLookup() {
        return Collections.unmodifiableMap(context.queryParamMap());
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
    public Map<String, Collection<String>> getHeaderLookup() {
        return explode(context.headerMap());
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
    public Map<String, Collection<String>> getCookieLookup() {
        return explode(context.cookieMap());
    }

    private static Map<String, Collection<String>> explode(Map<String, String> map) {
        return map.keySet().stream().collect(Collectors.toMap(k -> k, k -> listOf(map.get(k))));
    }

    private static List<String> listOf(String item) {
        List<String> list = new ArrayList<>();
        list.add(item);
        return list;
    }
}
