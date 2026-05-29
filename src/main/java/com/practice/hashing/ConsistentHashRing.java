package com.practice.hashing;

import java.util.*;

/**
 * A consistent hash ring backed by MurmurHash3.
 *
 * Each physical node is placed on the ring VIRTUAL_NODES times, using the
 * key "nodeId#replicaIndex". This spreads load evenly even with a small
 * number of physical nodes and minimises key remapping on node changes.
 *
 * The ring is a TreeMap<Integer, String>:
 *   - key   = Murmur hash of the virtual-node label (maps to the 2^32 int space)
 *   - value = physical node id (e.g. "node-A")
 *
 * Lookup: hash the request key, then find the smallest ring position >= that
 * hash (clockwise walk). If none exists, wrap around to the first entry.
 */
public class ConsistentHashRing {

    // 150 virtual nodes per physical node is the Cassandra default.
    // More replicas = better balance, higher memory.
    private static final int DEFAULT_VIRTUAL_NODES = 150;

    private final TreeMap<Integer, String> ring = new TreeMap<>();
    private final int virtualNodes;
    private final int seed;

    public ConsistentHashRing() {
        this(DEFAULT_VIRTUAL_NODES, 0);
    }

    public ConsistentHashRing(int virtualNodes, int seed) {
        if (virtualNodes < 1) throw new IllegalArgumentException("virtualNodes must be >= 1");
        this.virtualNodes = virtualNodes;
        this.seed         = seed;
    }

    // ── Mutations ──────────────────────────────────────────────────────────

    /**
     * Adds a physical node to the ring.
     * Creates virtualNodes virtual entries, e.g. "node-A#0", "node-A#1", ...
     */
    public void addNode(String nodeId) {
        Objects.requireNonNull(nodeId, "nodeId must not be null");
        for (int i = 0; i < virtualNodes; i++) {
            int hash = MurmurHash3.hash32(virtualKey(nodeId, i), seed);
            ring.put(hash, nodeId);
        }
    }

    /**
     * Removes a physical node and all its virtual entries from the ring.
     */
    public void removeNode(String nodeId) {
        Objects.requireNonNull(nodeId, "nodeId must not be null");
        for (int i = 0; i < virtualNodes; i++) {
            int hash = MurmurHash3.hash32(virtualKey(nodeId, i), seed);
            ring.remove(hash);
        }
    }

    // ── Lookups ───────────────────────────────────────────────────────────

    /**
     * Returns the node responsible for the given key.
     *
     * Algorithm:
     *   1. Hash the key to an integer position on the ring.
     *   2. Walk clockwise: find the first virtual-node position >= keyHash.
     *   3. If none (keyHash > all node positions), wrap around to ring.firstEntry().
     */
    public Optional<String> getNode(String key) {
        if (ring.isEmpty()) return Optional.empty();
        int keyHash = MurmurHash3.hash32(key, seed);
        // ceilingEntry: smallest key >= keyHash, or null if beyond the last entry
        Map.Entry<Integer, String> entry = ring.ceilingEntry(keyHash);
        if (entry == null) {
            entry = ring.firstEntry(); // wrap around
        }
        return Optional.of(entry.getValue());
    }

    /**
     * Returns up to n distinct physical nodes starting from the position of the key,
     * walking clockwise. Used for replication — e.g. store on the next 3 nodes.
     *
     * @param key  the data key
     * @param n    number of distinct nodes to return (max = ring node count)
     * @return     ordered list of responsible node ids (primary first)
     */
    public List<String> getReplicaNodes(String key, int n) {
        if (ring.isEmpty()) return Collections.emptyList();

        int keyHash = MurmurHash3.hash32(key, seed);
        List<String> result  = new ArrayList<>();
        Set<String>  seen    = new LinkedHashSet<>();

        // Start from the ceiling position, then iterate clockwise (wrapping via tailMap → headMap)
        Iterator<Map.Entry<Integer, String>> iter = clockwiseIterator(keyHash);

        while (iter.hasNext() && seen.size() < n) {
            String nodeId = iter.next().getValue();
            seen.add(nodeId); // Set deduplicates physical node ids
        }

        result.addAll(seen);
        return result;
    }

    // ── Introspection ─────────────────────────────────────────────────────

    /** Returns the number of distinct physical nodes on the ring. */
    public int nodeCount() {
        return (int) ring.values().stream().distinct().count();
    }

    /** Returns all distinct physical node ids. */
    public Set<String> nodes() {
        return new HashSet<>(ring.values());
    }

    /**
     * Returns a load distribution map: how many virtual-node slots each
     * physical node owns. All values should be close to virtualNodes.
     */
    public Map<String, Integer> virtualNodeDistribution() {
        Map<String, Integer> dist = new TreeMap<>();
        for (String nodeId : ring.values()) {
            dist.merge(nodeId, 1, Integer::sum);
        }
        return dist;
    }

    /**
     * Estimates key-space percentage owned by each node.
     * Uses the ring's sorted positions to calculate arc lengths on [0, 2^32).
     */
    public Map<String, Double> keySpaceDistribution() {
        if (ring.isEmpty()) return Collections.emptyMap();

        long   totalSpace  = (1L << 32);               // 2^32
        Map<String, Long> arcMap = new TreeMap<>();

        List<Map.Entry<Integer, String>> entries = new ArrayList<>(ring.entrySet());
        int size = entries.size();

        for (int i = 0; i < size; i++) {
            long current = Integer.toUnsignedLong(entries.get(i).getKey());
            long next    = Integer.toUnsignedLong(entries.get((i + 1) % size).getKey());
            long arc     = (next > current) ? next - current : totalSpace - current + next;
            arcMap.merge(entries.get(i).getValue(), arc, Long::sum);
        }

        Map<String, Double> result = new TreeMap<>();
        arcMap.forEach((node, arc) ->
                result.put(node, (arc * 100.0) / totalSpace));
        return result;
    }

    // ── Internals ─────────────────────────────────────────────────────────

    /** Virtual node label: "nodeId#replicaIndex" */
    private String virtualKey(String nodeId, int index) {
        return nodeId + "#" + index;
    }

    /**
     * Returns an iterator that starts at the ceiling of keyHash and
     * wraps around the ring exactly once.
     */
    private Iterator<Map.Entry<Integer, String>> clockwiseIterator(int keyHash) {
        // tailMap: from keyHash to end of ring
        // headMap: from start to keyHash (wrap)
        List<Map.Entry<Integer, String>> ordered = new ArrayList<>();
        ordered.addAll(ring.tailMap(keyHash).entrySet());
        ordered.addAll(ring.headMap(keyHash).entrySet());
        return ordered.iterator();
    }
}