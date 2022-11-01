package com.truncon.javalin.mvc.test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
//import com.google.inject.Module;
import com.truncon.javalin.mvc.ControllerRegistry;
import com.truncon.javalin.mvc.JavalinControllerRegistry;
import io.javalin.json.JavalinJackson;
import io.javalin.json.JsonMapper;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.openapi.OpenApiInfo;
import io.javalin.openapi.plugin.OpenApiConfiguration;
import io.javalin.openapi.plugin.OpenApiPlugin;
import io.javalin.openapi.plugin.swagger.SwaggerConfiguration;
import io.javalin.openapi.plugin.swagger.SwaggerPlugin;
import org.apache.log4j.Logger;

public final class App {
    private static final Logger LOGGER = Logger.getLogger(App.class);
    private final Javalin app;

    public static void main(String[] args) {
        App app = App.newInstance();
        app.start(5001);
    }

    public static App newInstance() {
        Javalin app = Javalin.create(config -> {
            config.showJavalinBanner = false;

            ObjectMapper mapper = new ObjectMapper()
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
            JsonMapper jsonMapper = new JavalinJackson(mapper);
            config.jsonMapper(jsonMapper);

            config.plugins.register(new OpenApiPlugin(getOpenApiOptions()));
            config.plugins.register(new SwaggerPlugin(new SwaggerConfiguration()));

            config.staticFiles.add("./public", Location.EXTERNAL);
            config.spaRoot.addFile("/", "./public/index.html", Location.EXTERNAL);
        });

        // Provide method of constructing a new DI container
        ControllerRegistry registry = new JavalinControllerRegistry(DaggerWebContainer::create);
        //Module module = new GuiceAppModule();
        //ControllerRegistry registry = new JavalinControllerRegistry(() -> new GuiceInjector(module));
        registry.register(app);

        // Prevent unhandled exceptions from taking down the web server
        app.exception(Exception.class, (e, ctx) -> {
            LOGGER.error("Encountered an unhandled exception.", e);
            ctx.status(500);
        });
        return new App(app);
    }

    private App(Javalin app) {
        this.app = app;
    }

    public void start(int port) {
        app.start(port);
    }

    public void stop() {
        app.stop();
    }

    private static OpenApiConfiguration getOpenApiOptions() {
        OpenApiConfiguration configuration = new OpenApiConfiguration();
        OpenApiInfo info = configuration.getInfo();
        info.setVersion("1.0");
        info.setDescription("Pickle Web");
        return configuration;
    }
}
