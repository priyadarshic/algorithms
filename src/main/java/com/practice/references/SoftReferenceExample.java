package com.practice.references;

import java.lang.ref.SoftReference;

/**
 * all soft references to softly-reachable objects are guaranteed 
 * to have been cleared before the virtual machine throws an OutOfMemoryError
 */
public class SoftReferenceExample {
    public static void main(String[] args) {
        // Create a strong reference
        String data = "I am a soft object";
        
        // Create a soft reference
        SoftReference<String> softRef = new SoftReference<>(data);
        
        System.out.println("Before clear and GC: " + softRef.get());
        
        data = null; // Object is now softly reachable
        
        System.out.println("Running GC...");
        System.gc();
        
        // Soft references are NOT usually cleared by standard GC unless memory is low
        System.out.println("After GC (likely still exists): " + softRef.get());
        
        System.out.println("\nNote: Soft references only get cleared when the JVM is about to run out of memory.");
    }
}
