package com.truncon.javalin.mvc.test;

import java.io.IOException;

public final class AppHost implements AutoCloseable {
    private final App app;

    private AppHost(App app) {
        this.app = app;
    }

    public static AppHost startNew() throws IOException {
        AppHost app = new AppHost(App.newInstance());
        app.start();
        return app;
    }

    private void start() throws IOException {
        app.start();
    }

    public void close() {
        app.stop();
    }
}
