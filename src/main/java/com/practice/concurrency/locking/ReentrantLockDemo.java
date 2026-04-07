package com.practice.concurrency.locking;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Demonstrates ReentrantLock, a mutual exclusion lock that serves as a 
 * more flexible alternative to synchronized methods and blocks.
 * 
 * Key Features:
 * 1. Mutual Exclusion: At most, one thread holds the lock at any given time.
 * 2. Reentrancy: A thread that already holds the lock can acquire it again 
 *    without blocking its own execution.
 * 3. Fairness Policy: Can optionally be created with 'fair' mode (true in constructor),
 *    which favors the longest-waiting thread.
 * 4. Interruptible Lock Acquisition: Provides methods to acquire the lock 
 *    while responding to thread interrupts.
 * 5. Unlock in Finally Block: Crucial to always release the lock in a finally 
 *    block to ensure it's freed even if an exception occurs in the critical section.
 */
public class ReentrantLockDemo {

    private final Lock lock = new ReentrantLock(true); // 'true' for fairness
    private int count = 0;

    public void increment() {
        lock.lock();
        try {
            // Critical section
            count++;
            System.out.println(Thread.currentThread().getName() + " incremented count to: " + count);
        } finally {
            // Always unlock in a finally block to ensure release even if an exception occurs
            lock.unlock();
        }
    }

    public void runDemo() {
        System.out.println("\n--- ReentrantLock Demo Starting ---");
        ExecutorService executor = Executors.newFixedThreadPool(3);

        for (int i = 0; i < 5; i++) {
            executor.submit(() -> increment());
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("Final shared count: " + count);
        System.out.println("--- ReentrantLock Demo Finished ---\n");
    }
}
