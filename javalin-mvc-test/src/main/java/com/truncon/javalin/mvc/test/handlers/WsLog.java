package com.truncon.javalin.mvc.test.handlers;

import com.truncon.javalin.mvc.api.ws.WsAfterActionHandler;
import com.truncon.javalin.mvc.api.ws.WsBeforeActionHandler;
import com.truncon.javalin.mvc.api.ws.WsContext;

public final class WsLog implements WsBeforeActionHandler, WsAfterActionHandler {
    @Override
    public boolean executeBefore(WsContext context, String[] arguments) {
        System.out.println("Before: " + String.join(",", arguments));
        return true;
    }

    @Override
    public Exception executeAfter(WsContext context, String[] arguments, Exception exception) {
        System.out.println("After: " + String.join(", ", arguments));
        if (exception != null) {
            System.err.println(exception.toString());
        }
        return exception;
    }
}
