package com.practice.prodcon;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ProducerConsumerController {
    public static void main(String[] args) {
        int capacity = 1;
        int limit = 100;
        int poisonPill = -1;

        BlockingQueue<Integer> sharedQueue = new ArrayBlockingQueue<>(capacity);

        Producer producer = new Producer(sharedQueue, limit, poisonPill);
        Consumer consumer = new Consumer(sharedQueue, poisonPill);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        System.out.println("[Main] Starting Producer and Consumer threads...");
        producerThread.start();
        consumerThread.start();

        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[Main] Main thread interrupted");
        }

        System.out.println("[Main] Producer and Consumer have finished their tasks.");
    }
}
