package library.monotonic;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class MonotonicDeque {

    // Sliding window maximum: for each window of size k, the max element.
    // O(n) total via a deque of indices with strictly decreasing a-values
    // front-to-back; the front is always the current window's max.
    public static int[] maxSlidingWindow(int[] a, int k) {
        int n = a.length;
        int[] result = new int[n - k + 1];
        Deque<Integer> deque = new ArrayDeque<>(); // indices

        for (int i = 0; i < n; i++) {
            // anything <= a[i] at the back can never be the max again - drop it
            while (!deque.isEmpty() && a[deque.peekLast()] <= a[i]) deque.pollLast();
            deque.offerLast(i);

            // front index has fallen out of the window - drop it
            if (deque.peekFirst() <= i - k) deque.pollFirst();

            if (i >= k - 1) result[i - k + 1] = a[deque.peekFirst()];
        }
        return result;
    }

    public static void main(String[] args) {
        int[] a = {1, 3, -1, -3, 5, 3, 6, 7};
        System.out.println(Arrays.toString(maxSlidingWindow(a, 3))); // expect: [3, 3, 5, 5, 6, 7]
    }
}
