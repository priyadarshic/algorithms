package com.practice.references;

import java.lang.ref.WeakReference;

public class WeakReferenceExample {
    public static void main(String[] args) {
        // Create a strong reference to an object
        StringBuilder builder = new StringBuilder("I am a strong object");
        
        // Create a weak reference to that object
        WeakReference<StringBuilder> weakRef = new WeakReference<>(builder);
        
        System.out.println("Before nulling strong reference: " + weakRef.get());
        
        // Null the strong reference - now the object is ONLY weakly reachable
        builder = null;
        
        System.out.println("After nulling strong reference: " + weakRef.get());
        
        System.out.println("Running Garbage Collector...");
        System.gc();
        
        // After GC, the weak reference should be cleared
        if (weakRef.get() == null) {
            System.out.println("Object has been garbage collected! (WeakRef is null)");
        } else {
            System.out.println("Object is still alive: " + weakRef.get());
        }
    }
}
