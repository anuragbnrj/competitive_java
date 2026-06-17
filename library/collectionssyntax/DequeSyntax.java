package library.collectionssyntax;

import java.util.*;

public class DequeSyntax {

    // ---------------------------------------------------------------
    // Deque (double-ended queue) - ArrayDeque. Add/remove/peek at
    // BOTH ends in O(1). Backs the stack and queue idioms, and is the
    // structure for 0-1 BFS and the sliding-window-maximum monotonic
    // deque.
    //
    //   head end: addFirst / pollFirst / peekFirst
    //   tail end: addLast  / pollLast  / peekLast
    //
    // (push==addFirst, pop==pollFirst; offer==addLast, poll==pollFirst.)
    // GOTCHA: ArrayDeque forbids null; poll*/peek* return null on empty.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        Deque<Integer> dq = new ArrayDeque<>();

        dq.addLast(2);          // [2]
        dq.addLast(3);          // [2, 3]
        dq.addFirst(1);         // [1, 2, 3]

        System.out.println(dq.peekFirst()); // expect: 1
        System.out.println(dq.peekLast());  // expect: 3

        System.out.println(dq.pollFirst()); // expect: 1  -> [2, 3]
        System.out.println(dq.pollLast());  // expect: 3  -> [2]
        System.out.println(dq);             // expect: [2]

        // Empty-end reads return null (no exception)
        dq.pollFirst();                      // removes 2
        System.out.println(dq.peekFirst()); // expect: null
    }
}
