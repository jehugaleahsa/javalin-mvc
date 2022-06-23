package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;

public final class InjectionResult {
    private final CodeBlock instanceCall;
    private final boolean injectorNeeded;

    InjectionResult(CodeBlock instanceCall, boolean injectorNeeded) {
        this.instanceCall = instanceCall;
        this.injectorNeeded = injectorNeeded;
    }

    public CodeBlock getInstanceCall() {
        return instanceCall;
    }

    public boolean isInjectorNeeded() {
        return injectorNeeded;
    }
}
