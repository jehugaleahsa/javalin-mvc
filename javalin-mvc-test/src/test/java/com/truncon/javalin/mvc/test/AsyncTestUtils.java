package com.truncon.javalin.mvc.test;

import java.net.SocketException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

public class AsyncTestUtils {
    private static final int RETRY_COUNT = 200;
    private static final int TIMEOUT_MS = 250;

    public static void runTestAsync(AsyncTestRunner runner) {
        AppHost.startNewAsync().thenCompose(a -> {
            CompletableFuture<Void> future = retry(() -> runner.runAsync(a), RETRY_COUNT);
            return future.handle((r, error) ->
                a.stopAsync().handle((r2, closeError) -> {
                    if (error != null) {
                        throw new CompletionException(error);
                    } else if (closeError != null) {
                        throw new CompletionException(closeError);
                    } else {
                        return r2;
                    }
                })
            );
        }).join();
    }

    private static CompletableFuture<Void> retry(Supplier<CompletableFuture<Void>> futureSupplier, int retryCount) {
        CompletableFuture<Void> future = futureSupplier.get();
        if (retryCount == 0) {
            return future;
        }
        return future.handle((r, ex) -> {
            if (ex == null) {
                return CompletableFuture.completedFuture(r);
            } else if (ex instanceof CompletionException &&  ex.getCause() instanceof SocketException) {
                return CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(TIMEOUT_MS);
                    } catch (InterruptedException exception) {
                        throw new CompletionException(exception);
                    }
                }).thenCompose(v -> retry(futureSupplier, retryCount - 1));
            } else {
                throw new CompletionException(ex);
            }
        }).thenCompose(v -> v);
    }

    public static void runTest(TestRunner runner) {
        runTestAsync(a -> CompletableFuture.runAsync(() -> {
            try {
                runner.run(a);
            } catch (Throwable throwable) {
                throw new CompletionException(throwable);
            }
        }));
    }

    @FunctionalInterface
    public interface TestRunner {
        void run(AppHost app) throws Exception;
    }

    @FunctionalInterface
    public interface AsyncTestRunner {
        CompletableFuture<Void> runAsync(AppHost app);
    }
}
