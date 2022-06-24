package com.truncon.javalin.mvc.test.handlers;

import com.truncon.javalin.mvc.api.AfterActionHandler;
import com.truncon.javalin.mvc.api.BeforeActionHandler;
import com.truncon.javalin.mvc.api.HttpContext;
import com.truncon.javalin.mvc.test.utils.Dependency;

import javax.inject.Inject;

public final class InjectionHandler implements BeforeActionHandler, AfterActionHandler {
    private final Dependency dependency;

    @Inject
    public InjectionHandler(Dependency dependency) {
        this.dependency = dependency;
    }

    @Override
    public boolean executeBefore(HttpContext context, String[] arguments) {
        context.getResponse().setTextBody(dependency.getValue());
        return false;
    }

    @Override
    public Exception executeAfter(HttpContext context, String[] arguments, Exception exception) {
        context.getResponse().setTextBody(dependency.getValue());
        return null;
    }
}
