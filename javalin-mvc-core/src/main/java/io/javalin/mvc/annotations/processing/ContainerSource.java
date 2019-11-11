package io.javalin.mvc.annotations.processing;

import dagger.Component;
import io.javalin.mvc.api.ControllerComponent;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ContainerSource {
    private final Types typeUtils;
    private final Elements elementUtils;
    private final TypeElement containerElement;
    private final List<ExecutableElement> dependencies;

    private ContainerSource(
            Types typeUtils,
            Elements elementUtils,
            TypeElement containerElement,
            List<ExecutableElement> dependencies) {
        this.typeUtils = typeUtils;
        this.elementUtils = elementUtils;
        this.containerElement = containerElement;
        this.dependencies = dependencies;
    }

    Types getTypeUtils() {
        return typeUtils;
    }

    Elements getElementUtils() {
        return elementUtils;
    }

    public static ContainerSource getContainerSource(
            Types typeUtils,
            Elements elementUtils,
            RoundEnvironment environment) throws ProcessingException {
        List<? extends TypeElement> elements = environment.getElementsAnnotatedWith(Component.class).stream()
                .filter(e -> e.getKind() == ElementKind.INTERFACE)
                .filter(e -> e.getAnnotation(ControllerComponent.class) != null)
                .map(e -> (TypeElement)e)
                .collect(Collectors.toList());
        if (elements.size() > 1) {
            Element[] badElements = elements.toArray(new Element[0]);
            throw new ProcessingException("Multiple Dagger Components annotated with ControllerComponent were found.", badElements);
        }
        if (elements.size() == 0) {
            return new ContainerSource(typeUtils, elementUtils, null, new ArrayList<>());
        }
        Element element = elements.toArray(new Element[0])[0];
        TypeElement typeElement = (TypeElement)element;

        List<ExecutableElement> dependencies = getDependencies(typeUtils, typeElement).collect(Collectors.toList());
        return new ContainerSource(typeUtils, elementUtils, typeElement, dependencies);
    }

    private static Stream<ExecutableElement> getDependencies(Types typeUtils, TypeElement container) {
        return Stream.concat(
            container.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .map(e -> (ExecutableElement)e),
            container.getInterfaces().stream()
                .map(i -> (TypeElement)typeUtils.asElement(i))
                .flatMap(i -> getDependencies(typeUtils, i))
        );

    }

    public TypeMirror getType() {
        return containerElement == null ? null : containerElement.asType();
    }

    public boolean isFound() {
        return containerElement != null;
    }

    public Name getDependencyName(Class<?> dependencyClass) {
        TypeElement searchType = elementUtils.getTypeElement(dependencyClass.getCanonicalName());
        return getDependencyName(searchType);
    }

    public Name getDependencyName(Name dependencyName) {
        TypeElement searchType = elementUtils.getTypeElement(dependencyName);
        return getDependencyName(searchType);
    }

    public Name getDependencyName(TypeElement searchType) {
        for (ExecutableElement dependency : dependencies) {
            if (typeUtils.isSameType(searchType.asType(), dependency.getReturnType())) {
                return dependency.getSimpleName();
            }
        }
        return  null;
    }
}
