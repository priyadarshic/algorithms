package com.practice.hashing;

import java.util.*;
import java.util.stream.Collectors;

public class ConsistentHashDemo {

    public static void main(String[] args) {
        System.out.println("══════════════════════════════════════════════════");
        System.out.println("  Consistent Hashing with MurmurHash3 — Demo");
        System.out.println("══════════════════════════════════════════════════\n");

        // ── 1. Setup: 3 nodes, 150 virtual nodes each ──────────────────
        ConsistentHashRing ring = new ConsistentHashRing(150, 0);
        ring.addNode("node-A");
        ring.addNode("node-B");
        ring.addNode("node-C");

        String[] keys = {
                "user:1001", "user:1002", "user:1003",
                "product:SKU-999", "session:abc123", "order:XYZ-7"
        };

        System.out.println("── Initial routing (3 nodes) ──────────────────────");
        Map<String, String> baseline = routeAll(ring, keys);
        printRouting(baseline);
        printDistribution(ring);

        // ── 2. Add a node ───────────────────────────────────────────────
        System.out.println("\n── After adding node-D ────────────────────────────");
        ring.addNode("node-D");
        Map<String, String> afterAdd = routeAll(ring, keys);
        printRouting(afterAdd);
        printMigration(baseline, afterAdd, "node addition");
        printDistribution(ring);

        // ── 3. Remove a node ────────────────────────────────────────────
        System.out.println("\n── After removing node-B ──────────────────────────");
        ring.removeNode("node-B");
        Map<String, String> afterRemove = routeAll(ring, afterAdd);
        printRouting(afterRemove);
        printMigration(afterAdd, afterRemove, "node removal");
        printDistribution(ring);

        // ── 4. Replication (get top-2 replica nodes for each key) ───────
        System.out.println("\n── Replication: top-2 nodes per key ───────────────");
        for (String key : keys) {
            List<String> replicas = ring.getReplicaNodes(key, 2);
            System.out.printf("  %-22s  →  %s%n", key, replicas);
        }

        // ── 5. Key-space percentage breakdown ───────────────────────────
        System.out.println("\n── Key-space % owned per node (3 nodes: A, C, D) ──");
        ring.keySpaceDistribution().forEach((node, pct) ->
                System.out.printf("  %-10s  %.2f%%%n", node, pct));
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static Map<String, String> routeAll(ConsistentHashRing ring, String[] keys) {
        Map<String, String> result = new LinkedHashMap<>();
        for (String k : keys) result.put(k, ring.getNode(k).orElse("(none)"));
        return result;
    }

    private static Map<String, String> routeAll(ConsistentHashRing ring,
                                                Map<String, String> previous) {
        return routeAll(ring, previous.keySet().toArray(new String[0]));
    }

    private static void printRouting(Map<String, String> routing) {
        routing.forEach((key, node) ->
                System.out.printf("  %-22s  →  %s%n", key, node));
    }

    /**
     * Shows which keys moved to a different node — the "migration cost".
     * Ideal consistent hashing should move only K/N keys when adding a node
     * (K = total keys, N = number of nodes).
     */
    private static void printMigration(Map<String, String> before,
                                       Map<String, String> after,
                                       String event) {
        long moved = before.entrySet().stream()
                .filter(e -> !e.getValue().equals(after.get(e.getKey())))
                .count();
        System.out.printf("  Keys remapped after %s: %d / %d  (%.0f%%)%n",
                event, moved, before.size(),
                (moved * 100.0) / before.size());
    }

    private static void printDistribution(ConsistentHashRing ring) {
        System.out.println("  Virtual-node slots: " +
                ring.virtualNodeDistribution().entrySet().stream()
                        .map(e -> e.getKey() + "=" + e.getValue())
                        .collect(Collectors.joining(", ")));
    }
}