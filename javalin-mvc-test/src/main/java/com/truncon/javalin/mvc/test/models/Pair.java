package com.truncon.javalin.mvc.test.models;

import com.truncon.javalin.mvc.api.Converter;
import com.truncon.javalin.mvc.api.HttpRequest;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ValueSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@UseConverter("pair")
public final class Pair {
    private final int first;
    private final int second;

    public Pair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    @Converter("pair")
    public static Pair parse(HttpRequest request, String name, ValueSource valueSource) {
        Map<String, Collection<String>> lookup = request.getSourceLookup(valueSource);
        Collection<String> values = lookup.get(name);
        return values.size() == 1 ? Pair.parse(values.iterator().next()) : null;
    }

    public static Pair parse(String representation) {
        String stripped = StringUtils.removeStart(representation, "(");
        stripped = StringUtils.removeEnd(stripped, ")");
        String[] parts = StringUtils.split(stripped, ",", 2);
        if (parts == null || parts.length != 2) {
            return null;
        }
        int[] values = Arrays.stream(parts)
            .map(StringUtils::trim)
            .map(NumberUtils::createInteger)
            .filter(Objects::nonNull)
            .mapToInt(v -> v)
            .toArray();
        return values.length != 2 ? null : new Pair(values[0], values[1]);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
