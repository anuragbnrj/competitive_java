package library.binarysearch;

import java.util.*;

public class BinarySearchBasics {

    // ---------------------------------------------------------------
    // The foundational binary-search templates on a SORTED int[].
    //
    //   search     : exact match, returns an index or -1.
    //   lowerBound : first index i with a[i] >= target  (in [0, n]).
    //   upperBound : first index i with a[i] >  target  (in [0, n]).
    //   firstTrue  : generic - smallest i where a monotonic predicate
    //                flips false -> true.
    //
    // lowerBound/upperBound use the HALF-OPEN range [lo, hi): hi starts
    // at n (a valid "not found / past the end" answer), the loop runs
    // while lo < hi, and exactly one side moves each step:
    //   - a[mid] fails the test -> lo = mid + 1
    //   - a[mid] passes         -> hi = mid
    //
    // Derived in O(log n):
    //   first occurrence of x = lowerBound(x)  (if a[idx] == x)
    //   last  occurrence of x = upperBound(x) - 1
    //   count of x            = upperBound(x) - lowerBound(x)
    //
    // GOTCHA: compute mid as lo + (hi - lo) / 2, never (lo + hi) / 2,
    // which can overflow int for large indices.
    // ---------------------------------------------------------------

    public static int search(int[] a, int target) {
        int lo = 0, hi = a.length - 1; // closed range [lo, hi]
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] == target) return mid;
            if (a[mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return -1;
    }

    public static int lowerBound(int[] a, int target) {
        int lo = 0, hi = a.length; // half-open [lo, hi)
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] < target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    public static int upperBound(int[] a, int target) {
        int lo = 0, hi = a.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (a[mid] <= target) lo = mid + 1;
            else hi = mid;
        }
        return lo;
    }

    // Smallest index in [0, n] where pred(i) is true, assuming pred is
    // monotonic: false...false, true...true. Returns n if never true.
    public static int firstTrue(int n, java.util.function.IntPredicate pred) {
        int lo = 0, hi = n;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (pred.test(mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    public static void main(String[] args) {
        int[] a = {1, 2, 2, 2, 3, 5};

        System.out.println(search(a, 3));   // expect: 4
        System.out.println(search(a, 4));   // expect: -1

        System.out.println(lowerBound(a, 2)); // expect: 1 (first index >= 2)
        System.out.println(upperBound(a, 2)); // expect: 4 (first index > 2)
        System.out.println("count of 2: " + (upperBound(a, 2) - lowerBound(a, 2))); // expect: 3

        // target larger than everything -> bounds return n
        System.out.println(lowerBound(a, 9)); // expect: 6

        // firstTrue: smallest i with a[i] >= 5  (== lowerBound(5))
        System.out.println(firstTrue(a.length, i -> a[i] >= 5)); // expect: 5
    }
}
