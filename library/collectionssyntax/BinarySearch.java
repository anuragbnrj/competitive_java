package library.collectionssyntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BinarySearch {

    // ---------------------------------------------------------------
    // Collections.binarySearch (and the array twin Arrays.binarySearch)
    // - the JDK's built-in binary search over a SORTED list/array.
    //
    //   - Found     -> returns the index of the element.
    //   - Not found -> returns -(insertionPoint) - 1, where
    //     insertionPoint is where the value WOULD be inserted to keep
    //     order. Recover it as: insertionPoint = -(result) - 1.
    //
    // GOTCHAS:
    //   - The input MUST be sorted; otherwise the result is undefined.
    //   - With duplicates, WHICH equal index is returned is not
    //     specified - hand-roll lower/upper bound if you need a side.
    //   - This is the *API*; the binary-search-on-answer *algorithm*
    //     (predicate + bounds) is a separate topic.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        List<Integer> al = new ArrayList<>(List.of(1, 2, 3, 10, 20));

        // 10 is present at index 3
        System.out.println(Collections.binarySearch(al, 10)); // expect: 3

        // 13 is absent; it would insert at index 4 -> -(4)-1 = -5
        int result = Collections.binarySearch(al, 13);
        System.out.println(result);                            // expect: -5
        System.out.println("insertion point: " + (-(result) - 1)); // expect: 4
    }
}