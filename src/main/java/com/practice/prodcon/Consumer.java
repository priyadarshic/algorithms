package com.practice.prodcon;

import java.util.concurrent.BlockingQueue;

public class Consumer implements Runnable {
    private final BlockingQueue<Integer> queue;
    private final int poisonPill;

    public Consumer(BlockingQueue<Integer> queue, int poisonPill) {
        this.queue = queue;
        this.poisonPill = poisonPill;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Integer item = queue.take();
                if (item == poisonPill) {
                    System.out.println("[Consumer] Poison pill received. Stopping.");
                    break;
                }
                System.out.println("[Consumer] Consumed: " + item);
                Thread.sleep(2000); // Simulate processing work
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[Consumer] Thread interrupted");
        }
    }
}
