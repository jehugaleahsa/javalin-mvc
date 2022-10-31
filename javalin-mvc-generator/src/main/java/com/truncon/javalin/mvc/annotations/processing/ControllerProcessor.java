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
import javax.tools.FileObject;
import javax.tools.StandardLocation;

import com.truncon.javalin.mvc.api.Controller;
import com.truncon.javalin.mvc.api.MvcComponent;
import com.truncon.javalin.mvc.api.Converter;
import com.truncon.javalin.mvc.api.UseConverter;
import com.truncon.javalin.mvc.api.ws.WsController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public final class ControllerProcessor extends AbstractProcessor {
    private static final String CONTAINERS_PATH = "META-INF/annotated-classes/containers.lst";
    private static final String CONTROLLERS_PATH = "META-INF/annotated-classes/controllers.lst";
    private static final String WS_CONTROLLERS_PATH = "META-INF/annotated-classes/ws_controllers.lst";
    private static final String CONVERTERS_PATH = "META-INF/annotated-classes/converters.lst";

    private final Set<TypeElement> processedContainers = new HashSet<>();
    private final Set<TypeElement> processedControllers = new HashSet<>();
    private final Set<TypeElement> processedWsControllers = new HashSet<>();
    private final Set<TypeElement> processedConverters = new HashSet<>();
    private TypeUtils typeUtils;
    private Filer filer;
    private Messager messager;
    private boolean processed;
    private boolean done;

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
            if (this.done) {
                // We stored all the classes we processed in files.
                // We don't want to do any more work.
                return true;
            }
            if (this.processed) {
                // On the first pass, we track all processed elements.
                // We saved off all elements to a file to support incremental builds.
                writeProcessedFiles();
                this.done = true;
                return true;
            }

            this.processed = true;

            this.processedContainers.addAll(getPreviouslyProcessedClasses(CONTAINERS_PATH));
            ContainerSource container = ContainerSource.getContainerSource(typeUtils, env, this.processedContainers);
            this.processedContainers.clear();
            TypeElement containerType = container.getType();
            if (containerType != null) {
                this.processedContainers.add(containerType);
            }

            this.processedControllers.addAll(getPreviouslyProcessedClasses(CONTROLLERS_PATH));
            List<ControllerSource> controllers = ControllerSource.getControllers(typeUtils, env, this.processedControllers);
            this.processedControllers.clear();
            List<TypeElement> controllerTypes = controllers.stream()
                .map(ControllerSource::getType)
                .collect(Collectors.toList());
            this.processedControllers.addAll(controllerTypes);

            this.processedWsControllers.addAll(getPreviouslyProcessedClasses(WS_CONTROLLERS_PATH));
            List<WsControllerSource> wsControllers = WsControllerSource.getWsControllers(typeUtils, env, this.processedWsControllers);
            this.processedWsControllers.clear();
            List<TypeElement> wsControllerTypes = wsControllers.stream()
                .map(WsControllerSource::getType)
                .collect(Collectors.toList());
            this.processedWsControllers.addAll(wsControllerTypes);

            this.processedConverters.addAll(getPreviouslyProcessedClasses(CONVERTERS_PATH));
            List<ConverterBuilder> converters = ConverterBuilder.getConverterBuilders(typeUtils, env, this.processedConverters);
            this.processedConverters.clear();
            List<TypeElement> converterTypes = converters.stream()
                .map(ConverterBuilder::getConversionClass)
                .distinct()
                .collect(Collectors.toList());
            this.processedConverters.addAll(converterTypes);

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

    private void writeProcessedFiles() {
        try {
            writeProcessedFile(CONTAINERS_PATH, this.processedContainers);
            writeProcessedFile(CONTROLLERS_PATH, this.processedControllers);
            writeProcessedFile(WS_CONTROLLERS_PATH, this.processedWsControllers);
            writeProcessedFile(CONVERTERS_PATH, this.processedConverters);
        } catch (IOException exception) {
            messager.printMessage(Diagnostic.Kind.ERROR, exception.getMessage());
        }
    }

    private void writeProcessedFile(String resourceName, Collection<TypeElement> types) throws IOException {
        FileObject file = filer.createResource(StandardLocation.CLASS_OUTPUT, "", resourceName);
        try (PrintWriter writer = new PrintWriter(file.openWriter())) {
            for (TypeElement type : types) {
                writer.write(type.getQualifiedName().toString());
                writer.write("\n");
            }
        }
        types.clear();
    }

    private Set<TypeElement> getPreviouslyProcessedClasses(String resourceName) {
        try {
            FileObject file = filer.getResource(StandardLocation.CLASS_OUTPUT, "", resourceName);
            List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(file.openReader(true))) {
                String line = reader.readLine();
                while (line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
            }
            return lines.stream()
                .distinct()
                .map(n -> typeUtils.getTypeElement(n))
                .filter(Objects::nonNull) // Someone could have deleted the class since last time
                .collect(Collectors.toSet());
        } catch (IOException exception) {
            messager.printMessage(Diagnostic.Kind.NOTE, exception.getMessage());
            return Collections.emptySet();
        }
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
