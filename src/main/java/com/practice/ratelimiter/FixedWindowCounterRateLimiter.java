package com.practice.ratelimiter;


/**
 * Fixed Window Counter Algorithm implementation.
 * 
 * Logic:
 * 1. Time Window: Divide time into fixed segments (e.g., 1-minute windows).
 * 2. Counter: Each window has a counter.
 * 3. Limit Check: If current window counter < limit, allow request and increment counter.
 * 4. Reset: When moving to a new window, the counter is reset to 0.
 * 
 * Pros: 
 * - Memory efficient (only stores a counter and timestamp).
 * 
 * Cons:
 * - Traffic peaks at window boundaries. (2x the limit can pass if requests occur at the end of window N and start of N+1).
 */
public class FixedWindowCounterRateLimiter implements RateLimiter {
    private final int limit;
    private final long windowSizeMillis;
    private int counter;
    private long windowStartTimestamp;

    public FixedWindowCounterRateLimiter(int limit, long windowSizeMillis) {
        this.limit = limit;
        this.windowSizeMillis = windowSizeMillis;
        this.counter = 0;
        this.windowStartTimestamp = System.currentTimeMillis();
    }

    @Override
    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        
        // If the current time is outside the current window, start a new one
        if (now - windowStartTimestamp >= windowSizeMillis) {
            windowStartTimestamp = now;
            counter = 0;
        }

        if (counter < limit) {
            counter++;
            return true;
        }
        return false;
    }
}
