package com.practice.ratelimiter;

/**
 * Sliding Window Counter Algorithm implementation.
 * 
 * Logic:
 * 1. Combination: Uses multiple fixed windows to approximate a sliding window.
 * 2. Formula: count = countPrevWindow * (1 - overlapWeight) + countCurrentWindow
 * 3. Accuracy: More accurate than fixed window, less memory-intensive than sliding window log.
 * 
 * Pros: 
 * - Memory efficient.
 * - Smooths out window boundary spikes.
 * 
 * Cons:
 * - Still an approximation (though very good).
 */
public class SlidingWindowCounterRateLimiter implements RateLimiter {
    private final int limit;
    private final long windowSizeMillis;
    private int currentWindowCounter;
    private int previousWindowCounter;
    private long currentWindowStartTimestamp;

    public SlidingWindowCounterRateLimiter(int limit, long windowSizeMillis) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
        this.currentWindowCounter = 0;
        this.previousWindowCounter = 0;
        this.currentWindowStartTimestamp = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        
        // Handle window transitions
        if (now - currentWindowStartTimestamp >= windowSizeMillis) {
            previousWindowCounter = currentWindowCounter;
            currentWindowCounter = 0;
            currentWindowStartTimestamp = now;
        }

        // Calculate the weighted count from the previous window and current window
        double overlapWeight = (double) (now - currentWindowStartTimestamp) / windowSizeMillis;
        double estimatedCount = previousWindowCounter * (1 - overlapWeight) + currentWindowCounter;

        if (estimatedCount < limit) {
            currentWindowCounter++;
            return true;
        }
        return false;
    }
}
