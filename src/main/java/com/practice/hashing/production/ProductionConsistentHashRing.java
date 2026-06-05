package com.practice.hashing.production;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A production-grade consistent hash ring implementation.
 * 
 * Key Features:
 * - Thread-Safety & High Throughput: Reads are wait-free using an immutable array reference updated atomically via copy-on-write.
 * - Pluggable Hashing: Supports injecting custom HashFunction implementations.
 * - Node Weighting: Allows adding nodes with different capacities (weights).
 * - Listeners: Supports observing topology changes.
 */
public class ProductionConsistentHashRing {

    // Default number of virtual nodes if no weight is provided.
    private static final int DEFAULT_WEIGHT = 150;

    private final HashFunction hashFunction;
    private final List<RingChangeListener> listeners = new CopyOnWriteArrayList<>();

    // We maintain an active snapshot of the ring as an immutable array for O(log N) wait-free binary search.
    private final AtomicReference<VirtualNode[]> ringState = new AtomicReference<>(new VirtualNode[0]);

    // Internal source of truth used during mutations. Mutating this requires synchronization.
    private final Map<String, Integer> physicalNodes = new HashMap<>(); // NodeId -> Weight
    
    public ProductionConsistentHashRing() {
        this(new MurmurHashFunction());
    }

    public ProductionConsistentHashRing(HashFunction hashFunction) {
        this.hashFunction = Objects.requireNonNull(hashFunction, "hashFunction cannot be null");
    }

    // ── Configuration ──────────────────────────────────────────────────────

    public void addListener(RingChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RingChangeListener listener) {
        listeners.remove(listener);
    }

    // ── Mutations ──────────────────────────────────────────────────────────

    /**
     * Adds a physical node to the ring with the default weight.
     * @param nodeId the unique identifier for the node
     */
    public void addNode(String nodeId) {
        addNode(nodeId, DEFAULT_WEIGHT);
    }

    /**
     * Adds a physical node to the ring with a specific weight.
     * The higher the weight, the more virtual nodes are created.
     *
     * @param nodeId the unique identifier for the node
     * @param weight the number of virtual nodes to generate
     */
    public synchronized void addNode(String nodeId, int weight) {
        Objects.requireNonNull(nodeId, "nodeId cannot be null");
        if (weight <= 0) throw new IllegalArgumentException("Weight must be > 0");

        if (physicalNodes.containsKey(nodeId)) {
            // Already exists; maybe log or do an update if weight changed?
            // For simplicity, we ignore duplicate adds or overwrite. Let's overwrite.
        }
        
        physicalNodes.put(nodeId, weight);
        rebuildRing();
        notifyNodeAdded(nodeId);
    }

    /**
     * Removes a physical node from the ring.
     * @param nodeId the unique identifier for the node
     */
    public synchronized void removeNode(String nodeId) {
        Objects.requireNonNull(nodeId, "nodeId cannot be null");
        if (physicalNodes.remove(nodeId) != null) {
            rebuildRing();
            notifyNodeRemoved(nodeId);
        }
    }

    /**
     * Rebuilds the immutable array snapshot from the current physical nodes.
     * Only called within a synchronized block during mutations.
     */
    private void rebuildRing() {
        // TreeMap naturally sorts by hash value
        TreeMap<Integer, String> treeMap = new TreeMap<>();

        for (Map.Entry<String, Integer> entry : physicalNodes.entrySet()) {
            String nodeId = entry.getKey();
            int weight = entry.getValue();

            for (int i = 0; i < weight; i++) {
                int hash = hashFunction.hash(nodeId + "#" + i);
                treeMap.put(hash, nodeId);
            }
        }

        // Flatten to array
        VirtualNode[] newRing = new VirtualNode[treeMap.size()];
        int idx = 0;
        for (Map.Entry<Integer, String> entry : treeMap.entrySet()) {
            newRing[idx++] = new VirtualNode(entry.getKey(), entry.getValue());
        }

        // Atomically publish the new state
        ringState.set(newRing);
    }

    // ── Lookups (Wait-Free) ───────────────────────────────────────────────

    /**
     * Returns the physical node responsible for the given key.
     * @param key the data key
     * @return an Optional containing the node ID, or empty if ring is empty
     */
    public Optional<String> getNode(String key) {
        VirtualNode[] ring = ringState.get();
        if (ring.length == 0) return Optional.empty();

        int hash = hashFunction.hash(key);
        int index = findCeilingIndex(ring, hash);

        // If beyond the end of the array, wrap around to 0
        if (index == ring.length) {
            index = 0;
        }

        return Optional.of(ring[index].physicalNodeId);
    }

    /**
     * Returns up to N distinct physical replica nodes.
     * @param key the data key
     * @param n the number of replicas
     * @return a list of node IDs
     */
    public List<String> getReplicaNodes(String key, int n) {
        VirtualNode[] ring = ringState.get();
        if (ring.length == 0) return Collections.emptyList();

        int hash = hashFunction.hash(key);
        int index = findCeilingIndex(ring, hash);

        Set<String> seenNodes = new LinkedHashSet<>();
        
        // Iterate clockwise, wrapping around if necessary
        for (int i = 0; i < ring.length && seenNodes.size() < n; i++) {
            int currentIdx = (index + i) % ring.length;
            seenNodes.add(ring[currentIdx].physicalNodeId);
        }

        return new ArrayList<>(seenNodes);
    }

    /**
     * Binary search to find the smallest index where ring[index].hash >= targetHash.
     * If all elements are smaller, returns array length.
     */
    private int findCeilingIndex(VirtualNode[] ring, int targetHash) {
        int low = 0;
        int high = ring.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int midVal = ring[mid].hash;

            if (midVal < targetHash) {
                low = mid + 1;
            } else if (midVal > targetHash) {
                high = mid - 1;
            } else {
                return mid; // Exact match
            }
        }
        return low; // Ceiling
    }

    // ── Introspection ─────────────────────────────────────────────────────

    public int getNodeCount() {
        // Safe to read physicalNodes size without sync since it's just a metric,
        // but for exact correctness we can read the array.
        VirtualNode[] ring = ringState.get();
        return (int) Arrays.stream(ring).map(v -> v.physicalNodeId).distinct().count();
    }

    // ── Listener Notifications ────────────────────────────────────────────

    private void notifyNodeAdded(String nodeId) {
        for (RingChangeListener listener : listeners) {
            try {
                listener.onNodeAdded(nodeId);
            } catch (Exception e) {
                // Production robust code shouldn't let a listener crash the node addition
                System.err.println("Listener threw exception on add: " + e.getMessage());
            }
        }
    }

    private void notifyNodeRemoved(String nodeId) {
        for (RingChangeListener listener : listeners) {
            try {
                listener.onNodeRemoved(nodeId);
            } catch (Exception e) {
                System.err.println("Listener threw exception on remove: " + e.getMessage());
            }
        }
    }

    // ── Internal Structures ───────────────────────────────────────────────

    /**
     * Immutable container for a virtual node in the sorted array.
     */
    private static class VirtualNode {
        final int hash;
        final String physicalNodeId;

        VirtualNode(int hash, String physicalNodeId) {
            this.hash = hash;
            this.physicalNodeId = physicalNodeId;
        }
    }
}
