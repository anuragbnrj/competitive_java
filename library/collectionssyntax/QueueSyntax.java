package library.collectionssyntax;

import java.util.*;

public class QueueSyntax {

    // ---------------------------------------------------------------
    // Queue (FIFO) - the structure behind BFS. Use ArrayDeque.
    //   - offer / add : enqueue at the tail
    //   - poll        : dequeue from the head (null if empty)
    //   - peek        : look at the head    (null if empty)
    //
    // Prefer offer/poll/peek (return null/false) over add/remove/
    // element (which THROW on empty/full) unless you want the throw.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        Queue<Integer> queue = new ArrayDeque<>();

        queue.offer(1);         // [1]
        queue.offer(2);         // [1, 2]
        queue.offer(3);         // [1, 2, 3]

        System.out.println(queue.peek());  // expect: 1 (head, not removed)
        System.out.println(queue.poll());  // expect: 1 (removed)
        System.out.println(queue.poll());  // expect: 2
        System.out.println(queue.size());  // expect: 1

        // poll() returns null on empty (no exception)
        queue.poll();                       // removes 3
        System.out.println(queue.poll());  // expect: null
    }
}
