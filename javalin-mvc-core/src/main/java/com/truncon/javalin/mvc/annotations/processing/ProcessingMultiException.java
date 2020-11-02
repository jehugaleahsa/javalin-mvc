package com.truncon.javalin.mvc.annotations.processing;

public final class ProcessingMultiException extends RuntimeException {
    private final ProcessingException[] exceptions;

    public ProcessingMultiException(String message, ProcessingException ...exceptions) {
        super(message);
        this.exceptions = exceptions;
    }

    public ProcessingException[] getExceptions() {
        return exceptions;
    }
}
