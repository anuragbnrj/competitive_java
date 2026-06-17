package library.heaps;

import java.util.*;

public class MergeKSorted {

    // ---------------------------------------------------------------
    // Merge k already-sorted arrays into one sorted array. A min-heap
    // holds the current front of each non-empty array as
    // {value, arrayIdx, elemIdx}; pop the global minimum, then push
    // the next element from that same array.
    //
    // O(N log k) time (N = total elements), O(k) heap space.
    // ---------------------------------------------------------------
    public static int[] merge(int[][] arrays) {
        int total = 0;
        for (int[] a : arrays) total += a.length;
        int[] result = new int[total];

        // min-heap by value
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 0; i < arrays.length; i++) {
            if (arrays[i].length > 0) {
                heap.offer(new int[]{arrays[i][0], i, 0});
            }
        }

        int idx = 0;
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            int val = top[0], arr = top[1], pos = top[2];
            result[idx++] = val;

            if (pos + 1 < arrays[arr].length) {
                heap.offer(new int[]{arrays[arr][pos + 1], arr, pos + 1});
            }
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] arrays = {
                {1, 4, 5},
                {1, 3, 4},
                {2, 6}
        };
        System.out.println("Merged (expect [1, 1, 2, 3, 4, 4, 5, 6]): "
                + Arrays.toString(merge(arrays)));

        int[][] withEmpty = {{}, {0}, {}};
        System.out.println("With empty arrays (expect [0]): " + Arrays.toString(merge(withEmpty)));
    }
}
