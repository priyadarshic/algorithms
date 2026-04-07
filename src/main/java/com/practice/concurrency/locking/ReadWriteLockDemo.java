package com.practice.concurrency.locking;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Demonstrates ReentrantReadWriteLock, which separates the lock into a 
 * Read lock and a Write lock to optimize for read-heavy workloads.
 * 
 * Key Features:
 * 1. Read Lock (Shared): Multiple threads can hold the read lock at the same time,
 *    provided the write lock is not held. This allows for concurrent reading.
 * 2. Write Lock (Exclusive): Only one thread can hold the write lock at a time.
 *    It blocks all other readers and writers to ensure consistent updates.
 * 3. Priority: Depending on implementation, writers usually have priority over
 *    new readers once a write is requested, to prevent "writer starvation."
 * 4. Reentrancy: Both read and write locks are reentrant.
 * 5. Downgrading: You can downgrade from a write lock to a read lock, but you 
 *    cannot upgrade from a read lock to a write lock without first releasing the read lock.
 */
public class ReadWriteLockDemo {

    private final Map<String, String> cache = new HashMap<>();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public void put(String key, String value) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is WRITING " + key + " -> " + value);
            Thread.sleep(200); // Simulate write delay
            cache.put(key, value);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public String get(String key) {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + " is READING " + key + ": " + cache.get(key));
            Thread.sleep(400); // Simulate read delay
            return cache.get(key);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return null;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void runDemo() {
        System.out.println("\n--- ReadWriteLock Demo Starting (Shared Reads, Exclusive Writes) ---");
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 1 Writer
        executor.submit(() -> put("user1", "John Doe"));

        // Multiple Readers (should be concurrent)
        for (int i = 0; i < 5; i++) {
            String user = "user" + i;
            String name = "John Doe" + i;
            executor.submit(() -> put(user, name));
            executor.submit(() -> get(user));
        }

        executor.submit(() -> put("user2", "Jane Smith"));
        executor.submit(() -> get("user2"));

        // for (int i = 0; i < 5; i++) {
        // String user = "user" + i;
        // String name = "John Doe" + i;
        // executor.submit(() -> put(user, name));
        // }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("--- ReadWriteLock Demo Finished ---\n");
    }

    public static void main(String[] args) {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();
        demo.runDemo();
    }
}
