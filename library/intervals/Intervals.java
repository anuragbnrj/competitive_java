package library.intervals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Intervals {

    // Merge all overlapping intervals. Sort by start, then extend the last
    // merged interval's end whenever the next interval starts at or before it.
    // O(n log n).
    public static int[][] merge(int[][] intervals) {
        if (intervals.length == 0) return intervals;
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));

        List<int[]> result = new ArrayList<>();
        int[] current = sorted[0].clone();
        for (int i = 1; i < sorted.length; i++) {
            int[] next = sorted[i];
            if (next[0] <= current[1]) {
                current[1] = Math.max(current[1], next[1]);
            } else {
                result.add(current);
                current = next.clone();
            }
        }
        result.add(current);
        return result.toArray(new int[0][]);
    }

    // Insert newInterval into an already-sorted, non-overlapping list of
    // intervals, merging where needed. O(n).
    public static int[][] insert(int[][] intervals, int[] newInterval) {
        List<int[]> result = new ArrayList<>();
        int i = 0, n = intervals.length;

        // intervals that end before newInterval starts: no overlap, keep as-is
        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(intervals[i]);
            i++;
        }

        // intervals that overlap newInterval: merge into it
        int start = newInterval[0], end = newInterval[1];
        while (i < n && intervals[i][0] <= end) {
            start = Math.min(start, intervals[i][0]);
            end = Math.max(end, intervals[i][1]);
            i++;
        }
        result.add(new int[]{start, end});

        // remaining intervals: entirely after newInterval
        while (i < n) {
            result.add(intervals[i]);
            i++;
        }
        return result.toArray(new int[0][]);
    }

    // Minimum number of intervals to remove so the rest are non-overlapping.
    // Sort by END time, greedily keep an interval iff it starts at/after the
    // last kept interval's end (the classic "max non-overlapping" greedy).
    // O(n log n).
    public static int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length == 0) return 0;
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[1], b[1]));

        int removed = 0;
        int lastEnd = sorted[0][1];
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i][0] < lastEnd) {
                removed++;
            } else {
                lastEnd = sorted[i][1];
            }
        }
        return removed;
    }

    public static void main(String[] args) {
        int[][] merged = merge(new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}});
        System.out.println(Arrays.deepToString(merged)); // expect: [[1, 6], [8, 10], [15, 18]]

        int[][] inserted = insert(new int[][]{{1, 3}, {6, 9}}, new int[]{2, 5});
        System.out.println(Arrays.deepToString(inserted)); // expect: [[1, 5], [6, 9]]

        System.out.println(eraseOverlapIntervals(new int[][]{{1, 2}, {2, 3}, {3, 4}, {1, 3}})); // expect: 1
    }
}
