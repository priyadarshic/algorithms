package com.practice.references;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;

/**
 * PhantomReference is enqueued by the garbage collector after it determines 
 * that its referent is phantom-reachable.
 * 
 * Unlike weak and soft references, phantom references are not automatically 
 * cleared by the garbage collector as they are enqueued. 
 * 
 * phantomReference.get() always returns null.
 */
public class PhantomReferenceExample {
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws InterruptedException {
        // Create an object and a ReferenceQueue
        Object obj = new Object();
        ReferenceQueue<Object> queue = new ReferenceQueue<>();
        
        // Create a PhantomReference
        PhantomReference<Object> phantomRef = new PhantomReference<>(obj, queue);
        
        // phantomRef.get() always returns null, even when the object is alive!
        System.out.println("phantomRef.get() result: " + phantomRef.get());
        
        System.out.println("Checking if enqueued immediately: " + phantomRef.isEnqueued());
        
        // Null the strong reference so the object becomes phantom-reachable
        obj = null;
        
        System.out.println("Triggering GC...");
        System.gc();
        
        // Give GC some time to work and enqueue the reference
        System.out.println("Waiting for reference to be enqueued...");
        boolean enqueued = false;
        for (int i = 0; i < 10; i++) {
            if (phantomRef.isEnqueued()) {
                enqueued = true;
                break;
            }
            Thread.sleep(100);
        }
        
        if (enqueued) {
            System.out.println("Reference has been enqueued in the ReferenceQueue!");
            
            // We can now retrieve it from the queue
            Reference<?> removedRef = queue.poll();
            if (removedRef != null) {
                System.out.println("Successfully polled the reference from the queue.");
                System.out.println("removedRef.get() result: " + removedRef.get());
                // Usually, you would perform cleanup here (like closing resources)
            }
        } else {
            System.out.println("Reference not enqueued yet. (Note: Phantom behavior can be JVM dependent)");
        }
    }
}
