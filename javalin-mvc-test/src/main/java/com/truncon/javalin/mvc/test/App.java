package com.truncon.javalin.mvc.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.truncon.javalin.mvc.ControllerRegistry;
import io.javalin.plugin.json.JavalinJackson;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Supplier;

public final class App {
    private static final Logger logger = Logger.getLogger(App.class);
    private final Javalin app;

    public static void main(String[] args) throws IOException {
        App app = App.newInstance();
        app.start();
    }

    public static App newInstance() {
        ObjectMapper mapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule())
            .registerModule(new Jdk8Module())
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, true);
        JavalinJackson.configure(mapper);
        Javalin app = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
            config.addStaticFiles("./public", Location.EXTERNAL);
            config.addSinglePageRoot("/", "./public/index.html", Location.EXTERNAL);
        });

        // Provide method of constructing a new DI container
        Supplier<WebContainer> scopeFactory = () -> DaggerWebContainer.builder().build();
        ControllerRegistry registry = new ControllerRegistry(scopeFactory);
        registry.register(app);

        // Prevent unhandled exceptions from taking down the web server
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("Encountered an unhandled exception.", e);
            ctx.status(500);
        });
        return new App(app);
    }

    private App(Javalin app) {
        this.app = app;
    }

    public void start() throws IOException {
        Properties appSettings = getAppSettings();
        app.start(getPort(appSettings));
    }

    private static Properties getAppSettings() throws IOException {
        return SettingsUtilities.loadSettings(
            "./config/application.properties",
            "./config/application-test.properties");
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

    private static int getPort(Properties properties) {
        String portString = properties.getProperty("port");
        if (NumberUtils.isParsable(portString)) {
            return Integer.parseInt(portString);
        }
        return 5001;
    }
}
