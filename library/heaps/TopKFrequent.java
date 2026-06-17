package library.heaps;

import java.util.*;

public class TopKFrequent {

    // ---------------------------------------------------------------
    // The k most frequent elements. Count with a HashMap, then keep a
    // MIN-heap of size k ordered BY FREQUENCY (smallest freq on top,
    // evicted first) so the k highest-frequency keys survive.
    //
    // O(n log k) time. An O(n) bucket-sort by frequency is possible
    // (index buckets 0..n) - see NOTES.md.
    // ---------------------------------------------------------------
    public static int[] topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        for (int x : nums) {
            freq.merge(x, 1, Integer::sum);
        }

        // min-heap by frequency: top = least frequent among the kept
        PriorityQueue<Integer> heap =
                new PriorityQueue<>((a, b) -> freq.get(a) - freq.get(b));
        for (int key : freq.keySet()) {
            heap.offer(key);
            if (heap.size() > k) {
                heap.poll();
            }
        }

        int[] result = new int[heap.size()];
        for (int i = result.length - 1; i >= 0; i--) {
            result[i] = heap.poll(); // poll gives ascending freq -> fill from back
        }
        return result;
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 1, 2, 2, 3};
        // freqs: 1->3, 2->2, 3->1 ; top 2 by frequency = [1, 2]
        System.out.println("Top 2 frequent (expect [1, 2]): " + Arrays.toString(topKFrequent(nums, 2)));

        int[] single = {7};
        System.out.println("Top 1 of [7] (expect [7]): " + Arrays.toString(topKFrequent(single, 1)));
    }
}
