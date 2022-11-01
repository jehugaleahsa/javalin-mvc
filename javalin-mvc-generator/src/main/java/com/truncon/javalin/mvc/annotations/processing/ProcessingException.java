package com.truncon.javalin.mvc.annotations.processing;

import javax.lang.model.element.Element;

public final class ProcessingException extends RuntimeException {
    private final Element[] elements;

    public ProcessingException(String message, Element ...elements) {
        this(message, null, elements);
    }

    public ProcessingException(String message, Throwable cause, Element ...elements) {
        super(message, cause);
        this.elements = elements;
    }

    public Element[] getElements() {
        return elements;
    }
}
