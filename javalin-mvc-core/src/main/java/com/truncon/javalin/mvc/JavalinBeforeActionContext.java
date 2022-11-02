package com.truncon.javalin.mvc;

import com.truncon.javalin.mvc.api.BeforeActionContext;
import com.truncon.javalin.mvc.api.HttpContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class JavalinBeforeActionContext implements BeforeActionContext {
    private final HttpContext context;
    private final List<String> arguments;
    private boolean cancelled;

    public JavalinBeforeActionContext(HttpContext context, String[] arguments) {
        this.context = context;
        this.arguments = Collections.unmodifiableList(Arrays.asList(arguments));
    }

    @Override
    public HttpContext getHttpContext() {
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
