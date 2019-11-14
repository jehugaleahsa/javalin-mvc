package com.truncon.javalin.mvc.test.handlers;

import com.truncon.javalin.mvc.api.AfterActionHandler;
import com.truncon.javalin.mvc.api.HttpContext;

public final class ErrorHandler implements AfterActionHandler {
    @Override
    public Exception executeAfter(HttpContext context, String[] arguments, Exception exception) {
        if (exception != null) {
            context.getResponse().setStatusCode(500).setTextBody("An unexpected error has occurred.");
        }
        return null;
    }
}
