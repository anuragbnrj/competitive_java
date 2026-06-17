package library.prefixsum;

import java.util.Arrays;

public class PrefixSum {

    // 1D prefix sums: prefix[i] = sum of a[0...i-1] (prefix[0] = 0).
    // Range sum [l, r] inclusive = prefix[r+1] - prefix[l]. O(n) build, O(1) query.
    public static int[] buildPrefix(int[] a) {
        int[] prefix = new int[a.length + 1];
        for (int i = 0; i < a.length; i++) prefix[i + 1] = prefix[i] + a[i];
        return prefix;
    }

    public static int rangeSum(int[] prefix, int l, int r) {
        return prefix[r + 1] - prefix[l];
    }

    // 2D prefix sums: prefix[i][j] = sum of grid[0...i-1][0...j-1].
    // Range sum over rows [r1,r2], cols [c1,c2] inclusive in O(1).
    public static int[][] buildPrefix2D(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] prefix = new int[m + 1][n + 1];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                prefix[i + 1][j + 1] = prefix[i][j + 1] + prefix[i + 1][j] - prefix[i][j] + grid[i][j];
            }
        }
        return prefix;
    }

    public static int rangeSum2D(int[][] prefix, int r1, int c1, int r2, int c2) {
        return prefix[r2 + 1][c2 + 1] - prefix[r1][c2 + 1] - prefix[r2 + 1][c1] + prefix[r1][c1];
    }

    // Difference array: apply range-add updates {l, r, val} in O(1) each, then
    // a single prefix sum materializes the final array. O(n + q) total.
    public static int[] applyRangeUpdates(int n, int[][] updates) {
        int[] diff = new int[n + 1];
        for (int[] u : updates) {
            int l = u[0], r = u[1], val = u[2];
            diff[l] += val;
            diff[r + 1] -= val;
        }
        int[] result = new int[n];
        int running = 0;
        for (int i = 0; i < n; i++) {
            running += diff[i];
            result[i] = running;
        }
        return result;
    }

    public static void main(String[] args) {
        int[] a = {2, 4, 6, 8, 10};
        int[] prefix = buildPrefix(a);
        System.out.println(rangeSum(prefix, 1, 3)); // expect: 18 (4+6+8)
        System.out.println(rangeSum(prefix, 0, 4)); // expect: 30 (whole array)

        int[][] grid = {
            {1, 2, 3},
            {4, 5, 6},
            {7, 8, 9}
        };
        int[][] prefix2D = buildPrefix2D(grid);
        System.out.println(rangeSum2D(prefix2D, 1, 1, 2, 2)); // expect: 28 (5+6+8+9)
        System.out.println(rangeSum2D(prefix2D, 0, 0, 2, 2)); // expect: 45 (whole grid)

        // start [0,0,0,0,0]; +5 on [0,2] -> [5,5,5,0,0]; -2 on [1,3] -> [5,3,3,-2,0]
        int[][] updates = {{0, 2, 5}, {1, 3, -2}};
        System.out.println(Arrays.toString(applyRangeUpdates(5, updates))); // expect: [5, 3, 3, -2, 0]
    }
}
