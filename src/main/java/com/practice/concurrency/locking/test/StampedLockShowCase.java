/*
 * Copyright (c) 2026.
 * All Intellectual Property Rights to this File/Digital Product belong to the
 * @Author PriyadarshiChaudhuri.
 * Contact priyadarshi.c@gmail.com for enquiries.
 * This File maybe used for Non-commercial purpose only with Credits and link to GitHub repository.
 */

package com.practice.concurrency.locking.test;

import multithreading.ThreadUtil;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class StampedLockShowCase {

    private static final StampedLock stampedLock  = new StampedLock();
    private static final PriorityBlockingQueue<Integer> queue = new PriorityBlockingQueue<>();


    private static void readQueue() {
        // Shared Read Lock - allows multiple simultaneous readers
        long stamp = stampedLock.readLock();
        try {
            ThreadUtil.sleep(100);
            // PriorityBlockingQueue handles internal thread safety
            Integer value = queue.poll();
            if (value != null) {
                System.out.println(Thread.currentThread().getName() + ": READ LOCK: \t" + value);
            }
        } finally {
            stampedLock.unlockRead(stamp);
            System.out.println(Thread.currentThread().getName() + ": UNLOCK Read: " + stamp);
        }
    }

    private static void writeQueue(int value) {
        // Exclusive Write Lock - blocks all readers and other writers
        long stamp = stampedLock.writeLock();
        try {
            System.out.println(Thread.currentThread().getName() + ": WRITE LOCK Acquired: " + stamp);
            ThreadUtil.sleep(200); // Simulate work
            queue.offer(value);
            System.out.println(Thread.currentThread().getName() + ": WRITE: \t\t" + value);
        } finally {
            stampedLock.unlockWrite(stamp);
            System.out.println(Thread.currentThread().getName() + ": UNLOCK Write: " + stamp);
        }
    }

    public static void runDemo() {

        System.out.println("--- StampedLock Demo Starting ---\n");
        ExecutorService executor = Executors.newFixedThreadPool(4);


        for (int i = 0; i < 10; i++) {
            int finalVal = i;
            executor.submit(() -> writeQueue(finalVal));
        }

        for (int i = 0; i < 15; i++) {
            executor.submit(StampedLockShowCase::readQueue);
        }

        try{
            if(!executor.awaitTermination(20, TimeUnit.SECONDS))
            {
                System.out.println("queue current size : " + queue.size());
                executor.shutdownNow();
            }
        }
        catch (InterruptedException e){
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        System.out.println("\n\n--- StampedLock Demo Finished ---\n");
    }

    public static void main(String[] args) {
        runDemo();
    }

}
