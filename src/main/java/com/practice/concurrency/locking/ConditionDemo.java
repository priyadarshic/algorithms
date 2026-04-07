package com.practice.concurrency.locking;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Demonstrates the use of Condition objects, which provide a means 
 * for one thread to suspend execution (wait) until notified by another 
 * thread that some state condition may now be true.
 */
public class ConditionDemo {

    private final Queue<Integer> buffer = new LinkedList<>();
    private final int CAPACITY = 2;
    private final Lock lock = new ReentrantLock();
    private final Condition notFull = lock.newCondition();
    private final Condition notEmpty = lock.newCondition();

    public void produce(int value) throws InterruptedException {
        lock.lock();
        try {
            while (buffer.size() == CAPACITY) {
                System.out.println("Buffer full. " + Thread.currentThread().getName() + " waits...");
                notFull.await(); // Releases lock and waits
            }
            buffer.add(value);
            System.out.println(Thread.currentThread().getName() + " PRODUCED: " + value);
            notEmpty.signalAll(); // Wake up any threads waiting for it to be not empty
        } finally {
            lock.unlock();
        }
    }

    public void consume() throws InterruptedException {
        lock.lock();
        try {
            while (buffer.isEmpty()) {
                System.out.println("Buffer empty. " + Thread.currentThread().getName() + " waits...");
                notEmpty.await(); // Releases lock and waits
            }
            int value = buffer.poll();
            System.out.println(Thread.currentThread().getName() + " CONSUMED: " + value);
            notFull.signalAll(); // Wake up any threads waiting for it to be not full
        } finally {
            lock.unlock();
        }
    }

    public void runDemo() {
        System.out.println("\n--- Condition Demo Starting (Producer-Consumer Coordination) ---");
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Producer
        executor.submit(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    produce(i);
                    Thread.sleep(200);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Consumers
        for (int i = 0; i < 2; i++) {
            executor.submit(() -> {
                try {
                    while (!Thread.currentThread().isInterrupted()) {
                        consume();
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
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
        System.out.println("--- Condition Demo Finished ---\n");
    }
}
