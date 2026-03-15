package com.practice.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

class Array2DMerge {

    public static void main(String[] args) {
        //intervals = [[1,3],[2,6],[8,10],[15,18]]
        //output = [[1,6],[8,10],[15,18]]
        int[][] intervals = {{1,3},{2,6},{8,10},{15,18}};

        int[][] out = mergeIntervals(intervals);
        printArray(out);
    }

    private static int[][] mergeIntervals(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }

        // Sort intervals based on starting time
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();
        int[] currentInterval = intervals[0];
        merged.add(currentInterval);

        for (int[] interval : intervals) {
            int currentEnd = currentInterval[1];
            int nextStart = interval[0];
            int nextEnd = interval[1];

            if (currentEnd >= nextStart) {
                // Overlapping intervals, merge them
                currentInterval[1] = Math.max(currentEnd, nextEnd);
            } else {
                // Disjoint intervals, add the new interval to the list
                currentInterval = interval;
                merged.add(currentInterval);
            }
        }

        return merged.toArray(new int[merged.size()][]);
    }

    private static void printArray(int[][] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.println("[" + arr[i][0] + ", " + arr[i][1] + "]");
        }
    }
}