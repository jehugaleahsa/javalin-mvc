package com.truncon.javalin.mvc.annotations.processing;

public final class StringUtils {
    private StringUtils() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.isEmpty() || value.isBlank();
    }

    public static boolean startsWith(String value, String prefix) {
        return value != null && prefix != null && value.startsWith(prefix);
    }

    public static String removeStart(String value, String prefix) {
        if (startsWith(value, prefix)) {
            return value.substring(prefix.length());
        } else {
            return value;
        }
    }

    public static boolean endsWith(String value, String suffix) {
        return value != null && suffix != null && value.endsWith(suffix);
    }

    public static String removeEnd(String value, String suffix) {
        if (endsWith(value, suffix)) {
            return value.substring(0, value.length() - suffix.length());
        } else {
            return value;
        }
    }

    public static String uncapitalize(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        char first = value.charAt(0);
        char firstUpper = Character.toLowerCase(first);
        return firstUpper + value.substring(1);
    }

    public static String stripToNull(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        String stripped = value.strip();
        return stripped.isEmpty() ? null : stripped;
    }
}
