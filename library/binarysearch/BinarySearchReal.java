package library.binarysearch;

import java.util.*;
import java.util.function.DoublePredicate;

public class BinarySearchReal {

    // ---------------------------------------------------------------
    // Binary search over a REAL (continuous) range. Use a FIXED number
    // of iterations (~100 doublings shrink the interval below any
    // practical epsilon) instead of `while (hi - lo > eps)`, which can
    // loop forever due to floating-point rounding.
    //
    // smallestFeasible finds the boundary x where a monotonic predicate
    // flips false -> true.
    // ---------------------------------------------------------------

    public static double smallestFeasible(double lo, double hi, DoublePredicate feasible) {
        for (int iter = 0; iter < 100; iter++) {
            double mid = lo + (hi - lo) / 2;
            if (feasible.test(mid)) hi = mid;
            else lo = mid;
        }
        return lo; // lo and hi have converged
    }

    // sqrt via real binary search: smallest x with x*x >= n.
    public static double sqrt(double n) {
        double hi = Math.max(n, 1.0); // sqrt(n) <= n for n >= 1, and <= 1 for n < 1
        return smallestFeasible(0, hi, x -> x * x >= n);
    }

    public static void main(String[] args) {
        System.out.printf("%.4f%n", sqrt(2));   // expect: 1.4142
        System.out.printf("%.4f%n", sqrt(9));   // expect: 3.0000
        System.out.printf("%.4f%n", sqrt(0.25)); // expect: 0.5000

        // Smallest x with x^3 >= 27 -> 3.0
        System.out.printf("%.4f%n", smallestFeasible(0, 100, x -> x * x * x >= 27)); // expect: 3.0000
    }
}
