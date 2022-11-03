package com.truncon.javalin.mvc.ws;

import com.truncon.javalin.mvc.api.ws.WsBeforeActionContext;
import com.truncon.javalin.mvc.api.ws.WsContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class JavalinWsBeforeActionContext implements WsBeforeActionContext {
    private final WsContext context;
    private final List<String> arguments;
    private boolean cancelled;

    public JavalinWsBeforeActionContext(WsContext context, String[] arguments) {
        this.context = context;
        this.arguments = Collections.unmodifiableList(Arrays.asList(arguments));
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
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
