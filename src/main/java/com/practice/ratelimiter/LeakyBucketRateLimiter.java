package com.practice.ratelimiter;

/**
 * Leaky Bucket Algorithm implementation.
 * 
 * Logic:
 * 1. Request Arrival: When a request arrives, it is added to the "bucket".
 * 2. Overflow Check: If the bucket is full, the request is discarded.
 * 3. Constant Leak Rate: Water "leaks" out of the bucket at a constant rate (e.g., 5 requests per second).
 * 4. Smoothing: This ensures that requests are processed at a stable, uniform rate, regardless of spikes.
 * 
 * Pros:
 * - Perfectly smooths out traffic.
 * - Prevents sudden bursts from overwhelming the system.
 */
public class LeakyBucketRateLimiter implements RateLimiter {
    private final long capacity;
    private final long leakRate; // requests per second
    private double currentWater;
    private long lastLeakTimestamp;

    public LeakyBucketRateLimiter(long capacity, long leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.currentWater = 0;
        this.lastLeakTimestamp = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean tryAcquire() {
        leak();

        if (currentWater + 1 <= capacity) {
            currentWater += 1;
            return true;
        }
        return false;
    }

    private void leak() {
        long now = System.currentTimeMillis();
        long timeElapsed = now - lastLeakTimestamp;
        
        // Leak "water" based on elapsed time and leak rate
        double leakedAmount = (timeElapsed * (double) leakRate) / 1000.0;

        if (leakedAmount > 0) {
            currentWater = Math.max(0, currentWater - leakedAmount);
            lastLeakTimestamp = now;
        }
    }
}
