package library.monotonic;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

public class MonotonicStack {

    // For each i, the index of the nearest j > i with a[j] > a[i], or -1 if none.
    // O(n): the stack holds indices whose "next greater" isn't found yet, and
    // their values are non-increasing bottom-to-top.
    public static int[] nextGreaterIndex(int[] a) {
        int n = a.length;
        int[] result = new int[n];
        Arrays.fill(result, -1);
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && a[stack.peek()] < a[i]) {
                result[stack.pop()] = i;
            }
            stack.push(i);
        }
        return result;
    }

    // For each i, the index of the nearest j < i with a[j] < a[i], or -1 if none.
    // Mirror of the above, scanning left-to-right but looking backward.
    public static int[] prevSmallerIndex(int[] a) {
        int n = a.length;
        int[] result = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && a[stack.peek()] >= a[i]) stack.pop();
            result[i] = stack.isEmpty() ? -1 : stack.peek();
            stack.push(i);
        }
        return result;
    }

    public static void main(String[] args) {
        int[] a = {2, 1, 2, 4, 3};
        System.out.println(Arrays.toString(nextGreaterIndex(a))); // expect: [3, 2, 3, -1, -1]
        System.out.println(Arrays.toString(prevSmallerIndex(a)));  // expect: [-1, -1, 1, 2, 2]
    }
}
