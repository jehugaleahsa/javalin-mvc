package com.truncon.javalin.mvc.annotations.processing;

import com.truncon.javalin.mvc.api.Controller;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

final class ControllerSource {
    private final Types typeUtils;
    private final Elements elementUtils;
    private final TypeElement controllerElement;

    private ControllerSource(Types typeUtils, Elements elementUtils, TypeElement controllerElement) {
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.controllerElement = controllerElement;
    }

    public static List<ControllerSource> getControllers(Types typeUtils, Elements elementUtils, RoundEnvironment environment) throws ProcessingException {
        Set<? extends Element> controllerElements = environment.getElementsAnnotatedWith(Controller.class);
        checkControllerElements(controllerElements);
        return controllerElements.stream()
                .map(e -> (TypeElement)e)
                .map(e -> new ControllerSource(typeUtils, elementUtils, e))
                .collect(Collectors.toList());
    }

    private static void checkControllerElements(Set<? extends Element> elements) throws ProcessingException {
        Element[] badElements = elements.stream()
                .filter(e -> e.getKind() != ElementKind.CLASS)
                .toArray(Element[]::new);
        if (badElements.length > 0) {
            throw new ProcessingException("Controller annotations can only be applied to classes.", badElements);
        }
    }

    Types getTypeUtils() {
        return typeUtils;
    }

    Elements getElementUtils() {
        return elementUtils;
    }

    public TypeElement getType() {
        return controllerElement;
    }

    public String getControllerClassName() {
        return controllerElement.getSimpleName().toString();
    }

    public List<RouteGenerator> getRouteGenerators() {
        return controllerElement.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .map(e -> (ExecutableElement)e)
                .flatMap(e -> RouteGenerator.getGenerators(this, e).stream())
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
