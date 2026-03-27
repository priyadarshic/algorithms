package com.practice.concurrency;

import org.junit.jupiter.api.Test;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.junit.jupiter.api.Assertions.*;

public class CompletableFutureDemoTest {

    @Test
    public void testSupplyAsyncAndThenApply() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFutureDemo.demonstrateSupplyAsyncAndThenApply();
        String result = future.get();
        assertEquals("Hello World!", result);
    }

    @Test
    public void testThenCombine() throws ExecutionException, InterruptedException {
        CompletableFuture<Double> future = CompletableFutureDemo.demonstrateThenCombine();
        Double result = future.get();
        assertEquals(22.857142857142858, result, 0.0001);
    }

    @Test
    public void testThenCompose() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFutureDemo.demonstrateThenCompose();
        String result = future.get();
        assertEquals("User-user123 has Rating: A+", result);
    }

    @Test
    public void testAsyncVariants() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFutureDemo.demonstrateAsyncVariants();
        String result = future.get();
        assertEquals("Result-thenApply-thenApplyAsync", result);
    }

    @Test
    public void testExceptionHandlingSuccess() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFutureDemo.demonstrateExceptionHandling(25);
        String result = future.get();
        assertEquals("Adult", result);
    }

    @Test
    public void testExceptionHandlingFailure() throws ExecutionException, InterruptedException {
        CompletableFuture<String> future = CompletableFutureDemo.demonstrateExceptionHandling(-5);
        String result = future.get();
        assertEquals("Unknown (Error Handled)", result);
    }

    @Test
    public void testAllOf() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<String> future = CompletableFutureDemo.demonstrateAllOf();
        String result = future.get(5, TimeUnit.SECONDS);
        // System.out.println(result);
        assertTrue(result.contains("Task 1"));
        assertTrue(result.contains("Task 2"));
        assertTrue(result.contains("Task 3"));
    }

    @Test
    public void testAllOfWithResult() throws ExecutionException, InterruptedException, TimeoutException {
        CompletableFuture<Integer> future = CompletableFutureDemo.demonstrateAllOfWithResult();
        Integer result = future.get(5, TimeUnit.SECONDS);
        assertEquals(60, result);
    }

    @Test
    public void testAllOfWithException() throws ExecutionException, InterruptedException, TimeoutException {
        // Create a version that fails
        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> 10);
        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> {
            throw new RuntimeException("Test Exception");
        });

        CompletableFuture<Integer> resultFuture = CompletableFuture.allOf(f1, f2)
                .thenApply(v -> f1.join() + f2.join())
                .exceptionally(ex -> -1); // Handle error

        Integer result = resultFuture.get(5, TimeUnit.SECONDS);
        assertEquals(-1, result);
    }
}
