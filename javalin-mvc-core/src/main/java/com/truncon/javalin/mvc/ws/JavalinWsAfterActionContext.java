package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsAfterActionContext;
import com.truncon.javalin.mvc.api.ws.WsContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class JavalinWsAfterActionContext implements WsAfterActionContext {
    private final WsContext context;
    private final List<String> arguments;
    private Exception exception;
    private boolean handled;

    public JavalinWsAfterActionContext(WsContext context, String[] arguments, Exception exception, boolean handled) {
        this.context = context;
        this.arguments = Collections.unmodifiableList(Arrays.asList(arguments));
        this.exception = exception;
        this.handled = handled;
    }

    @Override
    public WsContext getWsContext() {
        return context;
    }

    @Override
    public List<String> getArguments() {
        return arguments;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public void setException(Exception exception) {
        this.exception = exception;
    }

    @Override
    public boolean isHandled() {
        return exception == null || handled;
    }

    @Override
    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}
