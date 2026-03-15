package com.practice.graphs;

import java.util.*;

/**
 * Dijkstra's Algorithm implementation for finding the shortest paths
 * from a single source vertex to all other vertices in a weighted graph.
 * 
 * Note: Dijkstra's algorithm does not work with graphs that contain negative weight edges.
 */
public class DijkstrasAlgorithm {

    /**
     * Represents a weighted edge/connection to an adjacent node.
     */
    static class Node implements Comparable<Node> {
        int vertex, weight;

        public Node(int vertex, int weight) {
            this.vertex = vertex;
            this.weight = weight;
        }

        // We want to process the node with the smallest distance/weight first,
        // so we sort them in ascending order of their weights.
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.weight, other.weight);
        }
    }

    private final int V; // Number of vertices
    private final List<List<Node>> adj; // Adjacency list

    public DijkstrasAlgorithm(int v) {
        this.V = v;
        adj = new ArrayList<>(v);
        for (int i = 0; i < v; i++) {
            adj.add(new ArrayList<>());
        }
    }

    /**
     * Adds a directed edge to the graph.
     */
    public void addEdge(int source, int destination, int weight) {
        adj.get(source).add(new Node(destination, weight));
    }
    
    /**
     * Adds an undirected edge to the graph.
     */
    public void addUndirectedEdge(int u, int v, int weight) {
        adj.get(u).add(new Node(v, weight));
        adj.get(v).add(new Node(u, weight));
    }

    /**
     * Finds and prints the shortest paths from the source vertex to all other vertices.
     *
     * @param source The starting vertex
     */
    public void dijkstra(int source) {
        // Step 1: Initialize distances
        // Create an array to hold the shortest distance from the source to each vertex.
        // Initialize all distances to infinity (Integer.MAX_VALUE), except the source which is 0.
        int[] distances = new int[V];
        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[source] = 0;

        // Create a set to keep track of vertices whose shortest distance from the source
        // is already finalized.
        boolean[] settled = new boolean[V];

        // Ensure we always process the next vertex with the shortest known distance.
        // PriorityQueue acts as our Min-Heap.
        PriorityQueue<Node> pq = new PriorityQueue<>(V);

        // Add the source node to the PriorityQueue to start processing.
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            // Step 2: Extract the min-distance node
            // Remove the node with the smallest distance from the priority queue.
            Node current = pq.poll();
            int u = current.vertex;

            // If the node is already settled, we skip it.
            // This can happen because we might add multiple updates for the same vertex to the PQ.
            if (settled[u]) {
                continue;
            }

            // Mark the extracted node as settled. Its shortest path is now definitively known.
            settled[u] = true;

            // Step 3: Relaxation
            // Explore all adjacent neighbors of the current node.
            for (Node neighbor : adj.get(u)) {
                int v = neighbor.vertex;
                int weight = neighbor.weight;

                // Only process neighbors that haven't been settled yet.
                if (!settled[v]) {
                    // Check if there is a shorter path to 'v' through 'u'.
                    // If distance to 'u' + weight of edge (u, v) is less than the currently known shortest distance to 'v':
                    if (distances[u] + weight < distances[v]) {
                        distances[v] = distances[u] + weight;
                        // Add the neighbor to the PriorityQueue with the updated shorter distance.
                        pq.add(new Node(v, distances[v]));
                    }
                }
            }
        }

        // Print the calculated shortest distances
        printSolution(source, distances);
    }

    private void printSolution(int source, int[] distances) {
        System.out.println("Shortest Paths from Source Vertex: " + source);
        System.out.println("Vertex \t\t Distance from Source");
        for (int i = 0; i < V; i++) {
             // Handle unreachable vertices
             if (distances[i] == Integer.MAX_VALUE) {
                 System.out.println(i + " \t\t " + "Unreachable");
             } else {
                 System.out.println(i + " \t\t " + distances[i]);
             }
        }
    }

    public static void main(String[] args) {
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
         
        int vertices = 6;
        DijkstrasAlgorithm graph = new DijkstrasAlgorithm(vertices);

        // Adding undirected edges to match the Kruskal example graph
        graph.addUndirectedEdge(0, 1, 4);
        graph.addUndirectedEdge(0, 2, 4);
        graph.addUndirectedEdge(1, 2, 2);
        graph.addUndirectedEdge(1, 3, 3);
        graph.addUndirectedEdge(2, 3, 3);
        graph.addUndirectedEdge(2, 4, 4);
        graph.addUndirectedEdge(2, 5, 2);
        graph.addUndirectedEdge(3, 4, 3);
        graph.addUndirectedEdge(4, 5, 3);

        System.out.println("--- Dijkstra's Algorithm Demo ---");
        // Calculate shortest paths from vertex 0
        graph.dijkstra(0);
    }
}
