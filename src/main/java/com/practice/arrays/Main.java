package com.practice.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Main {
    
    /**
     * Merges overlapping intervals from a 2D array.
     * 
     * The algorithm works by:
     * 1. Handling edge cases (null or single interval).
     * 2. Sorting intervals by their start values to ensure we can merge in a single linear pass.
     * 3. Building a list of merged intervals by comparing the current merged interval 
     *    with the next interval in the sorted sequence.
     * 
     * @param intervals A 2D array where each element [start, end] represents an interval.
     * @return A 2D array containing the merged intervals.
     */
    public static int[][] mergeIntervals(int[][] intervals) {
        // Edge case: if input is null or has 1 or 0 intervals, no merging is needed.
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }

        // 1. Sort intervals by start time. This is CRITICAL for the linear scan to work.
        // If intervals [a, b] and [c, d] are sorted and a <= c, they overlap if b >= c.
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> merged = new ArrayList<>();
        
        // Initialize with the first interval as our starting point for merging.
        int[] currentInterval = intervals[0];
        merged.add(currentInterval);

        for (int[] nextInterval : intervals) {
            int currentEnd = currentInterval[1];
            int nextStart = nextInterval[0];
            int nextEnd = nextInterval[1];

            // 2. Check for overlap: If the end of our current interval is greater than 
            // or equal to the start of the next interval, they MUST overlap.
            if (currentEnd >= nextStart) {
                // To merge, we extend the current interval's end to the maximum end of both.
                // We move currentInterval[1] directly because it is already in the 'merged' list.
                currentInterval[1] = Math.max(currentEnd, nextEnd);
            } else {
                // 3. No overlap: This means we've finished merging into the current interval.
                // Move on to the next interval as the new base for merging.
                currentInterval = nextInterval;
                merged.add(currentInterval);
            }
        }

        // Convert the dynamic List back to a static 2D array for the return.
        return merged.toArray(new int[merged.size()][]);
    }
    
    /**
     * Helper method to print the 2D array in a readable format.
     */
    private static void printArray(int[][] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print("[" + arr[i][0] + "," + arr[i][1] + "]");
            if (i < arr.length - 1) {
                System.out.print(",");
            }
        }
        System.out.println("]");
    }
    
    public static void main(String[] args) {
        // Input: [[1,3],[2,6],[8,10],[15,18]]
        // Expected Output: [[1,6],[8,10],[15,18]]
        int[][] intervals = {{1, 3}, {4, 9}, {8, 10}, {15, 18}};
        
        int[][] out = mergeIntervals(intervals);
        System.out.print("Output: ");
        printArray(out);
    }
}