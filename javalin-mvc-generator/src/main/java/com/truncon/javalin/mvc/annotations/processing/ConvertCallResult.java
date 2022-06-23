package com.truncon.javalin.mvc.annotations.processing;

public final class ConvertCallResult {
    private final String call;
    private final boolean injectorNeeded;

    ConvertCallResult(String call, boolean injectorNeeded) {
        this.call = call;
        this.injectorNeeded = injectorNeeded;
    }

    public String getCall() {
        return call;
    }

    public boolean isInjectorNeeded() {
        return injectorNeeded;
    }
}
