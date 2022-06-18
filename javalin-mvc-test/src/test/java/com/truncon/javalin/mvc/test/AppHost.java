package com.truncon.javalin.mvc.test;

import java.util.concurrent.CompletableFuture;

public final class AppHost {
    public static final int PORT = 59933;
    private final App app;

    private AppHost(App app) {
        this.app = app;
    }

    public static CompletableFuture<AppHost> startNewAsync() {
        AppHost app = new AppHost(App.newInstance());
        return app.startAsync().thenApply(v -> app);
    }

    private CompletableFuture<Void> startAsync() {
        return CompletableFuture.runAsync(() -> app.start(PORT));
    }

    public CompletableFuture<Void> stopAsync() {
        return CompletableFuture.runAsync(app::stop);
    }
}
