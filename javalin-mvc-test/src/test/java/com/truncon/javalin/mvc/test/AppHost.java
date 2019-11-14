package com.truncon.javalin.mvc.test;

import com.truncon.javalin.mvc.ControllerRegistry;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Supplier;

public final class AppHost implements AutoCloseable {
    private static final Logger logger = Logger.getLogger(AppHost.class);
    private Javalin app;

    public static AppHost startNew() throws IOException {
        AppHost app = new AppHost();
        app.start();
        return app;
    }

    public void start() throws IOException {
        this.app = Javalin.create(config -> {
            config.showJavalinBanner = false;
            config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
            config.addStaticFiles("./public", Location.EXTERNAL);
            config.addSinglePageRoot("/", "./public/index.html", Location.EXTERNAL);
        });

        Properties appSettings = SettingsUtilities.loadSettings(
            "./config/application.properties",
            "./config/application-test.properties");

        // Provide method of constructing a new DI container
        Supplier<WebContainer> scopeFactory = () -> DaggerWebContainer.builder().build();
        ControllerRegistry registry = new ControllerRegistry(scopeFactory);
        registry.register(app);

        // Prevent unhandled exceptions from taking down the web server
        app.exception(Exception.class, (e, ctx) -> {
            logger.error("Encountered an unhandled exception.", e);
            ctx.status(500);
        });

        app.start(getPort(appSettings));
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

    public void stop() {
        app.stop();
    }

    public void close() {
        stop();
    }
}
