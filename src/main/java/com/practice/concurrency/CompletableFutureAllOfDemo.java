package com.practice.concurrency;

import java.util.concurrent.CompletableFuture;

/**
 * Demonstrates the use of {@link CompletableFuture#allOf(CompletableFuture[])}
 * to coordinate multiple asynchronous tasks.
 */
public class CompletableFutureAllOfDemo {

    public static void main(String[] args) {
        System.out.println("=== CompletableFuture.allOf() Result Aggregation Demo ===\n");

        // 1. Initiate multiple independent asynchronous tasks
        CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(1000);
            System.out.println("[Task 1] Fetched ID 10");
            return 10;
        });

        CompletableFuture<Integer> future2 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(2000);
            System.out.println("[Task 2] Fetched ID 20");
            return 20;
        });

        CompletableFuture<Integer> future3 = CompletableFuture.supplyAsync(() -> {
            simulateDelay(500);
            System.out.println("[Task 3] Fetched ID 30");
            return 30;
        });

        // 2. allOf(futures...) creates a future that completes when ALL inputs are finished.
        // It returns CompletableFuture<Void>, so we chain it to process results.
        CompletableFuture<Void> allOfFuture = CompletableFuture.allOf(future1, future2, future3);

        // 3. Coordinate results once all tasks have finished.
        // We use thenApply() to transform the signals into a single aggregated Integer.
        CompletableFuture<Integer> sumFuture = allOfFuture.thenApply(v -> {
            // join() is safe here because allOf has guaranteed completion of f1, f2, f3.
            // join() throws unchecked CompletionException, no try-catch needed here.
            return future1.join() + future2.join() + future3.join();
        }).exceptionally(ex -> {
            // Handle any potential failures gracefully
            System.err.println("Error encountered in pipeline: " + ex.getMessage());
            return -1; // Fallback value
        });

        System.out.println("Pipeline active. Waiting for aggregation...");

        // 4. Retrieve the final value. join() blocks until the entire pipeline is done.
        Integer finalResult = sumFuture.join();

        System.out.println("\nAll tasks synchronized.");
        System.out.println("Final Aggregated Sum: " + finalResult);
    }

    private static void simulateDelay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
