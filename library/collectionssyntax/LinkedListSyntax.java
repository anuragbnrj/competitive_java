package library.collectionssyntax;

import java.util.*;

public class LinkedListSyntax {

    // ---------------------------------------------------------------
    // LinkedList - a doubly-linked list implementing both List and
    // Deque.
    //
    // WHEN TO USE: almost never in practice. For a stack/queue/deque
    // prefer ArrayDeque (less memory, better cache behavior); for
    // index access prefer ArrayList. LinkedList only edges ahead when
    // you hold an Iterator and do many mid-list insert/removes via
    // that iterator. Included here for completeness.
    //
    // GOTCHA: get(i) is O(n) - it walks the links. Never index a
    // LinkedList in a loop.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        LinkedList<Integer> list = new LinkedList<>();

        list.addLast(2);        // [2]
        list.addFirst(1);       // [1, 2]
        list.addLast(3);        // [1, 2, 3]

        System.out.println(list.getFirst()); // expect: 1
        System.out.println(list.getLast());  // expect: 3
        System.out.println(list.get(1));     // expect: 2  (O(n) access!)

        list.removeFirst();                   // [2, 3]
        System.out.println(list);            // expect: [2, 3]

        // Used as a Deque (same API as ArrayDeque)
        Deque<Integer> asDeque = new LinkedList<>();
        asDeque.push(9);
        System.out.println(asDeque.pop());   // expect: 9
    }
}
