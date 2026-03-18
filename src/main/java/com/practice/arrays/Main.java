package com.practice.arrays;

import java.util.Arrays;

class Main {
    
    /**
     * Merges overlapping intervals using a while-loop structure that resembles the original logic.
     * Fixes included: sorting, chained merge look-ahead, and correct boundary handling.
     */
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }

        // Must sort first for the linear scan to work correctly
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        int[][] out = new int[intervals.length][2];
        int count = 0;
        int i = 0;

        // Using a while loop to resemble the original structure
        while (i < intervals.length) {
            int one = intervals[i][0];
            int two = intervals[i][1];

            // Look ahead and merge all subsequent intervals that overlap with 'two'
            // This fixes the "chained merge" issue in the original logic.
            while (i < intervals.length - 1 && two >= intervals[i + 1][0]) {
                // If the next interval overlaps, update the end ('two') to the max end
                two = Math.max(two, intervals[i + 1][1]);
                i++; // Consume the merged interval
            }

            // Store the final merged interval into the output array
            out[count][0] = one;
            out[count][1] = two;
            count++;

            i++; // Move to the next interval to process
        }

        // Return a copy of the array trimmed to the actual number of merged intervals
        return Arrays.copyOf(out, count);
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