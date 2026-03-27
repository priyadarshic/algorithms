package com.practice.concurrency;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CompletableFutureDemo provides a comprehensive guide to coordinating asynchronous tasks in Java.
 * 
 * Key Concepts:
 * - Non-blocking execution: Tasks run in the background (ForkJoinPool by default).
 * - Functional Chaining: Transform and combine results using a fluent API.
 * - Reactive Error Handling: Handle exceptions as part of the pipeline.
 */
public class CompletableFutureDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("=== Java CompletableFuture Comprehensive Demo ===\n");

        // 1. Basic Transformation
        System.out.println("Demo 1: supplyAsync -> thenApply (Linear Transformation)");
        System.out.println("Result: " + demonstrateSupplyAsyncAndThenApply().join());
        System.out.println("--------------------------------------------------");

        // 2. Combining two independent futures
        System.out.println("Demo 2: thenCombine (Parallel Execution & Merging)");
        System.out.println("Calculated BMI: " + demonstrateThenCombine().join());
        System.out.println("--------------------------------------------------");

        // 3. Chaining dependent futures
        System.out.println("Demo 3: thenCompose (Sequential Chaining)");
        System.out.println("User Credit Rating: " + demonstrateThenCompose().join());
        System.out.println("--------------------------------------------------");

        // 4. Async threading behavior
        System.out.println("Demo 4: thenApply vs thenApplyAsync (Thread Context)");
        demonstrateAsyncVariants().join();
        System.out.println("--------------------------------------------------");

        // 5. Handling Errors
        System.out.println("Demo 5: Exception Handling with exceptionally()");
        System.out.println("Maturity Status (Valid): " + demonstrateExceptionHandling(25).join());
        System.out.println("Maturity Status (Invalid): " + demonstrateExceptionHandling(-1).join());
        System.out.println("--------------------------------------------------");

        // 6. Waiting for many tasks
        System.out.println("Demo 6: allOf (Coordinating Multiple Tasks)");
        System.out.println("Combined Results: " + demonstrateAllOf().join());
        System.out.println("--------------------------------------------------");

        // 7. Returning a result from allOf
        System.out.println("Demo 7: allOf returning an aggregated result (Sum)");
        System.out.println("Sum Result: " + demonstrateAllOfWithResult().join());

        System.out.println("\n=== All Demos Completed ===");
    }

    /**
     * supplyAsync: Starts a task that returns a value asynchronously.
     * thenApply: Transforms the result once it's available (like map in Streams).
     * Use case: When you need to process a result without blocking the main thread.
     */
    public static CompletableFuture<String> demonstrateSupplyAsyncAndThenApply() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(1); // Simulate network/DB call
            return "Hello";
        }).thenApply(greeting -> greeting + " World!");
    }

    /**
     * thenCombine: Runs two independent futures in parallel and joins them.
     * Use case: Merging data from two different microservices.
     * Logic: Starts weight and height tasks simultaneously, then calculates BMI.
     */
    public static CompletableFuture<Double> demonstrateThenCombine() {
        CompletableFuture<Double> weightFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(1);
            return 70.0;
        });

        CompletableFuture<Double> heightFuture = CompletableFuture.supplyAsync(() -> {
            simulateDelay(1);
            return 1.75;
        });

        // Merges results: (weight, height) -> result
        return weightFuture.thenCombine(heightFuture, (weight, height) -> weight / (height * height));
    }

    /**
     * thenCompose: Used for deep chaining where one future depends on the result of another.
     * Use case: Getting a User object first, then using its ID to fetch Credit Rating.
     * Difference: thenApply returns F<T>, thenCompose returns F<F<T>> and flattens it to F<T>.
     */
    public static CompletableFuture<String> demonstrateThenCompose() {
        return getUserDetails("user123")
                .thenCompose(user -> getCreditRating(user)); // Flattens Nested Future
    }

    private static CompletableFuture<String> getUserDetails(String userId) {
        return CompletableFuture.supplyAsync(() -> "User-" + userId);
    }

    private static CompletableFuture<String> getCreditRating(String user) {
        return CompletableFuture.supplyAsync(() -> user + " has Rating: A+");
    }

    /**
     * thenApplyAsync: Forces the transformation to run in a separate thread.
     * thenApply: Might run in the same thread as the previous stage if it's already done.
     */
    public static CompletableFuture<String> demonstrateAsyncVariants() {
        return CompletableFuture.supplyAsync(() -> {
            simulateDelay(4);
            System.out.println("Initial Task Thread: " + Thread.currentThread().getName());
            return "Result";
        }).thenApply(res -> {
            System.out.println("thenApply Thread (likely same): " + Thread.currentThread().getName());
            return res + "-thenApply";
        }).thenApplyAsync(res -> {
            simulateDelay(5);
            System.out.println("thenApplyAsync Thread (new): " + Thread.currentThread().getName());
            return res + "-thenApplyAsync";
        });
    }

    /**
     * exceptionally: Catches any error in the pipeline and provides a fallback.
     * join() vs get(): join() throws unchecked exceptions, perfect for lambda usage.
     */
    public static CompletableFuture<String> demonstrateExceptionHandling(int age) {
        return CompletableFuture.supplyAsync(() -> {
            if (age < 0) throw new IllegalArgumentException("Invalid age: " + age);
            return age > 18 ? "Adult" : "Minor";
        }).exceptionally(ex -> {
            System.err.println("Audit Log: Caught error -> " + ex.getMessage());
            return "Unknown (Fallback)";
        });
    }

    /**
     * allOf: Completes when ALL given futures are finished.
     * Note: returns CompletableFuture<Void>. You must retrieve results manually.
     */
    public static CompletableFuture<String> demonstrateAllOf() {
        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> "Task1");
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> "Task2");
        CompletableFuture<String> f3 = CompletableFuture.supplyAsync(() -> "Task3");

        CompletableFuture<String> result = CompletableFuture.allOf(f1, f2, f3)
                .thenApply(v -> Stream.of(f1, f2, f3)
                        .map(CompletableFuture::join)
                        .collect(Collectors.joining(", ")));

        System.out.println(result.join());
        return result;
    }

    /**
     * demonstration of allOf returning a single Integer result.
     * We use join() inside thenApply() because allOf guarantees completion.
     */
    public static CompletableFuture<Integer> demonstrateAllOfWithResult() {
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> 20);
        CompletableFuture<Integer> f3 = CompletableFuture.supplyAsync(() -> 30);

        return CompletableFuture.allOf(f1, f2, f3)
                .thenApply(v -> f1.join() + f2.join() + f3.join())
                .exceptionally(ex -> -1);
    }

    private static void simulateDelay(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
