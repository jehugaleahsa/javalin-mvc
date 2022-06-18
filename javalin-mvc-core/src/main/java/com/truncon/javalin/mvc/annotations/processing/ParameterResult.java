package com.truncon.javalin.mvc.annotations.processing;

import java.util.ArrayList;
import java.util.List;

public final class ParameterResult {
    private final List<String> arguments = new ArrayList<>();
    private boolean injectorNeeded;

    ParameterResult() {
    }

    void addArgument(String argument) {
        this.arguments.add(argument);
    }

    public List<String> getArguments() {
        return arguments;
    }

    public String getArgumentList() {
        return String.join(", ", arguments);
    }

    public boolean isInjectorNeeded() {
        return injectorNeeded;
    }

    void markInjectorNeeded(boolean needed) {
        this.injectorNeeded |= needed;
    }
}
