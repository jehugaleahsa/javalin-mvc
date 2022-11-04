package com.truncon.javalin.mvc.test.handlers;

import com.truncon.javalin.mvc.api.ws.WsAfterActionContext;
import com.truncon.javalin.mvc.api.ws.WsAfterActionHandler;
import com.truncon.javalin.mvc.api.ws.WsBeforeActionContext;
import com.truncon.javalin.mvc.api.ws.WsBeforeActionHandler;
import com.truncon.javalin.mvc.test.utils.Dependency;

import javax.inject.Inject;

public final class WsInjectionHandler implements WsBeforeActionHandler, WsAfterActionHandler {
    private final Dependency dependency;

    @Inject
    public WsInjectionHandler(Dependency dependency) {
        this.dependency = dependency;
    }

    @Override
    public void executeBefore(WsBeforeActionContext context) {
        context.getWsContext().getResponse().sendText(dependency.getValue());
        context.setCancelled(true);
    }

    @Override
    public void executeAfter(WsAfterActionContext context) {
        context.getWsContext().getResponse().sendText(dependency.getValue());
        context.setHandled(true);
    }
}
