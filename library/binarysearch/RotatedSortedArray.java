package library.binarysearch;

import java.util.*;

public class RotatedSortedArray {

    // ---------------------------------------------------------------
    // A sorted array rotated at an unknown pivot, e.g.
    // [4,5,6,7,0,1,2]. Still O(log n): at each step at least one half
    // [lo..mid] or [mid..hi] is sorted; decide which, then check
    // whether target lies inside that sorted half.
    //
    // Assumes DISTINCT values. With duplicates the "which half is
    // sorted" test can be ambiguous (a[lo]==a[mid]==a[hi]); you shrink
    // both ends by one, degrading to O(n) worst case.
    // ---------------------------------------------------------------

    public static int search(int[] a, int target) {
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == target) return mid;

            if (a[lo] <= a[mid]) {          // left half [lo..mid] is sorted
                if (a[lo] <= target && target < a[mid]) hi = mid - 1;
                else lo = mid + 1;
            } else {                         // right half [mid..hi] is sorted
                if (a[mid] < target && target <= a[hi]) lo = mid + 1;
                else hi = mid - 1;
            }
        }
        return -1;
    }

    // Index of the minimum (= the rotation pivot). Distinct values.
    public static int findMin(int[] a) {
        int lo = 0, hi = a.length - 1;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            // If a[mid] > a[hi], the min must be to the RIGHT of mid.
            if (a[mid] > a[hi]) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    public static void main(String[] args) {
        int[] a = {4, 5, 6, 7, 0, 1, 2};

        System.out.println(search(a, 0));  // expect: 4
        System.out.println(search(a, 4));  // expect: 0
        System.out.println(search(a, 3));  // expect: -1

        int minIdx = findMin(a);
        System.out.println(minIdx);        // expect: 4
        System.out.println(a[minIdx]);     // expect: 0

        // Not actually rotated -> min is at index 0
        System.out.println(findMin(new int[]{1, 2, 3})); // expect: 0
    }
}
