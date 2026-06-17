package library.heaps;

import java.util.*;

public class KClosestPoints {

    // ---------------------------------------------------------------
    // The k points closest to the origin. Keep a MAX-heap of size k
    // ordered by squared distance: the farthest of the kept points
    // sits on top and is evicted first, so the k nearest survive.
    //
    // Compare SQUARED distance as `long` - no sqrt, no floats, no
    // overflow (coords up to ~1e4 -> dist^2 up to ~2e8, but use long
    // to stay safe for larger inputs).
    //
    // O(n log k) time, O(k) space.
    // ---------------------------------------------------------------
    public static int[][] kClosest(int[][] points, int k) {
        // max-heap by squared distance
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>(
                (a, b) -> Long.compare(dist2(b), dist2(a)));

        for (int[] p : points) {
            maxHeap.offer(p);
            if (maxHeap.size() > k) {
                maxHeap.poll(); // drop the farthest kept point
            }
        }

        int[][] result = new int[maxHeap.size()][];
        for (int i = 0; i < result.length; i++) {
            result[i] = maxHeap.poll();
        }
        return result;
    }

    private static long dist2(int[] p) {
        return (long) p[0] * p[0] + (long) p[1] * p[1];
    }

    public static void main(String[] args) {
        int[][] points = {{1, 3}, {-2, 2}, {5, 8}, {0, 1}};
        // dist^2: 10, 8, 89, 1 -> 2 closest are {0,1} and {-2,2}
        int[][] result = kClosest(points, 2);

        List<String> got = new ArrayList<>();
        for (int[] p : result) got.add(Arrays.toString(p));
        Collections.sort(got); // order within the k is unspecified
        System.out.println("2 closest (expect [-2, 2] and [0, 1]): " + got);
    }
}
