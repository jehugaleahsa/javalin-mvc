package com.truncon.javalin.mvc;

public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.isEmpty() || value.trim().isEmpty();
    }

    public static String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public static String removeStart(String value, String start) {
        if (value == null || start == null) {
            return value;
        }
        if (value.startsWith(start)) {
            return value.substring(start.length());
        } else {
            return value;
        }
    }

    public static String uncapitalize(String value) {
        if (value == null) {
            return null;
        }
        char first = value.charAt(0);
        if (Character.isUpperCase(first)) {
            char lower = Character.toLowerCase(first);
            return lower + value.substring(1);
        } else {
            return value;
        }
    }
}
