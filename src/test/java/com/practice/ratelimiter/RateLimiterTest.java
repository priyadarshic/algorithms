package com.practice.ratelimiter;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.TimeUnit;

public class RateLimiterTest {

    @Test
    public void testTokenBucket() throws InterruptedException {
        RateLimiter limiter = new TokenBucketRateLimiter(5, 10); // 5 capacity, 10/sec refill
        
        // Initial 5 should be accepted
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire(), "Request " + i + " should be allowed");
        }
        
        // 6th should be rejected
        assertFalse(limiter.tryAcquire(), "Request 6 should be rejected");
        
        // Wait 200ms (should refill 2 tokens)
        TimeUnit.MILLISECONDS.sleep(200);
        assertTrue(limiter.tryAcquire(), "Refilled request 1 should be allowed");
        assertTrue(limiter.tryAcquire(), "Refilled request 2 should be allowed");
        assertFalse(limiter.tryAcquire(), "Refilled request 3 should be rejected");
    }

    @Test
    public void testLeakyBucket() throws InterruptedException {
        RateLimiter limiter = new LeakyBucketRateLimiter(5, 10); // 5 capacity, 10/sec leak
        
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire());
        }
        assertFalse(limiter.tryAcquire());
        
        TimeUnit.MILLISECONDS.sleep(200);
        assertTrue(limiter.tryAcquire());
        assertTrue(limiter.tryAcquire());
        assertFalse(limiter.tryAcquire());
    }

    @Test
    public void testFixedWindow() throws InterruptedException {
        RateLimiter limiter = new FixedWindowCounterRateLimiter(5, 500); // 5 per 500ms
        
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire());
        }
        assertFalse(limiter.tryAcquire());
        
        TimeUnit.MILLISECONDS.sleep(600);
        assertTrue(limiter.tryAcquire(), "Should reset after window expired");
    }

    @Test
    public void testSlidingWindowLog() throws InterruptedException {
        RateLimiter limiter = new SlidingWindowLogRateLimiter(5, 500);
        
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire());
        }
        assertFalse(limiter.tryAcquire());
        
        TimeUnit.MILLISECONDS.sleep(600);
        assertTrue(limiter.tryAcquire());
    }

    @Test
    public void testSlidingWindowCounter() throws InterruptedException {
        RateLimiter limiter = new SlidingWindowCounterRateLimiter(5, 500);
        
        for (int i = 0; i < 5; i++) {
            assertTrue(limiter.tryAcquire());
        }
        // At 5 requests in current window, it should deny
        assertFalse(limiter.tryAcquire());
        
        // Halfway through next window, some of previous window still counts
        TimeUnit.MILLISECONDS.sleep(600);
        assertTrue(limiter.tryAcquire(), "Should allow in new window");
    }
}
