package com.practice.prodcon;

import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int limit;
    private final int poisonPill;

    public Producer(BlockingQueue<Integer> queue, int limit, int poisonPill) {
        this.queue = queue;
        this.limit = limit;
        this.poisonPill = poisonPill;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i <= limit; i++) {
                System.out.println("[Producer] Producing: " + i);
                queue.put(i);
                Thread.sleep(800); // Simulate some work
            }
            System.out.println("[Producer] Finished producing. Sending poison pill.");
            queue.put(poisonPill);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[Producer] Thread interrupted");
        }
    }
}
