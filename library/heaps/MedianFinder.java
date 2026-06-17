package library.heaps;

import java.util.*;

public class MedianFinder {

    // ---------------------------------------------------------------
    // Running median of a data stream with TWO heaps:
    //   lo = MAX-heap holding the smaller half
    //   hi = MIN-heap holding the larger half
    // Invariant: every element of lo <= every element of hi, and
    // sizes differ by at most 1 (lo may hold one extra).
    //
    // addNum O(log n), findMedian O(1).
    // ---------------------------------------------------------------
    private final PriorityQueue<Integer> lo = new PriorityQueue<>(Collections.reverseOrder());
    private final PriorityQueue<Integer> hi = new PriorityQueue<>();

    public void addNum(int num) {
        // Push to lo, then shuttle lo's max into hi to keep order...
        lo.offer(num);
        hi.offer(lo.poll());

        // ...then rebalance so lo carries the extra element when odd.
        if (hi.size() > lo.size()) {
            lo.offer(hi.poll());
        }
    }

    public double findMedian() {
        if (lo.size() > hi.size()) {
            return lo.peek();               // odd count -> middle is lo's max
        }
        return (lo.peek() + hi.peek()) / 2.0; // even count -> average of two middles
    }

    public static void main(String[] args) {
        MedianFinder mf = new MedianFinder();
        mf.addNum(1);
        mf.addNum(2);
        System.out.println("Median of {1,2} (expect 1.5): " + mf.findMedian());
        mf.addNum(3);
        System.out.println("Median of {1,2,3} (expect 2.0): " + mf.findMedian());
        mf.addNum(4);
        mf.addNum(5);
        System.out.println("Median of {1,2,3,4,5} (expect 3.0): " + mf.findMedian());
    }
}
