package com.practice.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Kruskal's Algorithm implementation for finding the Minimum Spanning Tree (MST)
 * of a connected, undirected graph.
 */
public class KruskalsAlgorithm {

    /**
     * Represents a weighted edge in the graph.
     */
    static class Edge implements Comparable<Edge> {
        int src, dest, weight;

        public Edge(int src, int dest, int weight) {
            this.src = src;
            this.dest = dest;
            this.weight = weight;
        }

        // Sort edges by weight in ascending order
        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.weight, other.weight);
        }
        
        @Override
        public String toString() {
            return src + " -- " + dest + " == " + weight;
        }
    }

    /**
     * Disjoint Set (Union-Find) data structure 
     * using Path Compression and Union by Rank.
     */
    static class DisjointSet {
        int[] parent, rank;

        public DisjointSet(int n) {
            parent = new int[n];
            rank = new int[n];
            // Initially, each element is in its own set (parent is itself)
            // and has a rank of 0.
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
        }

        // Find the root of the set in which element 'i' resides.
        // Applies Path Compression to keep the tree flat.
        public int find(int i) {
            if (parent[i] == i) {
                return i;
            }
            // Path compression: make the found root the parent of 'i'
            return parent[i] = find(parent[i]);
        }

        // Union of two sets.
        // Applies Union by Rank to keep the tree shallow.
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);

            // If they are in different sets, merge them
            if (rootX != rootY) {
                // Attach the shorter tree under the root of the taller tree
                if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else {
                    // If ranks are identical, attach one to the other and increment rank
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
            }
        }
    }

    /**
     * Finds and prints the Minimum Spanning Tree using Kruskal's algorithm.
     * 
     * @param vertices Number of vertices in the graph
     * @param edges    List of all edges in the graph
     */
    public static void kruskalMST(int vertices, List<Edge> edges) {
        List<Edge> mst = new ArrayList<>(); // To store the resultant MST
        int minCost = 0;

        // Step 1: Sort all edges in non-decreasing order of their weight
        Collections.sort(edges);

        // Step 2: Initialize Disjoint Set to track connected components
        DisjointSet ds = new DisjointSet(vertices);

        // Step 3: Iterate through all sorted edges
        for (Edge edge : edges) {
            int rootSrc = ds.find(edge.src);
            int rootDest = ds.find(edge.dest);

            // If including this edge does not cause a cycle (roots are different)
            // include it in the MST and merge the two components.
            if (rootSrc != rootDest) {
                mst.add(edge);
                minCost += edge.weight;
                ds.union(rootSrc, rootDest); // Union the sets
            }
            
            // Optimization: If the MST has V - 1 edges, it's complete, so we can stop early
            if (mst.size() == vertices - 1) {
                break;
            }
        }

        // Output results
        System.out.println("Edges in the constructed MST:");
        for (Edge edge : mst) {
            System.out.println(edge);
        }
        System.out.println("Minimum Cost Spanning Tree: " + minCost);
    }

    public static void main(String[] args) {
        int vertices = 6;
        List<Edge> edges = new ArrayList<>();

        /*
         * Example Graph:
         * 
         *        4          3
         *   (0)------(1)-------(3)
         *    |      /          /
         *   4|    /2         /3
         *    |  /          /
         *   (2)          (4)
         *     \         /
         *     2\       /3
         *       \     /
         *         (5)
         */
         
        // Add edges: src, dest, weight
        edges.add(new Edge(0, 1, 4));
        edges.add(new Edge(0, 2, 4));
        edges.add(new Edge(1, 2, 2));
        edges.add(new Edge(2, 3, 3));
        edges.add(new Edge(2, 5, 2));
        edges.add(new Edge(2, 4, 4));
        edges.add(new Edge(3, 4, 3));
        edges.add(new Edge(5, 4, 3));
        edges.add(new Edge(1, 3, 3));

        System.out.println("--- Kruskal's Algorithm Demo ---");
        kruskalMST(vertices, edges);
    }
}
