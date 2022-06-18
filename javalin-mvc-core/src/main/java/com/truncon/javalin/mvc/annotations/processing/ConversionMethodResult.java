package com.truncon.javalin.mvc.annotations.processing;

public final class ConversionMethodResult {
    private final String method;
    private final boolean injectorNeeded;

    ConversionMethodResult(String method, boolean injectorNeeded) {
        this.method = method;
        this.injectorNeeded = injectorNeeded;
    }

    public String getMethod() {
        return method;
    }

    public boolean isInjectorNeeded() {
        return injectorNeeded;
    }
}
