package com.practice.graphs;

import java.util.*;

/**
 * Adjacency List representation of a Graph.
 * Includes common algorithms for learning: BFS, DFS, and Cycle Detection.
 */
public class GraphAL {
    private final int V; // Number of vertices
    private final List<List<Integer>> adj; // Adjacency List

    public GraphAL(int v) {
        this.V = v;
        adj = new ArrayList<>(v);
        for (int i = 0; i < v; i++) {
            adj.add(new ArrayList<>());
        }
    }

    /**
     * Adds an undirected edge between u and v.
     */
    public void addEdge(int u, int v) {
        adj.get(u).add(v);
        adj.get(v).add(u);
    }

    /**
     * Adds a directed edge from u to v.
     */
    public void addDirectedEdge(int u, int v) {
        adj.get(u).add(v);
    }

    /**
     * Breadth-First Search (BFS) Traversal
     * Explores level-by-level using a Queue.
     * 
     * Time Complexity: O(V + E) where V is vertices and E is edges.
     * Space Complexity: O(V) for the visited array and queue.
     */
    public void performBFS(int startNode) {
        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();

        visited[startNode] = true;
        queue.add(startNode);

        System.out.print("BFS starting from " + startNode + ": ");
        while (!queue.isEmpty()) {
            int curr = queue.poll();

            System.out.print(curr + " ");

            for (int neighbor : adj.get(curr)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.add(neighbor);
                }
            }
        }
        System.out.println();
    }

    /**
     * Depth-First Search (DFS) Traversal
     * Explores as deep as possible using recursion (Call Stack).
     * 
     * Time Complexity: O(V + E)
     * Space Complexity: O(V) for the visited array and recursion stack.
     */
    public void dfs(int startNode) {
        boolean[] visited = new boolean[V];
        System.out.print("DFS starting from " + startNode + ": ");
        dfsRecursive(startNode, visited);
        System.out.println();
    }

    private void dfsRecursive(int curr, boolean[] visited) {
        visited[curr] = true;
        System.out.print(curr + " ");

        for (int neighbor : adj.get(curr)) {
            if (!visited[neighbor]) {
                dfsRecursive(neighbor, visited);
            }
        }
    }

    /**
     * Cycle Detection (Undirected Graph) using DFS.
     * Rule: If a visited neighbor is NOT the parent/previous node, a cycle exists.
     * 
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     */
    public boolean hasCycleUndirected() {
        boolean[] visited = new boolean[V];
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                if (hasCycleUndirectedUtil(i, visited, -1)) return true;
            }
        }
        return false;
    }

    private boolean hasCycleUndirectedUtil(int curr, boolean[] visited, int parent) {
        visited[curr] = true;

        for (int neighbor : adj.get(curr)) {
            if (!visited[neighbor]) {
                if (hasCycleUndirectedUtil(neighbor, visited, curr)) return true;
            } else if (neighbor != parent) {
                // Visited and not parent => Cycle found!
                return true;
            }
        }
        return false;
    }

    /**
     * Cycle Detection (Directed Graph) using DFS and Recursion Stack.
     * Rule: If a neighbor is already in the current recursion path (back edge), a cycle exists.
     * 
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     */
    public boolean hasCycleDirected() {
        boolean[] visited = new boolean[V];
        boolean[] recStack = new boolean[V];

        for (int i = 0; i < V; i++) {
            if (hasCycleDirectedUtil(i, visited, recStack)) return true;
        }
        return false;
    }

    private boolean hasCycleDirectedUtil(int curr, boolean[] visited, boolean[] recStack) {
        if (recStack[curr]) return true; // Back edge detected
        if (visited[curr]) return false; // Already processed

        visited[curr] = true;
        recStack[curr] = true;

        for (int neighbor : adj.get(curr)) {
            if (hasCycleDirectedUtil(neighbor, visited, recStack)) return true;
        }

        recStack[curr] = false; // Backtrack
        return false;
    }

    public void printGraph() {
        for (int i = 0; i < V; i++) {
            System.out.print(i + " -> " + adj.get(i));
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println("--- Graph Learning Demo ---");
        GraphAL g = new GraphAL(5);
        g.addEdge(0, 1);
        g.addEdge(0, 4);
        g.addEdge(1, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 3);
        g.addEdge(3, 4);

        System.out.println("Adjacency List:");
        g.printGraph();

        g.performBFS(0);
        g.dfs(0);

        System.out.println("Has Cycle (Undirected): " + g.hasCycleUndirected());

        System.out.println("\n--- Directed Graph Cycle Demo ---");
        GraphAL dg = new GraphAL(3);
        dg.addDirectedEdge(0, 1);
        dg.addDirectedEdge(1, 2);
        dg.addDirectedEdge(2, 0); // Cycle: 0 -> 1 -> 2 -> 0
        dg.printGraph();
        System.out.println("Has Cycle (Directed): " + dg.hasCycleDirected());
    }
}
