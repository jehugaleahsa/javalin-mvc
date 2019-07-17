package io.javalin.mvc.annotations.processing;

import java.util.*;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import io.javalin.mvc.api.Controller;

public final class ControllerProcessor extends AbstractProcessor {
    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;

    @Override
	public synchronized void init(ProcessingEnvironment env) {
    	super.init(env);
    	typeUtils = env.getTypeUtils();
    	elementUtils = env.getElementUtils();
    	filer = env.getFiler();
		messager = env.getMessager();
    }

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        try {
            List<ControllerSource> controllers = ControllerSource.getRouteSources(typeUtils, elementUtils, env);
            if (controllers.isEmpty()) {
                return true;
            }
            ContainerSource container = ContainerSource.getContainerSource(typeUtils, elementUtils, env);
            ControllerRegistryGenerator generator = new ControllerRegistryGenerator(container, controllers);
            generator.generateRoutes(filer);
        } catch (ProcessingException exception) {
            for (Element element : exception.getElements()) {
                messager.printMessage(Diagnostic.Kind.ERROR, exception.toString(), element);
            }
        } catch (Exception exception) {
            messager.printMessage(Diagnostic.Kind.ERROR, exception.toString());
        }
        return true;
    }

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> types = new HashSet<>();
		types.add(Controller.class.getCanonicalName());
		return types;
    }

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
    }
} 