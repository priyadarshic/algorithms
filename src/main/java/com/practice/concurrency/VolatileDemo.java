package com.practice.concurrency;

/**
 * VolatileDemo demonstrates the visibility guarantee of the volatile keyword.
 */
public class VolatileDemo {

    // With volatile, changes made by one thread are immediately visible to others.
    // Try removing 'volatile' to see how the reader thread might hang (depending on
    // JVM/OS).
    private static volatile boolean sayHello = false;
    private static long count = 0;

    public static void main(String[] args) throws InterruptedException {

        // Thread 1: Reader Thread
        // This thread continuously checks the status of the 'sayHello' flag.
        Thread readerThread = new Thread(() -> {
            System.out.println("Reader Thread: Waiting for flag to change...");
            while (!sayHello) {
                // Without volatile, the CPU might cache sayHello = false
                // and never check the main memory again, leading to an infinite loop.
                count++;
            }
            System.out.println("Reader Thread: Flag changed! Loop iterations: " + count);
            System.out.println("Reader Thread: Hello World!");
        });

        // Thread 2: Writer Thread
        // This thread updates the 'sayHello' flag after a short delay.
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(10); // Sleep for 10 ms
                System.out.println("Writer Thread: Changing flag to true...");
                sayHello = true;
                System.out.println("Current Count: " + count);
                System.out.println("Writer Thread: Flag change complete.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        readerThread.start();
        writerThread.start();

        readerThread.join();
        writerThread.join();
        System.out.println("Final Count: " + count);
        System.out.println("Main: Demo completed successfully.");
    }
}
