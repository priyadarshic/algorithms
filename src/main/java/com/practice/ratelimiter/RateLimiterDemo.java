package com.practice.ratelimiter;

import java.util.concurrent.TimeUnit;

/**
 * Demo application to showcase all the implemented Rate Limiting algorithms.
 */
public class RateLimiterDemo {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Rate Limiter Algorithms Demo ===");

        // 1. Token Bucket: capacity 5, refill rate 2 tokens/sec
        testLimiter("Token Bucket", new TokenBucketRateLimiter(5, 2));

        // 2. Leaky Bucket: capacity 5, leak rate 2 requests/sec
        testLimiter("Leaky Bucket", new LeakyBucketRateLimiter(5, 2));

        // 3. Fixed Window: limit 5, window 1 second
        testLimiter("Fixed Window", new FixedWindowCounterRateLimiter(5, 1000));

        // 4. Sliding Window Log: limit 5, window 1 second
        testLimiter("Sliding Window Log", new SlidingWindowLogRateLimiter(5, 1000));

        // 5. Sliding Window Counter: limit 5, window 1 second
        testLimiter("Sliding Window Counter", new SlidingWindowCounterRateLimiter(5, 1000));
    }

    private static void testLimiter(String name, RateLimiter limiter) throws InterruptedException {
        System.out.println("\nTesting: " + name);
        System.out.println("Trying 10 requests immediately...");

        for (int i = 1; i <= 10; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.print(allowed ? " [OK] " : " [Refused] ");
            if (i == 10) System.out.println();
        }

        System.out.println("Waiting 500ms and trying 3 more requests...");
        TimeUnit.MILLISECONDS.sleep(500);
        for (int i = 1; i <= 3; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.print(allowed ? " [OK] " : " [Refused] ");
            if (i == 3) System.out.println();
        }

        System.out.println("Waiting 1s and trying 5 more requests...");
        TimeUnit.MILLISECONDS.sleep(1000);
        for (int i = 1; i <= 5; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.print(allowed ? " [OK] " : " [Refused] ");
            if (i == 5) System.out.println();
        }
    }
}
