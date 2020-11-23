package com.truncon.javalin.mvc.annotations.processing;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.MvcComponent;
import com.truncon.javalin.mvc.api.Converter;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ws.WsController;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ControllerProcessor extends AbstractProcessor {
    private TypeUtils typeUtils;
    private Filer filer;
    private Messager messager;

    @Override
	public synchronized void init(ProcessingEnvironment env) {
    	super.init(env);
    	typeUtils = new TypeUtils(env.getElementUtils(), env.getTypeUtils());
    	filer = env.getFiler();
		messager = env.getMessager();
    }

	@Override
	public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment env) {
        try {
            ContainerSource container = ContainerSource.getContainerSource(typeUtils, env);
            List<ControllerSource> controllers = ControllerSource.getControllers(typeUtils, env);
            List<WsControllerSource> wsControllers = WsControllerSource.getWsControllers(container, env);
            if (controllers.isEmpty() && wsControllers.isEmpty()) {
                return true;
            }
            List<ConverterBuilder> converters = ConverterBuilder.getConverterBuilders(typeUtils, env);
            ControllerRegistryGenerator generator = new ControllerRegistryGenerator(
                container,
                controllers,
                wsControllers,
                converters);
            generator.generateRegistry(filer);
        } catch (ProcessingMultiException exception) {
            for (ProcessingException subException : exception.getExceptions()) {
                for (Element element : subException.getElements()) {
                    messager.printMessage(Diagnostic.Kind.ERROR, subException.getMessage(), element);
                }
            }
        } catch (ProcessingException exception) {
            for (Element element : exception.getElements()) {
                messager.printMessage(Diagnostic.Kind.ERROR, exception.toString(), element);
            }
        } catch (Throwable exception) {
            messager.printMessage(
                Diagnostic.Kind.ERROR,
                ExceptionUtils.getMessage(exception) + ": " + ExceptionUtils.getStackTrace(exception));
        }
        return true;
    }

	@Override
	public Set<String> getSupportedAnnotationTypes() {
		Set<String> types = new HashSet<>();
		types.add(Converter.class.getCanonicalName());
		types.add(UseConverter.class.getCanonicalName());
		types.add(Controller.class.getCanonicalName());
		types.add(WsController.class.getCanonicalName());
		types.add(MvcComponent.class.getCanonicalName());
		return types;
    }

	@Override
	public SourceVersion getSupportedSourceVersion() {
		return SourceVersion.latestSupported();
    }
} 
