package library.collectionssyntax;

import java.util.TreeSet;

public class TreeSetSyntax {

    // ---------------------------------------------------------------
    // TreeSet - a sorted, de-duplicated set (red-black tree). O(log n)
    // add/contains, iterated in ascending order. The set analogue of
    // TreeMap; use it for "sorted unique" + nearest-element queries.
    //
    //   ceiling(x) : smallest element >= x
    //   higher(x)  : smallest element >  x
    //   floor(x)   : largest  element <= x
    //   lower(x)   : largest  element <  x
    //
    // GOTCHAS:
    //   - These return null when no such element exists - guard before
    //     unboxing.
    //   - TreeSet rejects null elements (NullPointerException).
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        TreeSet<Integer> treeSet = new TreeSet<>();
        for (int i = 1; i <= 10; i++) treeSet.add(i);
        treeSet.add(5);  // duplicate ignored

        System.out.println(treeSet);             // expect: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        System.out.println(treeSet.first() + " " + treeSet.last()); // expect: 1 10

        int x = 3;
        System.out.println(treeSet.ceiling(x));  // expect: 3 (>= 3)
        System.out.println(treeSet.higher(x));   // expect: 4 (> 3)
        System.out.println(treeSet.floor(x));    // expect: 3 (<= 3)
        System.out.println(treeSet.lower(x));    // expect: 2 (< 3)

        // No such element -> null
        System.out.println(treeSet.lower(1));    // expect: null
    }

}
