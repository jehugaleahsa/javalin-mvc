package com.truncon.javalin.mvc.test;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public final class AppHost {
    private final App app;

    private AppHost(App app) {
        this.app = app;
    }

    public static CompletableFuture<AppHost> startNew() throws IOException {
        AppHost app = new AppHost(App.newInstance());
        return app.start().thenApply(v -> app);
    }

    private CompletableFuture<Void> start() throws IOException {
        return app.start();
    }

    public CompletableFuture<Void> close() {
        return app.stop();
    }
}
