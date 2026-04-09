/*
 * Copyright (c) 2022.
 * All Intellectual Property Rights to this File/Digital Product belong to the
 * @Author PriyadarshiChaudhuri.
 * Contact priyadarshi.c@gmail.com for enquiries.
 * This File maybe used for Non-commercial purpose only with Credits and link to GitHub repository.
 */

package com.practice.concurrency.locking;

import multithreading.ThreadUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLocking {

    private static final Map<String, String> cache = new HashMap<>(); // Standard Map since we use ReadWriteLock
    private static final ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void put(String key, String value) {
        lock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName());
            ThreadUtil.sleep(100);
            cache.put(key, value);
            System.out.println("Put " + key + ": " + value);
        } finally {
            lock.writeLock().unlock();
        }

    }

    public static String get(String key) {
        lock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName());
            ThreadUtil.sleep(500);
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }

    private static void putThread() {
        for (int i = 0; i < 10; i++) {
            String key = "key" + i;
            String value = "value" + i;
            executorService.submit(() -> put(key, value));
        }
    }

    private static void getThread() {
        for (int i = 0; i < 10; i++) {
            String key = "key" + i;
            String value = "value" + i;
            executorService.submit(() -> {
                String result = get(key);
                System.out.println("Get " + key + ": " + result);
            });
        }
    }

    public static void main(String[] args) {
        System.out.println("\n--- ReadWriteLocking Demo Starting ---");

        // putThread();
        // getThread();

        executorService.submit(() -> putThread());
        executorService.submit(() -> getThread());

        // executorService.shutdown();
        try {
            if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        System.out.println("--- ReadWriteLocking Demo Finished ---\n");
    }

}
