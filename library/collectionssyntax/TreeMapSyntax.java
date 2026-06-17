package library.collectionssyntax;

import java.util.TreeMap;

public class TreeMapSyntax {

    // ---------------------------------------------------------------
    // TreeMap - a sorted map (red-black tree). O(log n) get/put, keys
    // iterated in ascending order. Use it when you need order +
    // nearest-key queries: ceiling/floor/higher/lower, firstKey/
    // lastKey, headMap/tailMap/subMap.
    //
    //   ceilingKey(x) : smallest key >= x
    //   higherKey(x)  : smallest key >  x
    //   floorKey(x)   : largest  key <= x
    //   lowerKey(x)   : largest  key <  x
    //
    // GOTCHAS:
    //   - These return null when no such key exists - guard before
    //     unboxing (unboxing null -> NPE).
    //   - TreeMap rejects null keys (NullPointerException).
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        TreeMap<Integer, Integer> treeMap = new TreeMap<>();
        for (int i = 1; i <= 10; i++) treeMap.put(i, i * 10);

        // Iterated in ascending key order (unlike HashMap)
        System.out.println(treeMap.keySet());   // expect: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
        System.out.println(treeMap.firstKey() + " " + treeMap.lastKey()); // expect: 1 10

        int searchKey = 4;
        System.out.println(treeMap.ceilingKey(searchKey)); // expect: 4 (>= 4)
        System.out.println(treeMap.higherKey(searchKey));  // expect: 5 (> 4)
        System.out.println(treeMap.floorKey(searchKey));   // expect: 4 (<= 4)
        System.out.println(treeMap.lowerKey(searchKey));   // expect: 3 (< 4)

        // No such key -> null (e.g. nothing strictly above the max)
        System.out.println(treeMap.higherKey(10));         // expect: null

        // Range views (subMap is [from, to) by default)
        System.out.println(treeMap.subMap(3, 6).keySet()); // expect: [3, 4, 5]
    }

}
