package com.practice.datastructures;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LinkedHashMapDemo illustrates the internal ordering of LinkedHashMap.
 * It demonstrates both Insertion-order and Access-order modes.
 */
public class LinkedHashMapDemo {

    public static void main(String[] args) {
        // 1. Insertion Order (Default)
        System.out.println("--- Insertion Order (Default) ---");
        Map<String, Integer> insertionOrderMap = new LinkedHashMap<>();
        insertionOrderMap.put("One", 1);
        insertionOrderMap.put("Two", 2);
        insertionOrderMap.put("Three", 3);

        System.out.println("Map elements: " + insertionOrderMap);
        // Expected: {One=1, Two=2, Three=3}

        // Even if we access "One", the order remains the same
        insertionOrderMap.get("One");

        System.out.println("Map elements after access One: " + insertionOrderMap);
        // Expected: {One=1, Two=2, Three=3}

        // 2. Access Order
        System.out.println("\n--- Access Order ---");
        // Constructor: LinkedHashMap(initialCapacity, loadFactor, accessOrder)
        Map<String, Integer> accessOrderMap = new LinkedHashMap<>(16, 0.75f, true);
        accessOrderMap.put("Apple", 100);
        accessOrderMap.put("Banana", 200);
        accessOrderMap.put("Cherry", 300);

        System.out.println("Before access: " + accessOrderMap);

        // Accessing "Apple" moves it to the end (most recently used)
        accessOrderMap.get("Apple");
        System.out.println("After accessing 'Apple': " + accessOrderMap);

        // Accessing "Banana" moves it to the end
        accessOrderMap.get("Banana");
        System.out.println("After accessing 'Banana': " + accessOrderMap);
        // Expected: {Cherry=300, Apple=100, Banana=200}

        // 3. Illustrating MRU (Most Recently Used) behavior for LRU Cache
        System.out.println("\n--- LRU Cache behavior ---");
        accessOrderMap.put("Date", 400);
        System.out.println("After adding 'Date': " + accessOrderMap);
    }
}
