package library.twopointers;

import java.util.Arrays;

public class TwoPointers {

    // Opposite ends: find indices (lo, hi) into a SORTED array with a[lo] + a[hi] == target.
    // Returns {-1, -1} if no pair exists.
    public static int[] twoSumSorted(int[] a, int target) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int sum = a[lo] + a[hi];
            if (sum == target) return new int[]{lo, hi};
            if (sum < target) lo++;
            else hi--;
        }
        return new int[]{-1, -1};
    }

    // Same direction (read/write, partition): move all zeroes to the end,
    // preserving the relative order of the non-zero elements. In place, O(1) extra space.
    public static void moveZeroesToEnd(int[] a) {
        int write = 0;
        for (int read = 0; read < a.length; read++) {
            if (a[read] != 0) {
                int tmp = a[write];
                a[write] = a[read];
                a[read] = tmp;
                write++;
            }
        }
    }

    public static void main(String[] args) {
        int[] sorted = {2, 7, 11, 15};
        System.out.println(Arrays.toString(twoSumSorted(sorted, 9)));   // expect: [0, 1]
        System.out.println(Arrays.toString(twoSumSorted(sorted, 100))); // expect: [-1, -1]

        int[] z = {0, 1, 0, 3, 12};
        moveZeroesToEnd(z);
        System.out.println(Arrays.toString(z)); // expect: [1, 3, 12, 0, 0]
    }
}
