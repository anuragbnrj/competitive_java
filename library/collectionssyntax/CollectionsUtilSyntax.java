package library.collectionssyntax;

import java.util.*;

public class CollectionsUtilSyntax {

    // ---------------------------------------------------------------
    // java.util.Collections - static helpers for Collection types
    // (the counterpart to Arrays, which is for raw arrays).
    //
    // Common in OAs: sort (with/without comparator), reverse,
    // max/min, frequency, swap, nCopies, reverseOrder comparator.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(3, 1, 2, 1));

        Collections.sort(list);                  // ascending
        System.out.println(list);                // expect: [1, 1, 2, 3]

        Collections.sort(list, Collections.reverseOrder()); // descending
        System.out.println(list);                // expect: [3, 2, 1, 1]

        Collections.reverse(list);
        System.out.println(list);                // expect: [1, 1, 2, 3]

        System.out.println(Collections.max(list)); // expect: 3
        System.out.println(Collections.min(list)); // expect: 1
        System.out.println(Collections.frequency(list, 1)); // expect: 2

        Collections.swap(list, 0, 3);
        System.out.println(list);                // expect: [3, 1, 2, 1]

        // nCopies: immutable list of n identical elements
        System.out.println(Collections.nCopies(3, "x")); // expect: [x, x, x]

        // max with a custom comparator (longest string)
        List<String> words = List.of("a", "bbb", "cc");
        System.out.println(Collections.max(words, Comparator.comparingInt(String::length))); // expect: bbb
    }
}
