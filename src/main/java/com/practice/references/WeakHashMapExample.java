package com.practice.references;

import java.util.Map;
import java.util.WeakHashMap;

public class WeakHashMapExample {
    public static void main(String[] args) throws InterruptedException {
        Map<Key, String> map = new WeakHashMap<>();
        
        Key strongKey = new Key("StrongKey");
        Key weakKey = new Key("WeakKey");
        
        map.put(strongKey, "This will stay");
        map.put(weakKey, "This will be removed");
        
        System.out.println("Map size: " + map.size());
        
        // Remove the strong reference to the 'WeakKey'
        weakKey = null;
        
        System.out.println("Triggering GC...");
        System.gc();
        
        // Sleep a bit to give GC/WeakHashMap time to process
        Thread.sleep(100);
        
        System.out.println("Map size after GC: " + map.size());
        System.out.println("Remaining items in map: " + map.keySet());
    }
    
    // Custom class to easily visualize keys
    static class Key {
        String name;
        Key(String name) { this.name = name; }
        @Override
        public String toString() { return name; }
    }
}
