package com.truncon.javalin.mvc.test;

import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AsyncTestUtils {
    private static final int RETRY_COUNT = 200;
    private static final int TIMEOUT_MS = 25;

    public static CompletableFuture<Void> runTest(TestRunner runner) {
        return AppHost.startNew().thenCompose(a -> {
            Throwable ex = null;
            // Try to run the test N times if a SocketException is thrown.
            // This most likely means Javalin hasn't obtained/released the port yet.
            for (int i = 0; i != RETRY_COUNT; ++i) {
                ex = null;
                try {
                    runner.run(a);
                    break;
                } catch (SocketException exception) {
                    ex = exception;
                    try {
                        Thread.sleep(TIMEOUT_MS);
                    } catch (InterruptedException iex) {
                        break;
                    }
                } catch (Throwable exception) {
                    ex = exception;
                    break;
                }
            }
            Throwable error = ex;
            return a.stop().handle((v, closeError) -> {
                if (error != null) {
                    throw new CompletionException(error);
                } else if (closeError != null) {
                    throw new CompletionException(closeError);
                }
                return null;
            });
        });
    }

    @FunctionalInterface
    public interface TestRunner {
        void run(AppHost app) throws Exception;
    }
}
