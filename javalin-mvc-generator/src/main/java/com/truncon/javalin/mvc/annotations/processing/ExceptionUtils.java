package com.truncon.javalin.mvc.annotations.processing;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtils {
    private ExceptionUtils() {
    }

    public static String getMessage(Throwable throwable) {
        return throwable == null ? null : throwable.getMessage();
    }

    public static String getStackTrace(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        StringWriter writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer, true);
        throwable.printStackTrace(printWriter);
        return writer.toString();
    }
}
