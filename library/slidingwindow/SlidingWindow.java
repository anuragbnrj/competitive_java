package library.slidingwindow;

import java.util.HashMap;
import java.util.Map;

public class SlidingWindow {

    // Fixed-size window: max sum of any contiguous window of size k.
    // Add the entering element, drop the leaving one - never re-sum the window.
    public static long maxSumFixedWindow(int[] a, int k) {
        long sum = 0, best = Long.MIN_VALUE;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
            if (i >= k) sum -= a[i - k];
            if (i >= k - 1) best = Math.max(best, sum);
        }
        return best;
    }

    // Variable window: count of subarrays with at most k distinct values.
    // Expand right; while distinct count > k, shrink from left.
    public static long countAtMostKDistinct(int[] a, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        long ans = 0;
        int left = 0;
        for (int right = 0; right < a.length; right++) {
            freq.merge(a[right], 1, Integer::sum);
            while (freq.size() > k) {
                int v = a[left++];
                if (freq.merge(v, -1, Integer::sum) == 0) freq.remove(v);
            }
            ans += (right - left + 1); // all subarrays [l, right], l in [left, right]
        }
        return ans;
    }

    // Variable window: length of the longest subarray with at most k distinct values.
    public static int longestAtMostKDistinct(int[] a, int k) {
        Map<Integer, Integer> freq = new HashMap<>();
        int left = 0, best = 0;
        for (int right = 0; right < a.length; right++) {
            freq.merge(a[right], 1, Integer::sum);
            while (freq.size() > k) {
                int v = a[left++];
                if (freq.merge(v, -1, Integer::sum) == 0) freq.remove(v);
            }
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    // Variable window, shrink-while-valid: length of the smallest subarray
    // whose sum is >= target. Assumes all-positive values. Returns 0 if none.
    public static int minWindowSumAtLeast(int[] a, int target) {
        int left = 0, best = Integer.MAX_VALUE;
        long sum = 0;
        for (int right = 0; right < a.length; right++) {
            sum += a[right];
            while (sum >= target) {
                best = Math.min(best, right - left + 1);
                sum -= a[left++];
            }
        }
        return best == Integer.MAX_VALUE ? 0 : best;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 1, 2, 3};

        System.out.println(maxSumFixedWindow(a, 2));      // expect: 5  (window [2,3])
        System.out.println(countAtMostKDistinct(a, 2));   // expect: 12 (subarrays w/ <=2 distinct)
        System.out.println(longestAtMostKDistinct(a, 2)); // expect: 4  (window [1,2,1,2])
        System.out.println(minWindowSumAtLeast(a, 5));    // expect: 2  (window [2,3])
        System.out.println(minWindowSumAtLeast(a, 100));  // expect: 0  (no window reaches 100)
    }
}
