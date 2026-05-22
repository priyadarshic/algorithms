package com.practice.ratelimiter;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Demo application to showcase all the implemented Rate Limiting algorithms.
 */
public class RateLimiterDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Rate Limiter Algorithms Demo ===");

        Scanner input = new Scanner(System.in);
        System.out.println("Enter choice of algorithm to test:");
        System.out.println("1. Token Bucket");
        System.out.println("2. Leaky Bucket");
        System.out.println("3. Fixed Window");
        System.out.println("4. Sliding Window Log");
        System.out.println("5. Sliding Window Counter");

        int choice = input.nextInt();
        switch (choice) {
            case 1:
                testLimiter("Token Bucket", new TokenBucketRateLimiter(5, 2));
                break;
            case 2:
                testLimiter("Leaky Bucket", new LeakyBucketRateLimiter(5, 2));
                break;
            case 3:
                testLimiter("Fixed Window", new FixedWindowCounterRateLimiter(5, 1000));
                break;
            case 4:
                testLimiter("Sliding Window Log", new SlidingWindowLogRateLimiter(5, 1000));
                break;
            case 5:
                testLimiter("Sliding Window Counter", new SlidingWindowCounterRateLimiter(5, 1000));
                break;
            default:
                System.out.println("Testing all");
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
                break;
        }
        input.close();
    }

    private static void testLimiter(String name, RateLimiter limiter) throws InterruptedException {
        System.out.println("\nTesting: " + name);
        System.out.println("Trying 10 requests immediately...");

        for (int i = 1; i <= 10; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.print(allowed ? " [OK] " : " [Refused] ");
            // TimeUnit.MILLISECONDS.sleep(5);
            if (i == 10)
                System.out.println();
        }

        System.out.println("Waiting 200ms and trying 5 more requests...");
        TimeUnit.MILLISECONDS.sleep(200);
        for (int i = 1; i <= 5; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.print(allowed ? " [OK] " : " [Refused] ");
            if (i == 5)
                System.out.println();
        }

        System.out.println("Waiting 1s and trying 5 more requests...");
        TimeUnit.MILLISECONDS.sleep(1000);
        for (int i = 1; i <= 5; i++) {
            boolean allowed = limiter.tryAcquire();
            System.out.print(allowed ? " [OK] " : " [Refused] ");
            if (i == 5)
                System.out.println();
        }
    }
}
