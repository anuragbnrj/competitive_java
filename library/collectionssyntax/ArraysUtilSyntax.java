package library.collectionssyntax;

import java.util.*;

public class ArraysUtilSyntax {

    // ---------------------------------------------------------------
    // java.util.Arrays - static helpers for raw arrays.
    //
    // GOTCHAS:
    //   - Arrays.sort(int[]) takes NO comparator and only sorts
    //     ascending. To sort descending you must use a boxed
    //     Integer[] with Collections.reverseOrder().
    //   - Arrays.binarySearch needs a SORTED array; on a miss it
    //     returns -(insertionPoint) - 1.
    //   - Use Arrays.toString for 1-D, Arrays.deepToString for nested.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        int[] a = {5, 2, 8, 1};

        Arrays.sort(a);                         // ascending, in place
        System.out.println(Arrays.toString(a)); // expect: [1, 2, 5, 8]

        // Descending needs boxing (no comparator for primitives)
        Integer[] b = {5, 2, 8, 1};
        Arrays.sort(b, Collections.reverseOrder());
        System.out.println(Arrays.toString(b)); // expect: [8, 5, 2, 1]

        // binarySearch (array must be sorted ascending)
        System.out.println(Arrays.binarySearch(a, 5));  // expect: 2 (index)
        System.out.println(Arrays.binarySearch(a, 3));  // expect: -3 (-(ip=2)-1)

        // fill / copyOf / copyOfRange
        int[] f = new int[3];
        Arrays.fill(f, 7);
        System.out.println(Arrays.toString(f));            // expect: [7, 7, 7]
        System.out.println(Arrays.toString(Arrays.copyOf(a, 6)));        // expect: [1, 2, 5, 8, 0, 0]
        System.out.println(Arrays.toString(Arrays.copyOfRange(a, 1, 3)));// expect: [2, 5]

        // equals (element-wise) and deepToString (nested)
        System.out.println(Arrays.equals(new int[]{1, 2}, new int[]{1, 2})); // expect: true
        int[][] grid = {{1, 2}, {3, 4}};
        System.out.println(Arrays.deepToString(grid)); // expect: [[1, 2], [3, 4]]

        // Sum via stream
        System.out.println(Arrays.stream(a).sum()); // expect: 16
    }
}
