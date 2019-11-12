package com.truncon.javalin.mvc.annotations.processing;

import javax.lang.model.element.Element;

public final class ProcessingException extends Exception {
    private final Element[] elements;

    public ProcessingException(String message, Element ...elements) {
        super(message);
        this.elements = elements;
    }

    public Element[] getElements() {
        return elements;
    }
}
