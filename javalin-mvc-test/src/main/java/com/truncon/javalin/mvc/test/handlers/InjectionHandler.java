package com.truncon.javalin.mvc.test.handlers;

import com.truncon.javalin.mvc.api.AfterActionContext;
import com.truncon.javalin.mvc.api.AfterActionHandler;
import com.truncon.javalin.mvc.api.BeforeActionContext;
import com.truncon.javalin.mvc.api.BeforeActionHandler;
import com.truncon.javalin.mvc.test.utils.Dependency;

import javax.inject.Inject;

public final class InjectionHandler implements BeforeActionHandler, AfterActionHandler {
    private final Dependency dependency;

    @Inject
    public InjectionHandler(Dependency dependency) {
        this.dependency = dependency;
    }

    @Override
    public void executeBefore(BeforeActionContext context) {
        context.getHttpContext().getResponse().setTextBody(dependency.getValue());
        context.setCancelled(true);
    }

    @Override
    public void executeAfter(AfterActionContext context) {
        context.getHttpContext().getResponse().setTextBody(dependency.getValue());
        context.setHandled(true);
    }
}
