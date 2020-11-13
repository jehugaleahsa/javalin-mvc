package com.truncon.javalin.mvc.annotations.processing;

import com.truncon.javalin.mvc.api.Controller;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

final class ControllerSource {
    private final TypeUtils typeUtils;
    private final TypeElement controllerElement;

    private ControllerSource(TypeUtils typeUtils, TypeElement controllerElement) {
        this.typeUtils = typeUtils;
        this.controllerElement = controllerElement;
    }

    public static List<ControllerSource> getControllers(TypeUtils typeUtils, RoundEnvironment environment) throws ProcessingException {
        Set<? extends Element> controllerElements = environment.getElementsAnnotatedWith(Controller.class);
        checkControllerElements(controllerElements);
        return controllerElements.stream()
            .map(e -> (TypeElement) e)
            .map(e -> new ControllerSource(typeUtils, e))
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

    public TypeElement getType() {
        return controllerElement;
    }

    public TypeUtils getTypeUtils() {
        return typeUtils;
    }

    public String getControllerClassName() {
        return controllerElement.getSimpleName().toString();
    }

    public List<RouteGenerator> getRouteGenerators() {
        return controllerElement.getEnclosedElements().stream()
            .filter(e -> e.getKind() == ElementKind.METHOD)
            .map(e -> (ExecutableElement) e)
            .flatMap(e -> RouteGenerator.getGenerators(this, e).stream())
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
}
