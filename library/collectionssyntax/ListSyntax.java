package library.collectionssyntax;

import java.util.*;

public class ListSyntax {

    // ---------------------------------------------------------------
    // ArrayList - dynamic array, O(1) amortized add/get, O(n) remove
    // at an index (shifts elements).
    //
    // GOTCHAS (the classic OA traps):
    //   - list.remove(int) removes by INDEX; list.remove(Object)
    //     removes by value. With an Integer list, remove(2) deletes
    //     index 2, NOT the value 2 -> cast: remove(Integer.valueOf(2)).
    //   - Arrays.asList(arr) on a PRIMITIVE int[] gives List<int[]>
    //     of size 1, not a list of ints.
    //   - Arrays.asList(...) is FIXED-SIZE; add/remove throw
    //     UnsupportedOperationException. List.of(...) is fully
    //     immutable.
    //   - subList(a, b) is a VIEW backed by the original list.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>(List.of(10, 20, 30, 40));

        // remove(int index) vs remove(Object)
        list.remove(1);                         // removes index 1 (value 20)
        System.out.println(list);               // expect: [10, 30, 40]
        list.remove(Integer.valueOf(30));       // removes the value 30
        System.out.println(list);               // expect: [10, 40]

        // Sort with a Comparator (descending)
        List<Integer> nums = new ArrayList<>(List.of(3, 1, 2));
        nums.sort(Comparator.reverseOrder());
        System.out.println(nums);               // expect: [3, 2, 1]
        nums.sort(Comparator.naturalOrder());
        System.out.println(nums);               // expect: [1, 2, 3]

        // indexOf / contains
        System.out.println(nums.indexOf(2));    // expect: 1
        System.out.println(nums.contains(5));   // expect: false

        // subList is a VIEW: writes pass through to the backing list
        List<Integer> view = nums.subList(0, 2);
        view.set(0, 99);
        System.out.println(nums);               // expect: [99, 2, 3]

        // Arrays.asList is fixed-size -> add() throws
        List<Integer> fixed = Arrays.asList(1, 2, 3);
        try {
            fixed.add(4);
        } catch (UnsupportedOperationException e) {
            System.out.println("Arrays.asList is fixed-size"); // expect this line
        }
    }
}
