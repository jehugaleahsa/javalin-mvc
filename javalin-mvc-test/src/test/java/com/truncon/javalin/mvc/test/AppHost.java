package com.truncon.javalin.mvc.test;

import java.util.concurrent.CompletableFuture;

public final class AppHost {
    public static final int PORT = 57021;
    private final App app;

    private AppHost(App app) {
        this.app = app;
    }

    public static CompletableFuture<AppHost> startNew() {
        AppHost app = new AppHost(App.newInstance());
        return app.start().thenApply(v -> app);
    }

    private CompletableFuture<Void> start() {
        return app.start(PORT);
    }

    public CompletableFuture<Void> stop() {
        return app.stop();
    }
}
