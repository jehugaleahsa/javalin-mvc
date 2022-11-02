package com.truncon.javalin.mvc;

import com.truncon.javalin.mvc.api.AfterActionContext;
import com.truncon.javalin.mvc.api.HttpContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class JavalinAfterActionContext implements AfterActionContext {
    private final HttpContext context;
    private final List<String> arguments;
    private final Exception exception;
    private boolean handled;

    public JavalinAfterActionContext(HttpContext context, String[] arguments, Exception exception, boolean handled) {
        this.context = context;
        this.arguments = Collections.unmodifiableList(Arrays.asList(arguments));
        this.exception = exception;
        this.handled = handled;
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
    public Exception getException() {
        return exception;
    }

    @Override
    public boolean isHandled() {
        return handled;
    }

    @Override
    public void setHandled(boolean handled) {
        this.handled = this.exception != null && (this.handled || handled);
    }
}
