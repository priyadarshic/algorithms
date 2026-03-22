package com.practice.datastructures;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Demonstrates the internal working of HashMap, specifically collision handling.
 */
public class HashMapDemo {

    /**
     * A class with a controlled hashCode to demonstrate collisions.
     */
    static class Key {
        private final String name;
        private final int forcedHashCode;

        public Key(String name, int forcedHashCode) {
            this.name = name;
            this.forcedHashCode = forcedHashCode;
        }

        @Override
        public int hashCode() {
            // All keys with the same forcedHashCode will land in the same bucket
            return forcedHashCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return Objects.equals(name, key.name);
        }

        @Override
        public String toString() {
            return name + " (hash: " + forcedHashCode + ")";
        }
    }

    public static void main(String[] args) {
        System.out.println("--- HashMap Internal Behavior Demo ---");

        // 1. Initialize HashMap
        Map<Key, String> map = new HashMap<>();

        // 2. Create keys that will collide (all have forcedHashCode = 42)
        Key k1 = new Key("Key1", 42);
        Key k2 = new Key("Key2", 42);
        Key k3 = new Key("Key3", 42);

        System.out.println("Inserting keys that collide in bucket index associated with hash 42...");
        
        // 3. Put elements
        map.put(k1, "Value for K1");
        map.put(k2, "Value for K2");
        map.put(k3, "Value for K3");

        System.out.println("Map size: " + map.size());

        // 4. Retrieve elements
        System.out.println("\nRetrieving values:");
        System.out.println(k1 + " -> " + map.get(k1));
        System.out.println(k2 + " -> " + map.get(k2));
        System.out.println(k3 + " -> " + map.get(k3));

        // 5. Demonstrate how index is calculated internally (conceptual)
        // index = (n - 1) & (hash ^ (hash >>> 16))
        int n = 16; // default initial capacity
        int hash = 42;
        int spreadHash = hash ^ (hash >>> 16);
        int index = (n - 1) & spreadHash;
        System.out.println("\nInternal Index Calculation (Conceptual):");
        System.out.println("Capacity (n): " + n);
        System.out.println("Forced Hash: " + hash);
        System.out.println("Spread Hash: " + spreadHash);
        System.out.println("Bucket Index = (n-1) & spreadHash = " + index);

        System.out.println("\nAll these keys will reside in bucket index " + index + " as a LinkedList.");
        System.out.println("If we add more than 8 elements to this bucket, it transitions to a Red-Black Tree.");
    }
}
