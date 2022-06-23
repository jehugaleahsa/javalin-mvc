package com.truncon.javalin.mvc.annotations.processing;

import com.squareup.javapoet.CodeBlock;
import com.truncon.javalin.mvc.api.Injector;
import com.truncon.javalin.mvc.api.MvcModule;
import dagger.Component;
import com.truncon.javalin.mvc.api.MvcComponent;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

final class ContainerSource {
    private final TypeUtils typeUtils;
    private final Type type;
    private final TypeElement containerElement;
    private final List<ExecutableElement> dependencies;

    private ContainerSource(
            TypeUtils typeUtils,
            Type type,
            TypeElement containerElement,
            List<ExecutableElement> dependencies) {
        this.typeUtils = typeUtils;
        this.type = type;
        this.containerElement = containerElement;
        this.dependencies = dependencies;
    }

    public TypeUtils getTypeUtils() {
        return typeUtils;
    }

    public static ContainerSource getContainerSource(
            TypeUtils typeUtils,
            RoundEnvironment environment,
            Collection<TypeElement> alternatives) throws ProcessingException {
        ContainerSource daggerContainer = getDaggerContainer(typeUtils, environment, alternatives);
        if (daggerContainer != null) {
            return daggerContainer;
        }
        ContainerSource guiceContainer = getGuiceContainer(typeUtils, environment, alternatives);
        if (guiceContainer != null) {
            return guiceContainer;
        }

        return new ContainerSource(typeUtils, Type.NONE, null, Collections.emptyList());
    }

    private static ContainerSource getDaggerContainer(
            TypeUtils typeUtils,
            RoundEnvironment environment,
            Collection<TypeElement> alternatives) {
        List<? extends TypeElement> elements = Stream.concat(
                environment.getElementsAnnotatedWith(Component.class).stream(),
                alternatives.stream() // Classes from previous builds, to support incremental
            )
            .distinct()
            .filter(e -> e.getKind() == ElementKind.INTERFACE)
            .filter(e -> e.getAnnotation(MvcComponent.class) != null)
            .map(e -> (TypeElement) e)
            .collect(Collectors.toList());
        if (elements.size() > 1) {
            Element[] badElements = elements.toArray(new Element[0]);
            String message = "More than one "
                + Component.class.getSimpleName()
                + " classes annotated with "
                + MvcComponent.class.getSimpleName()
                + " were found.";
            throw new ProcessingException(message, badElements);
        }
        if (!elements.isEmpty()) {
            TypeElement typeElement = elements.get(0);
            List<ExecutableElement> dependencies = getDependencies(typeUtils, typeElement)
                .collect(Collectors.toList());
            return new ContainerSource(typeUtils, Type.DAGGER, typeElement, dependencies);
        }
        return null;
    }

    private static ContainerSource getGuiceContainer(
        TypeUtils typeUtils,
        RoundEnvironment environment,
        Collection<TypeElement> alternatives) {
        List<? extends TypeElement> elements = Stream.concat(
                environment.getElementsAnnotatedWith(MvcModule.class).stream(),
                alternatives.stream() // Classes from previous builds, to support incremental
            )
            .distinct()
            .filter(e -> e.getKind() == ElementKind.INTERFACE)
            .filter(e -> e.getAnnotation(MvcModule.class) != null)
            .map(e -> (TypeElement) e)
            .collect(Collectors.toList());
        if (elements.size() > 1) {
            Element[] badElements = elements.toArray(new Element[0]);
            String message = "More than one classes annotated with "
                + MvcModule.class.getSimpleName()
                + " were found.";
            throw new ProcessingException(message, badElements);
        }
        if (!elements.isEmpty()) {
            TypeElement typeElement = elements.get(0);
            return new ContainerSource(typeUtils, Type.DAGGER, typeElement, Collections.emptyList());
        }
        return null;
    }

    private static Stream<ExecutableElement> getDependencies(TypeUtils typeUtils, TypeElement container) {
        return Stream.concat(
            container.getEnclosedElements().stream()
                .filter(e -> e.getKind() == ElementKind.METHOD)
                .map(e -> (ExecutableElement) e),
            container.getInterfaces().stream()
                .map(i -> (TypeElement) typeUtils.asElement(i))
                .flatMap(i -> getDependencies(typeUtils, i))
        );

    }

    public Type getContainerType() {
        return type;
    }

    public TypeElement getType() {
        return containerElement;
    }

    public TypeMirror getInjectorType() {
        if (type == Type.DAGGER) {
            return containerElement.asType();
        } else if (type == Type.RUNTIME) {
            return typeUtils.toType(Injector.class);
        } else {
            return null;
        }
    }

    public boolean isFound() {
        return type != Type.NONE;
    }

    public InjectionResult getInstanceCall(TypeMirror type, String injectorName) {
        if (injectorName != null) {
            if (this.type == ContainerSource.Type.DAGGER) {
                Name dependencyName = getDependencyName(type);
                if (dependencyName != null) {
                    CodeBlock call = CodeBlock.of("$N.$L()", injectorName, dependencyName);
                    return new InjectionResult(call, true);
                }
            } else if (this.type == ContainerSource.Type.RUNTIME) {
                CodeBlock call = CodeBlock.of("$N.getInstance($T.class)", injectorName, type);
                return new InjectionResult(call, true);
            }
        }
        CodeBlock call = CodeBlock.of("new $T()", type);
        return new InjectionResult(call, false);
    }

    private Name getDependencyName(TypeMirror searchType) {
        TypeElement element = typeUtils.getTypeElement(searchType);
        return getDependencyName(element);
    }

    public Name getDependencyName(TypeElement searchType) {
        for (ExecutableElement dependency : dependencies) {
            if (typeUtils.isSameType(searchType.asType(), dependency.getReturnType())) {
                return dependency.getSimpleName();
            }
        }
        return null;
    }

    public enum Type {
        NONE,
        DAGGER,
        RUNTIME
    }
}
