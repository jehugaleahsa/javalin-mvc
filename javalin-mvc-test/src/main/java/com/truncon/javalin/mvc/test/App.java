package com.truncon.javalin.mvc.test;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.google.inject.Module;
import com.truncon.javalin.mvc.ControllerRegistry;
import com.truncon.javalin.mvc.JavalinControllerRegistry;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.json.JsonMapper;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
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

            config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
            config.addStaticFiles("./public", Location.EXTERNAL);
            config.addSinglePageRoot("/", "./public/index.html", Location.EXTERNAL);
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

    private static OpenApiOptions getOpenApiOptions() {
        return new OpenApiOptions(new Info()
                .version("1.0")
                .description("Pickle Web"))
            .path("/swagger")
            .swagger(new SwaggerOptions("/swagger-ui")
                .title("Pickle Web API Documentation"))
            .activateAnnotationScanningFor("net.pinnacle21.web");
    }
}
