package com.truncon.javalin.mvc.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public final class SettingsUtilities {
    private SettingsUtilities() {
    }

    public static Properties loadSettings(String ...paths) throws IOException {
        Properties settings = new Properties();
        for (String path : paths) {
            loadSettings(settings, path);
        }
        return settings;
    }

    private static void loadSettings(Properties settings, String url) throws IOException {
        Path settingsPath = Paths.get(url);
        if (Files.exists(settingsPath)) {
            try (InputStream settingsStream = new FileInputStream(settingsPath.toFile())) {
                settings.load(settingsStream);
            }
        }
    }
}
