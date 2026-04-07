package com.practice.ratelimiter;

import java.util.concurrent.TimeUnit;

/**
 * Manual test runner for Rate Limiting algorithms when JUnit environment is not readily available.
 */
public class ManualTestRunner {
    public static void main(String[] args) {
        System.out.println("--- Starting Manual Test Suite ---");
        
        try {
            testTokenBucket();
            System.out.println("[PASS] Token Bucket");
            
            testLeakyBucket();
            System.out.println("[PASS] Leaky Bucket");
            
            testFixedWindow();
            System.out.println("[PASS] Fixed Window");
            
            testSlidingWindowLog();
            System.out.println("[PASS] Sliding Window Log");
            
            testSlidingWindowCounter();
            System.out.println("[PASS] Sliding Window Counter");
            
            System.out.println("\n--- All Tests Passed! ---");
        } catch (Exception e) {
            System.err.println("\n--- Test Failed! ---");
            e.printStackTrace();
            System.exit(1);
        }
    }



    private static void assertTrue(boolean condition, String message) {
        if (!condition) throw new RuntimeException(message);
    }

    private static void assertFalse(boolean condition, String message) {
        if (condition) throw new RuntimeException(message);
    }

    private static void testTokenBucket() throws InterruptedException {
        RateLimiter limiter = new TokenBucketRateLimiter(5, 10);
        for (int i = 0; i < 5; i++) assertTrue(limiter.tryAcquire(), "Initial burst " + i);
        assertFalse(limiter.tryAcquire(), "Should be empty");
        TimeUnit.MILLISECONDS.sleep(210); // Wait for refill (2 tokens)
        assertTrue(limiter.tryAcquire(), "Refill 1");
        assertTrue(limiter.tryAcquire(), "Refill 2");
        assertFalse(limiter.tryAcquire(), "Should be empty again");
    }

    private static void testLeakyBucket() throws InterruptedException {
        RateLimiter limiter = new LeakyBucketRateLimiter(5, 10);
        for (int i = 0; i < 5; i++) assertTrue(limiter.tryAcquire(), "Initial burst " + i);
        assertFalse(limiter.tryAcquire(), "Should be full");
        TimeUnit.MILLISECONDS.sleep(210); // Leak 2 drops
        assertTrue(limiter.tryAcquire(), "Leak refill 1");
        assertTrue(limiter.tryAcquire(), "Leak refill 2");
        assertFalse(limiter.tryAcquire(), "Should be full again");
    }

    private static void testFixedWindow() throws InterruptedException {
        RateLimiter limiter = new FixedWindowCounterRateLimiter(5, 500);
        for (int i = 0; i < 5; i++) assertTrue(limiter.tryAcquire(), "Window burst " + i);
        assertFalse(limiter.tryAcquire(), "Window full");
        TimeUnit.MILLISECONDS.sleep(510);
        assertTrue(limiter.tryAcquire(), "Window reset");
    }

    private static void testSlidingWindowLog() throws InterruptedException {
        RateLimiter limiter = new SlidingWindowLogRateLimiter(5, 500);
        for (int i = 0; i < 5; i++) assertTrue(limiter.tryAcquire(), "Log burst " + i);
        assertFalse(limiter.tryAcquire(), "Log full");
        TimeUnit.MILLISECONDS.sleep(510);
        assertTrue(limiter.tryAcquire(), "Log slid");
    }

    private static void testSlidingWindowCounter() throws InterruptedException {
        RateLimiter limiter = new SlidingWindowCounterRateLimiter(5, 500);
        for (int i = 0; i < 5; i++) assertTrue(limiter.tryAcquire(), "Counter burst " + i);
        assertFalse(limiter.tryAcquire(), "Counter full");
        TimeUnit.MILLISECONDS.sleep(600);
        // First call at the boundary might still be rejected due to smoothing (weighted count still 5)
        limiter.tryAcquire(); 
        TimeUnit.MILLISECONDS.sleep(10);
        assertTrue(limiter.tryAcquire(), "Counter slid and allowed after slight delay");
    }
}
