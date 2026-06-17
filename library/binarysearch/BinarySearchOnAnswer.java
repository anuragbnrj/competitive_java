package library.binarysearch;

import java.util.*;
import java.util.function.LongPredicate;

public class BinarySearchOnAnswer {

    // ---------------------------------------------------------------
    // "Binary search on the answer": the search space is the ANSWER
    // value, not an array index. Works whenever feasibility is
    // MONOTONIC - if x works, everything on one side of x also works:
    //
    //   feasible:  false false false TRUE TRUE TRUE
    //                              ^ smallest feasible answer
    //
    // smallestFeasible finds the first x in [lo, hi] with feasible(x).
    // For a "maximize" problem, flip the predicate / mirror the search
    // (largestFeasible below).
    //
    // Use `long` for the range and any internal sums to avoid overflow.
    // ---------------------------------------------------------------

    // First x in [lo, hi] with feasible(x) == true (assumes one exists).
    public static long smallestFeasible(long lo, long hi, LongPredicate feasible) {
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (feasible.test(mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    // Last x in [lo, hi] with feasible(x) == true. Note the +1 bias on
    // mid so it rounds UP - otherwise lo = mid loops forever.
    public static long largestFeasible(long lo, long hi, LongPredicate feasible) {
        while (lo < hi) {
            long mid = lo + (hi - lo + 1) / 2;
            if (feasible.test(mid)) lo = mid;
            else hi = mid - 1;
        }
        return lo;
    }

    // ----- Worked example: Koko Eating Bananas (LC 875) -----
    // Min eating speed k so all piles are eaten within `hours` hours.
    // hoursNeeded(k) is non-increasing in k -> "can finish in time" is
    // monotonic, so we binary-search the smallest feasible speed.
    public static int minEatingSpeed(int[] piles, int hours) {
        int maxPile = 0;
        for (int p : piles) maxPile = Math.max(maxPile, p);

        return (int) smallestFeasible(1, maxPile, k -> hoursNeeded(piles, k) <= hours);
    }

    private static long hoursNeeded(int[] piles, long k) {
        long hours = 0;
        for (int p : piles) hours += (p + k - 1) / k; // ceil(p / k)
        return hours;
    }

    public static void main(String[] args) {
        // Smallest x in [0, 100] with x*x >= 50 -> 8 (8*8=64, 7*7=49)
        System.out.println(smallestFeasible(0, 100, x -> x * x >= 50)); // expect: 8

        // Largest x in [0, 100] with x*x <= 50 -> 7
        System.out.println(largestFeasible(0, 100, x -> x * x <= 50));  // expect: 7

        System.out.println(minEatingSpeed(new int[]{3, 6, 7, 11}, 8)); // expect: 4
        System.out.println(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 5)); // expect: 30
        System.out.println(minEatingSpeed(new int[]{30, 11, 23, 4, 20}, 6)); // expect: 23
    }
}
