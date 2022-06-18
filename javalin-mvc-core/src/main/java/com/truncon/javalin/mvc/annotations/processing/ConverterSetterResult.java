package com.truncon.javalin.mvc.annotations.processing;

public final class ConverterSetterResult {
    private final boolean called;
    private final boolean injectorNeeded;

    ConverterSetterResult(boolean called, boolean injectorNeeded) {
        this.called = called;
        this.injectorNeeded = injectorNeeded;
    }

    public boolean isCalled() {
        return called;
    }

    public boolean isInjectorNeeded() {
        return injectorNeeded;
    }
}
