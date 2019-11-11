package io.javalin.mvc.ws;

import io.javalin.mvc.api.ws.WsRequest;
import io.javalin.websocket.WsContext;

import java.util.ArrayList;
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
        return context.queryParamMap();
    }

    @Override
    public boolean hasHeader(String name) {
        return context.headerMap().containsKey(name);
    }

    @Override
    public String getHeader(String name) {
        return context.header(name);
    }

    @Override
    public Map<String, List<String>> getHeaderLookup() {
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
    public Map<String, List<String>> getCookieLookup() {
        return explode(context.cookieMap());
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
