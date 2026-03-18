package com.practice.arrays;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testStandardOverlapping() {
        int[][] intervals = {{1, 3}, {2, 6}, {8, 10}, {15, 18}};
        int[][] expected = {{1, 6}, {8, 10}, {15, 18}};
        assertArrayEquals(expected, Main.mergeIntervals(intervals));
    }

    @Test
    void testChainedOverlapping() {
        // User's specific case: [4,9] merges with [8,10] to become [4,10]
        int[][] intervals = {{1, 3}, {4, 9}, {8, 10}, {15, 18}};
        int[][] expected = {{1, 3}, {4, 10}, {15, 18}};
        assertArrayEquals(expected, Main.mergeIntervals(intervals));
    }

    @Test
    void testNonOverlapping() {
        int[][] intervals = {{1, 2}, {3, 4}, {5, 6}};
        int[][] expected = {{1, 2}, {3, 4}, {5, 6}};
        assertArrayEquals(expected, Main.mergeIntervals(intervals));
    }

    @Test
    void testUnsortedInput() {
        int[][] intervals = {{15, 18}, {1, 3}, {8, 10}, {2, 6}};
        int[][] expected = {{1, 6}, {8, 10}, {15, 18}};
        assertArrayEquals(expected, Main.mergeIntervals(intervals));
    }

    @Test
    void testFullyOverlapping() {
        int[][] intervals = {{1, 10}, {2, 3}, {4, 5}, {6, 7}};
        int[][] expected = {{1, 10}};
        assertArrayEquals(expected, Main.mergeIntervals(intervals));
    }

    @Test
    void testEmptyInput() {
        int[][] intervals = {};
        int[][] expected = {};
        assertArrayEquals(expected, Main.mergeIntervals(intervals));
    }

    @Test
    void testNullInput() {
        assertNull(Main.mergeIntervals(null));
    }

    @Test
    void testSingleInterval() {
        int[][] intervals = {{1, 5}};
        int[][] expected = {{1, 5}};
        assertArrayEquals(expected, Main.mergeIntervals(intervals));
    }

    @Test
    void testIdenticalIntervals() {
        int[][] intervals = {{1, 4}, {1, 4}, {1, 4}};
        int[][] expected = {{1, 4}};
        assertArrayEquals(expected, Main.mergeIntervals(intervals));
    }

    @Test
    void testTouchingIntervals() {
        // Intervals that touch at the boundaries should be merged
        int[][] intervals = {{1, 4}, {4, 5}};
        int[][] expected = {{1, 5}};
        assertArrayEquals(expected, Main.mergeIntervals(intervals));
    }
}
