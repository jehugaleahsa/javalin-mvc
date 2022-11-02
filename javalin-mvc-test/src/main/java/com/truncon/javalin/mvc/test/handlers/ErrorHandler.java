package com.truncon.javalin.mvc.test.handlers;

import com.truncon.javalin.mvc.api.AfterActionContext;
import com.truncon.javalin.mvc.api.AfterActionHandler;

public final class ErrorHandler implements AfterActionHandler {
    @Override
    public void executeAfter(AfterActionContext context) {
        if (context.getException() != null) {
            context.getHttpContext().getResponse()
                .setStatusCode(500)
                .setTextBody("An unexpected error has occurred.");
        }
        context.setHandled(true);
    }
}
