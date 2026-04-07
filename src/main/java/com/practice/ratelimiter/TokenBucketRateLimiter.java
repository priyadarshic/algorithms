package com.practice.ratelimiter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Token Bucket Algorithm implementation.
 * 
 * Logic:
 * 1. Tokens addition: Tokens are added to the "bucket" at a fixed rate (e.g., 10 tokens per second).
 * 2. Bucket Capacity: The bucket has a maximum capacity. If the bucket is full, newly added tokens are discarded.
 * 3. Token Consumption: Each request consumes one token. If there are tokens available, the request proceeds.
 * 4. Rejection: If the bucket is empty, the request is rejected (rate limited).
 * 
 * Pros: 
 * - Handles bursts of traffic (up to the bucket capacity).
 * - Simple and efficient.
 */
public class TokenBucketRateLimiter implements RateLimiter {
    private final long capacity;
    private final long refillRate; // tokens per second
    private final AtomicLong availableTokens;
    private long lastRefillTimestamp;

    public TokenBucketRateLimiter(long capacity, long refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.availableTokens = new AtomicLong(capacity);
        this.lastRefillTimestamp = System.nanoTime();
    }

    @Override
    public synchronized boolean tryAcquire() {
        refill();

        if (availableTokens.get() > 0) {
            availableTokens.decrementAndGet();
            return true;
        }
        return false;
    }

    private void refill() {
        long now = System.nanoTime();
        long timeElapsed = now - lastRefillTimestamp;
        
        // Calculate tokens to be added based on elapsed time
        // Refill rate is tokens per second, so we convert nano to seconds
        long tokensToAdd = (timeElapsed * refillRate) / 1_000_000_000L;

        if (tokensToAdd > 0) {
            long newTokens = Math.min(capacity, availableTokens.get() + tokensToAdd);
            availableTokens.set(newTokens);
            lastRefillTimestamp = now;
        }
    }
}
