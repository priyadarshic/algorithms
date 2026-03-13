package com.practice.graphs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class GraphTest {

    @Test
    public void testBFS() {
        /*
         * Graph Structure:
         * (0)---(1)
         *  | \ /
         *  |  (2)---(3)
         * (3) (self-loop)
         */
        GraphAL g = new GraphAL(4);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 3);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        g.performBFS(2);
        
        String output = outContent.toString().trim();
        // Starting from 2, BFS should visit 2, 0, 1, 3
        assertTrue(output.contains("2 0 1 3") || output.contains("2 0 3 1"));
        
        System.setOut(System.out);
    }

    @Test
    public void testDFS() {
        /*
         * Graph Structure (Same as BFS):
         * 0 -- 1
         * | \ /
         * |  2 -- 3
         * 3 (loop)
         */
        GraphAL g = new GraphAL(4);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.addEdge(2, 0);
        g.addEdge(2, 3);
        g.addEdge(3, 3);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        g.dfs(2);
        
        String output = outContent.toString().trim();
        // DFS starting from 2 could be multiple valid paths
        assertTrue(output.contains("2") && output.contains("0") && output.contains("1") && output.contains("3"));
        
        System.setOut(System.out);
    }

    @Test
    public void testUndirectedCycle() {
        /*
         * Graph Structure (No Cycle):
         * 0 -- 1 -- 2
         * 
         * With Cycle:
         * 0 -- 1
         * |    |
         * + -- 2
         */
        GraphAL g = new GraphAL(3);
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        assertFalse(g.hasCycleUndirected(), "Should not have cycle");

        g.addEdge(2, 0);
        assertTrue(g.hasCycleUndirected(), "Should have cycle 0-1-2-0");
    }

    @Test
    public void testDirectedCycle() {
        /*
         * Graph Structure (No Cycle):
         * 0 -> 1 -> 2
         * 
         * With Cycle:
         * 0 -> 1 -> 2 -> (back to 0)
         */
        GraphAL g = new GraphAL(3);
        g.addDirectedEdge(0, 1);
        g.addDirectedEdge(1, 2);
        assertFalse(g.hasCycleDirected(), "Should not have cycle in directed chain");

        g.addDirectedEdge(2, 0);
        assertTrue(g.hasCycleDirected(), "Should have cycle 0->1->2->0");
    }
}
