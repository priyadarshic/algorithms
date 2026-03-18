package com.practice.datastructures;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.Iterator;

/**
 * ConcurrentHashMapDemo demonstrates the use of ConcurrentHashMap in a multi-threaded environment.
 * It shows how ConcurrentHashMap allows concurrent modifications while iterating, 
 * whereas a regular HashMap would throw a ConcurrentModificationException.
 */
public class ConcurrentHashMapDemo {

    public static void main(String[] args) {
        // Create a ConcurrentHashMap
        Map<String, Integer> map = new ConcurrentHashMap<>();

        // Initialize with some data
        map.put("Apple", 10);
        map.put("Banana", 20);
        map.put("Cherry", 30);

        System.out.println("Initial Map: " + map);

        // Thread 1: Iterating over the map
        Thread readerThread = new Thread(() -> {
            System.out.println("Reader Thread: Starting iteration...");
            try {
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    System.out.println("Reader Thread: Read " + key + " = " + map.get(key));
                    
                    // Artificial delay to allow writer thread to modify
                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Reader Thread: Iteration complete.");
        });

        // Thread 2: Modifying the map concurrently
        Thread writerThread = new Thread(() -> {
            try {
                // Wait a bit before starting modifications
                Thread.sleep(50);
                
                System.out.println("Writer Thread: Adding Date...");
                map.put("Date", 40);
                
                Thread.sleep(100);
                System.out.println("Writer Thread: Removing Banana...");
                map.remove("Banana");
                
                Thread.sleep(100);
                System.out.println("Writer Thread: Updating Apple...");
                map.put("Apple", 100);
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Writer Thread: Modifications complete.");
        });

        // Start both threads
        readerThread.start();
        writerThread.start();

        // Wait for threads to finish
        try {
            readerThread.join();
            writerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Final Map: " + map);
        System.out.println("Demo finished successfully without ConcurrentModificationException!");
    }
}
