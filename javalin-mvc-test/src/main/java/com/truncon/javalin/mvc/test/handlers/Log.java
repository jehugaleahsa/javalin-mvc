package com.truncon.javalin.mvc.test.handlers;

import com.truncon.javalin.mvc.api.AfterActionHandler;
import com.truncon.javalin.mvc.api.BeforeActionHandler;
import com.truncon.javalin.mvc.api.HttpContext;

public final class Log implements BeforeActionHandler, AfterActionHandler {
    @Override
    public boolean executeBefore(HttpContext context, String[] arguments) {
        System.out.println("Before: " + String.join(",", arguments));
        return true;
    }

    @Override
    public Exception executeAfter(HttpContext context, String[] arguments, Exception exception) {
        System.out.println("After: " + String.join(", ", arguments));
        if (exception != null) {
            System.err.println(exception.toString());
        }
        return exception;
    }
}
