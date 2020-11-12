package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsRequest;
import io.javalin.websocket.WsContext;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
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
        return copy(context.queryParamMap());
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
