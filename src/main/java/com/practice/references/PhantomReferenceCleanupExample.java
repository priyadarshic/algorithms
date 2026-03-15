package com.practice.references;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

/**
 * Practical example of using PhantomReference for resource cleanup.
 * 
 * Since PhantomReference.get() always returns null, we subclass it 
 * to store the metadata needed for cleaning up the resource.
 */
public class PhantomReferenceCleanupExample {
    
    // A simulated resource that requires manual cleanup (e.g., a database connection or native memory)
    static class LargeResource {
        private final String resourceName;
        LargeResource(String name) { this.resourceName = name; }
        @Override
        public String toString() { return resourceName; }
    }

    // Custom PhantomReference to store cleanup metadata
    static class ResourceReference extends PhantomReference<LargeResource> {
        private final String resourceName;

        public ResourceReference(LargeResource referent, ReferenceQueue<? super LargeResource> queue) {
            super(referent, queue);
            this.resourceName = referent.resourceName;
        }

        public void cleanup() {
            System.out.println("[Cleanup] Releasing resources for: " + resourceName);
            // In a real app, you might close a File, Database Connection, etc.
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReferenceQueue<LargeResource> queue = new ReferenceQueue<>();
        List<ResourceReference> references = new ArrayList<>();
        
        // 1. Create a resource and its phantom reference
        LargeResource resource = new LargeResource("DatabaseConnection_v1");
        references.add(new ResourceReference(resource, queue));
        
        System.out.println("Resource created: " + resource);
        
        // 2. Make the resource phantom-reachable
        resource = null;
        
        // 3. Trigger GC multiple times to ensure the object is collected
        System.out.println("Triggering GC...");
        System.gc();
        Thread.sleep(500);
        
        // 4. Check the queue for references ready for cleanup
        System.out.println("Checking ReferenceQueue...");
        Reference<? extends LargeResource> refInQueue = queue.poll();
        
        if (refInQueue instanceof ResourceReference) {
            ResourceReference customRef = (ResourceReference) refInQueue;
            customRef.cleanup(); // Perform the actual cleanup
            customRef.clear();   // Recommended to clear the reference once done
        } else {
            System.out.println("Queue is empty. Object might not be collected yet.");
        }
        
        System.out.println("Done.");
    }
}
