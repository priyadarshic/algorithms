package com.practice.ratelimiter;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Sliding Window Log Algorithm implementation.
 * 
 * Logic:
 * 1. Time Log: Keep a timestamped log of each request.
 * 2. Window Filter: When a new request arrives, remove all timestamps older than the window (e.g., last 1 minute).
 * 3. Limit Check: If current log size < limit, allow request and add current timestamp to log.
 * 
 * Pros: 
 * - Very accurate. Handles window boundary problems perfectly.
 * 
 * Cons:
 * - High Memory usage: Stores a timestamp for every request in the window.
 */
public class SlidingWindowLogRateLimiter implements RateLimiter {
    private final int limit;
    private final long windowSizeMillis;
    private final Queue<Long> requestQueue;

    public SlidingWindowLogRateLimiter(int limit, long windowSizeMillis) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
        this.requestQueue = new LinkedList<>();
    }

    @Override
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        long windowStart = now - windowSizeMillis;

        // Remove timestamps older than the current window
        while (!requestQueue.isEmpty() && requestQueue.peek() < windowStart) {
            requestQueue.poll();
        }

        if (requestQueue.size() < limit) {
            requestQueue.add(now);
            return true;
        }
        return false;
    }
}
