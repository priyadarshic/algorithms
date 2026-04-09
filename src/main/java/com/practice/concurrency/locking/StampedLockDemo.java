package com.practice.concurrency.locking;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

/**
 * Demonstrates StampedLock, introduced in Java 8.
 * 
 * StampedLock is a more sophisticated and often faster alternative to ReadWriteLock.
 * It uses 'stamps' (long values) to represent the state of the lock.
 * 
 * Key Features:
 * 1. Optimistic Reading: This is the most powerful feature. It allows a thread to
 *    read data WITHOUT blocking any writers. After reading, the thread 'validates'
 *    the stamp. If a write occurred during the read, the thread can then upgrade
 *    to a traditional (blocking) read lock.
 * 
 * 2. Three Lock Modes:
 *    - Writing: Exclusive access, just like ReentrantLock.
 *    - Reading (Heavy): Shared access, blocks writers, used for consistent read-only tasks.
 *    - Optimistic Reading: Non-blocking shared access, does not block writers.
 * 
 * 3. Not Reentrant: Unlike ReentrantLock, StampedLock is NOT reentrant. A thread
 *    holding a lock will deadlock if it tries to acquire it again.
 * 
 * 4. Conversion methods: Provides methods to conditionally 'upgrade' or 'downgrade'
 *    stamps between modes.
 */
public class StampedLockDemo {

    private double x, y;
    private final StampedLock lock = new StampedLock();

    public void move(double deltaX, double deltaY) {
        long stamp = lock.writeLock(); // Exclusive lock
        try {
            System.out.println(Thread.currentThread().getName() + " is WRITING (exclusive)");
            x += deltaX;
            y += deltaY;
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.unlockWrite(stamp);
        }
    }

    // A read-only method using optimistic reading
    public double distanceFromOrigin() {
        long stamp = lock.tryOptimisticRead(); // Non-blocking read
        double curX = x, curY = y;
        
        System.out.println(Thread.currentThread().getName() + " is OPTIMISTIC READING");
        
        // Check if a write lock was acquired since we got the stamp
        if (!lock.validate(stamp)) {
            System.out.println(Thread.currentThread().getName() + " data changed; upgrading to heavy read lock.");
            stamp = lock.readLock(); // Block and get a read lock
            try {
                curX = x;
                curY = y;
            } finally {
                lock.unlockRead(stamp);
            }
        }
        return Math.sqrt(curX * curX + curY * curY);
    }

    public void runDemo() {
        System.out.println("\n--- StampedLock Demo Starting (Optimistic Reads) ---");
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Multiple Readers (Optimistic)
        for (int i = 0; i < 9; i++) {
            executor.submit(() -> {
                double dist = distanceFromOrigin();
                System.out.println(Thread.currentThread().getName() + " distance: " + dist);
            });
        }

        // 1 Writer (Will invalidate optimistic reads)
        executor.submit(() -> move(10, 20));

        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("--- StampedLock Demo Finished ---\n");
    }
    public static void main(String[] args) {
        StampedLockDemo demo = new StampedLockDemo();
        demo.runDemo();
    }
}
