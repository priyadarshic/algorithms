package com.practice.ratelimiter;

/**
 * Interface for various Rate Limiting algorithms.
 */
public interface RateLimiter {
    /**
     * Attempts to acquire a permit for a request.
     * 
     * @return true if the request is allowed, false otherwise.
     */
    boolean tryAcquire();
}
