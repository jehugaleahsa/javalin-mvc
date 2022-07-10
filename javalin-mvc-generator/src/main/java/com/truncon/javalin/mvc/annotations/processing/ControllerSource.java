package com.truncon.javalin.mvc.annotations.processing;

import com.truncon.javalin.mvc.api.Controller;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ControllerSource {
    private final TypeUtils typeUtils;
    private final TypeElement controllerElement;
    private final String prefix;

    private ControllerSource(TypeUtils typeUtils, TypeElement controllerElement, String prefix) {
        this.typeUtils = typeUtils;
        this.controllerElement = controllerElement;
        this.prefix = prefix;
    }

    public static List<ControllerSource> getControllers(
            TypeUtils typeUtils,
            RoundEnvironment environment,
            Collection<TypeElement> alternateTypes) throws ProcessingException {
        Set<Element> controllerElements = getControllerElements(environment);
        checkControllerElements(controllerElements);
        Stream<TypeElement> controllerTypes = controllerElements.stream()
            .map(TypeElement.class::cast);
        Stream<TypeElement> oldControllerTypes = alternateTypes.stream()
            .filter(t -> t.getAnnotation(Controller.class) != null || t.getAnnotation(javax.ws.rs.Path.class) != null);
        return Stream.concat(controllerTypes, oldControllerTypes)
            .distinct()
            .map(e -> new ControllerSource(typeUtils, e, getPrefix(e)))
            .collect(Collectors.toList());
    }

    private static Set<Element> getControllerElements(RoundEnvironment environment) {
        Set<? extends Element> builtin = environment.getElementsAnnotatedWith(Controller.class);
        Set<Element> elements = new HashSet<>(builtin);
        List<Element> javax = environment.getElementsAnnotatedWith(javax.ws.rs.Path.class).stream()
            .filter(e -> e.getKind() == ElementKind.INTERFACE || e.getKind() == ElementKind.CLASS)
            .collect(Collectors.toList());
        elements.addAll(javax);
        return elements;
    }

    private static void checkControllerElements(Set<Element> elements) throws ProcessingException {
        Element[] badElements = elements.stream()
            .filter(e -> e.getKind() != ElementKind.CLASS || e.getModifiers().contains(Modifier.ABSTRACT))
            .toArray(Element[]::new);
        if (badElements.length > 0) {
            String message = "Controller annotations can only be applied to non-abstract classes.";
            throw new ProcessingException(message, badElements);
        }
    }

    private static String getPrefix(TypeElement element) {
        Controller builtin = element.getAnnotation(Controller.class);
        if (builtin != null) {
            return builtin.prefix();
        }
        javax.ws.rs.Path javax = element.getAnnotation(javax.ws.rs.Path.class);
        if (javax != null) {
            return javax.value();
        }
        return null;
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

    public String getPrefix() {
        return prefix;
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
