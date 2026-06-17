package library.heaps;

import java.util.*;

public class MeetingRoomsII {

    // ---------------------------------------------------------------
    // Minimum number of rooms = maximum number of meetings running at
    // once. Sort by start time, then keep a MIN-heap of END times of
    // ongoing meetings. For each meeting, if the earliest-ending room
    // is already free (its end <= this start) reuse it (poll); always
    // push this meeting's end. Heap size = rooms in use; its peak is
    // the answer.
    //
    // O(n log n) time. (`end <= start` treats touching intervals as
    // non-overlapping; use `<` if a meeting ending at t conflicts
    // with one starting at t.)
    // ---------------------------------------------------------------
    public static int minRooms(int[][] intervals) {
        if (intervals.length == 0) return 0;

        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));

        PriorityQueue<Integer> endTimes = new PriorityQueue<>(); // min-heap of end times
        for (int[] meeting : intervals) {
            int start = meeting[0], end = meeting[1];
            if (!endTimes.isEmpty() && endTimes.peek() <= start) {
                endTimes.poll(); // a room freed up -> reuse it
            }
            endTimes.offer(end);
        }

        return endTimes.size();
    }

    public static void main(String[] args) {
        System.out.println("[[0,30],[5,10],[15,20]] (expect 2): "
                + minRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}}));
        System.out.println("[[7,10],[2,4]] (expect 1): "
                + minRooms(new int[][]{{7, 10}, {2, 4}}));
        System.out.println("[[1,5],[2,6],[3,7]] all overlap (expect 3): "
                + minRooms(new int[][]{{1, 5}, {2, 6}, {3, 7}}));
        System.out.println("[] (expect 0): " + minRooms(new int[][]{}));
    }
}
