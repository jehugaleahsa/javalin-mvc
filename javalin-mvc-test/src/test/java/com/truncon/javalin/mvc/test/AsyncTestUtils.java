package com.truncon.javalin.mvc.test;

import java.io.IOException;
import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class AsyncTestUtils {
    public static CompletableFuture<Void> runTest(TestRunner runner) throws IOException {
        return AppHost.startNew().thenCompose(a -> {
            Throwable ex = null;
            // Try to run the test N times if a SocketException is thrown.
            // This most likely means Javalin hasn't obtained/released the port yet.
            for (int i = 0; i != 10; ++i) {
                ex = null;
                try {
                    runner.run(a);
                    break;
                } catch (SocketException exception) {
                    ex = exception;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException iex) {
                        break;
                    }
                } catch (Throwable exception) {
                    ex = exception;
                    break;
                }
            }
            Throwable error = ex;
            return a.close().handle((v, closeError) -> {
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
