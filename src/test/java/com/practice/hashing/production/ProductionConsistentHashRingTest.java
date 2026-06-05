package com.practice.hashing.production;

import org.junit.jupiter.api.Test;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class ProductionConsistentHashRingTest {

    @Test
    public void testNodeAdditionAndLookups() {
        ProductionConsistentHashRing ring = new ProductionConsistentHashRing();
        ring.addNode("node-A", 10);
        ring.addNode("node-B", 10);
        ring.addNode("node-C", 10);

        assertEquals(3, ring.getNodeCount());

        // Simple lookup
        Optional<String> node = ring.getNode("some-test-key");
        assertTrue(node.isPresent());
        assertTrue(Arrays.asList("node-A", "node-B", "node-C").contains(node.get()));
    }

    @Test
    public void testReplicaNodes() {
        ProductionConsistentHashRing ring = new ProductionConsistentHashRing();
        ring.addNode("node-A", 10);
        ring.addNode("node-B", 10);
        ring.addNode("node-C", 10);

        List<String> replicas = ring.getReplicaNodes("some-test-key", 2);
        assertEquals(2, replicas.size());
        assertNotEquals(replicas.get(0), replicas.get(1)); // Should be distinct physical nodes
    }

    @Test
    public void testConcurrency() throws InterruptedException {
        ProductionConsistentHashRing ring = new ProductionConsistentHashRing();
        ring.addNode("initial-node");

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);
        AtomicInteger successfulReads = new AtomicInteger(0);

        // Reader threads
        for (int i = 0; i < threadCount - 2; i++) {
            executor.submit(() -> {
                for (int j = 0; j < 10000; j++) {
                    Optional<String> node = ring.getNode("key-" + j);
                    if (node.isPresent()) successfulReads.incrementAndGet();
                }
                latch.countDown();
            });
        }

        // Writer threads adding and removing nodes continuously
        for (int i = 0; i < 2; i++) {
            final int writerId = i;
            executor.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    ring.addNode("node-" + writerId + "-" + j);
                    ring.removeNode("node-" + writerId + "-" + (j - 1));
                    try { Thread.sleep(1); } catch (InterruptedException e) {}
                }
                latch.countDown();
            });
        }

        latch.await(5, TimeUnit.SECONDS);
        executor.shutdown();

        // 8 reader threads * 10000 reads = 80000 successful reads
        assertEquals(80000, successfulReads.get());
    }

    @Test
    public void testListeners() {
        ProductionConsistentHashRing ring = new ProductionConsistentHashRing();
        
        List<String> added = new ArrayList<>();
        List<String> removed = new ArrayList<>();
        
        ring.addListener(new RingChangeListener() {
            @Override
            public void onNodeAdded(String nodeId) { added.add(nodeId); }
            @Override
            public void onNodeRemoved(String nodeId) { removed.add(nodeId); }
        });

        ring.addNode("node-1");
        ring.addNode("node-2");
        ring.removeNode("node-1");

        assertEquals(Arrays.asList("node-1", "node-2"), added);
        assertEquals(Collections.singletonList("node-1"), removed);
    }
}
