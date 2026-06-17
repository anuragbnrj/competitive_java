package library.heaps;

import java.util.*;

public class KthLargest {

    // ---------------------------------------------------------------
    // k-th LARGEST element. Keep a MIN-heap of size k: after pushing
    // every value and evicting whenever size > k, the heap holds the
    // k largest seen, and its smallest (the top) is the k-th largest.
    //
    // (The pairing is the classic trip-up: k-th *largest* -> *min*
    // heap of size k; k-th *smallest* -> *max* heap of size k.)
    //
    // O(n log k) time, O(k) space. For one-shot k-th order statistic
    // QuickSelect is O(n) average - see NOTES.md.
    // ---------------------------------------------------------------
    public static int kthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(); // default = min-heap
        for (int x : nums) {
            minHeap.offer(x);
            if (minHeap.size() > k) {
                minHeap.poll(); // drop the smallest, keep the k largest
            }
        }
        return minHeap.peek();
    }

    public static void main(String[] args) {
        int[] nums = {3, 2, 1, 5, 6, 4};
        System.out.println("2nd largest (expect 5): " + kthLargest(nums, 2));
        System.out.println("1st largest (expect 6): " + kthLargest(nums, 1));
        System.out.println("6th largest (expect 1): " + kthLargest(nums, 6));

        int[] dups = {3, 3, 3, 3};
        System.out.println("2nd largest of all-equal (expect 3): " + kthLargest(dups, 2));
    }
}
