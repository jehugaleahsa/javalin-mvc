package com.truncon.javalin.mvc.test.handlers;

import com.truncon.javalin.mvc.api.AfterActionContext;
import com.truncon.javalin.mvc.api.AfterActionHandler;
import com.truncon.javalin.mvc.api.BeforeActionContext;
import com.truncon.javalin.mvc.api.BeforeActionHandler;

public final class Log implements BeforeActionHandler, AfterActionHandler {
    @Override
    public void executeBefore(BeforeActionContext context) {
        System.out.println("Before: " + String.join(",", context.getArguments()));
    }

    @Override
    public void executeAfter(AfterActionContext context) {
        System.out.println("After: " + String.join(", ", context.getArguments()));
        if (context.getException() != null) {
            System.err.println(context.getException().toString());
        }
    }
}
