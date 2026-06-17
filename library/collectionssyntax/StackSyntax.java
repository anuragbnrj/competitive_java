package library.collectionssyntax;

import java.util.*;

public class StackSyntax {

    // ---------------------------------------------------------------
    // Stack (LIFO). Use ArrayDeque, NOT the legacy java.util.Stack.
    //   - Deque as stack: push / pop / peek  (all at the head)
    //   - java.util.Stack extends Vector, is synchronized (slower),
    //     and iterates BOTTOM-to-top (surprising). Avoid it.
    //
    // GOTCHA: ArrayDeque forbids null elements. pop() on an empty
    // deque throws NoSuchElementException; peek() returns null.
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        Deque<Integer> stack = new ArrayDeque<>();

        stack.push(1);          // [1]
        stack.push(2);          // [2, 1]  (2 is on top)
        stack.push(3);          // [3, 2, 1]

        System.out.println(stack.peek());  // expect: 3 (top, not removed)
        System.out.println(stack.pop());   // expect: 3 (removed)
        System.out.println(stack.pop());   // expect: 2
        System.out.println(stack.size());  // expect: 1
        System.out.println(stack.isEmpty());// expect: false

        // Safe emptiness check before popping
        while (!stack.isEmpty()) {
            System.out.println("drain: " + stack.pop()); // expect: drain: 1
        }
    }
}
