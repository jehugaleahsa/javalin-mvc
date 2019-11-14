package com.truncon.javalin.mvc.test;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public final class RouteBuilder {
    private RouteBuilder() {
    }

    public static String buildRoute(String path, Map<String, String> pathReplacements) throws IOException {
        Properties settings = loadConfig();
        if (path == null) {
            path = "";
        } else if (!path.startsWith("/")) {
            path = "/" + path;
        }
        for (String pathKey : pathReplacements.keySet()) {
            String replacement = pathReplacements.get(pathKey);
            if (!pathKey.startsWith(":")) {
                pathKey = ":" + pathKey;
            }
            path = path.replaceAll(pathKey, replacement);
        }
        return "http://localhost:" + settings.getProperty("port") + path;
    }

    private static Properties loadConfig() throws IOException {
        return SettingsUtilities.loadSettings(
                "./config/application.properties",
                "./config/application-test.properties");
    }
}
