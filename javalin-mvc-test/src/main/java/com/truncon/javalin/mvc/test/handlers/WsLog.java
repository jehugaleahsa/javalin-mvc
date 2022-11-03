package com.truncon.javalin.mvc.test.handlers;

import com.truncon.javalin.mvc.api.ws.WsAfterActionContext;
import com.truncon.javalin.mvc.api.ws.WsAfterActionHandler;
import com.truncon.javalin.mvc.api.ws.WsBeforeActionContext;
import com.truncon.javalin.mvc.api.ws.WsBeforeActionHandler;

public final class WsLog implements WsBeforeActionHandler, WsAfterActionHandler {
    @Override
    public void executeBefore(WsBeforeActionContext context) {
        System.out.println("Before: " + String.join(",", context.getArguments()));
    }

    @Override
    public void executeAfter(WsAfterActionContext context) {
        System.out.println("After: " + String.join(", ", context.getArguments()));
        if (context.getException() != null) {
            System.err.println(context.getException().toString());
        }
    }
}
