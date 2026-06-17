# Heaps / Priority Queue — revision notes

Pre-OA cheat sheet for `library/heaps/`. Each template is a self-contained,
runnable class (`main` has a worked self-test). Verify any one with:

```
javac library/heaps/KthLargest.java && java library.heaps.KthLargest
```

## Java `PriorityQueue` cheat-sheet

- **Default is a MIN-heap** (`new PriorityQueue<>()` → smallest on top).
- **Max-heap**: `new PriorityQueue<>(Collections.reverseOrder())` or
  `new PriorityQueue<>((a, b) -> b - a)`.
- **By key** (objects / `int[]`): `new PriorityQueue<>((a, b) -> a[0] - b[0])`
  (min by first field). **Tie-break** by chaining: `a[0] != b[0] ? a[0] - b[0]
  : a[1] - b[1]`.
- Ops: `offer`/`add` and `poll` are **O(log n)**; `peek` is **O(1)**; building
  from a collection via the constructor is **O(n)** (heapify).
- `poll()` / `peek()` on an **empty** heap return **`null`** (autounbox → NPE).
  Guard with `isEmpty()`.
- **Iteration order is NOT sorted** — `for (x : pq)` walks the heap array in
  arbitrary order. Only repeated `poll()` yields sorted order.
- **No decrease-key** in Java's PQ → use **lazy deletion** (push an updated copy,
  skip stale entries on poll). See `library/graphs/Dijkstra.java`.
- **Comparator overflow**: `(a, b) -> a - b` is wrong for large/negative ints
  (subtraction overflows). Prefer **`Integer.compare(a, b)`** / `Long.compare`.

## Decision guide — which heap pattern?

| Problem signal | Technique | File |
| --- | --- | --- |
| "k-th **largest**" / "k **largest**" | min-heap of size k | `KthLargest.java` |
| "k-th **smallest**" / "k **smallest**" | max-heap of size k | (mirror of above) |
| "k most **frequent**" | freq map + size-k heap | `TopKFrequent.java` |
| "k **closest** / nearest" | max-heap of size k (by dist²) | `KClosestPoints.java` |
| "**merge** k sorted lists/arrays" | min-heap k-way merge | `MergeKSorted.java` |
| "**median** of a stream" / running middle | two heaps | `MedianFinder.java` |
| "min **rooms** / max concurrent intervals" | min-heap of end times | `MeetingRoomsII.java` |
| "**merge** overlapping intervals" / "min removals so the rest don't overlap" | sort + linear scan (**not** a heap!) | `../intervals/Intervals.java` |
| "sliding window **maximum**" | monotonic deque (**not** a heap!) | `../monotonic/MonotonicDeque.java` |
| weighted shortest path (non-negative) | heap = Dijkstra | `../graphs/Dijkstra.java` |
| MST by growing a tree | heap = Prim | `../graphs/Mst.java` |

**The size-K rule of thumb:** to keep the K *largest*, use a *min*-heap of size K
(evict the smallest); to keep the K *smallest*, use a *max*-heap of size K (evict
the largest). The element on top is the K-th one.

---

## K-th largest — `KthLargest.java`

**What & when.** k-th largest (or k largest) element of an array / stream.
**Complexity.** O(n log k) time, O(k) space.
**Edge cases & gotchas.**
- k-th *largest* ⇒ *min*-heap of size k (the classic inversion). Mirror it for
  k-th smallest (max-heap size k).
- Duplicates count by position: 2nd largest of `[3,3,3]` is `3`, not "the 2nd
  distinct".
- One-shot query on a static array? **QuickSelect** is O(n) average (O(n²) worst);
  the heap wins for **streams** or when k ≪ n and you want simplicity.

**Active recall.**
- Q: Why a min-heap (not max-heap) for the k-th *largest*? → A: A size-k min-heap
  retains exactly the k largest seen; its smallest element (top) is therefore the
  k-th largest.
- Q: Heap vs QuickSelect — when each? → A: QuickSelect O(n) avg for a one-shot
  static array; heap for streaming data or simple O(n log k) with small k.

**Practice.**
- [Kth Largest Element in an Array](https://leetcode.com/problems/kth-largest-element-in-an-array/)
- [Kth Largest Element in a Stream](https://leetcode.com/problems/kth-largest-element-in-a-stream/)

---

## Top K frequent — `TopKFrequent.java`

**What & when.** The k most frequently occurring elements.
**Complexity.** O(n log k) time. (Bucket sort → O(n).)
**Edge cases & gotchas.**
- Heap orders **by frequency** (min-heap), not by value — keep a freq `HashMap`
  and compare with `freq.get(a) - freq.get(b)`.
- O(n) alternative: bucket the keys by frequency into indices `0..n`, then read
  buckets from the top down. Use it when k is large / close to the number of
  distinct keys.
- Same shape solves "top k frequent **words**" (add a lexicographic tie-break).

**Active recall.**
- Q: How do you get this down to O(n)? → A: Bucket sort by frequency (frequencies
  are bounded by n), then collect from highest bucket down — no heap.

**Practice.**
- [Top K Frequent Elements](https://leetcode.com/problems/top-k-frequent-elements/)
- [Top K Frequent Words](https://leetcode.com/problems/top-k-frequent-words/)
- [Sort Characters By Frequency](https://leetcode.com/problems/sort-characters-by-frequency/)

---

## K closest points — `KClosestPoints.java`

**What & when.** The k points nearest the origin (or any reference point).
**Complexity.** O(n log k) time, O(k) space.
**Edge cases & gotchas.**
- Keep a **max-heap of size k** by distance; the farthest kept point is on top and
  evicted first.
- Compare **squared** distance — never `sqrt` (slow, float error). Use **`long`**
  (`(long)x*x + (long)y*y`) so large coordinates don't overflow `int`.
- The order *within* the returned k is unspecified.

**Active recall.**
- Q: Why compare squared distance instead of actual distance? → A: `sqrt` is
  monotonic, so squared distance gives the same ordering without float error or
  cost; cast to `long` to avoid overflow.

**Practice.**
- [K Closest Points to Origin](https://leetcode.com/problems/k-closest-points-to-origin/)

---

## Merge k sorted — `MergeKSorted.java`

**What & when.** Merge k already-sorted lists/arrays into one sorted sequence.
**Complexity.** O(N log k), N = total elements; O(k) heap space.
**Edge cases & gotchas.**
- Heap holds **one entry per array** (its current front) as
  `{value, arrayIdx, elemIdx}`; after polling, push the next element from the
  *same* array.
- Skip **empty** input arrays when seeding the heap (don't index `arr[0]`).
- For linked lists the idea is identical — store the node instead of indices.

**Active recall.**
- Q: Why is the heap size only k, not N? → A: At any moment it holds just the
  current head of each of the k arrays; each poll is replaced by at most one push.

**Practice.**
- [Merge k Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/)
- [Kth Smallest Element in a Sorted Matrix](https://leetcode.com/problems/kth-smallest-element-in-a-sorted-matrix/)
- [Find K Pairs with Smallest Sums](https://leetcode.com/problems/find-k-pairs-with-smallest-sums/)

---

## Median of a stream — `MedianFinder.java`

**What & when.** Running median as numbers arrive (data stream / online).
**Complexity.** `addNum` O(log n), `findMedian` O(1).
**Edge cases & gotchas.**
- Two heaps: `lo` = **max-heap** (smaller half), `hi` = **min-heap** (larger half).
  Keep **`|lo| - |hi| ∈ {0, 1}`** (lo carries the extra on odd counts).
- Standard add: push to `lo`, move `lo.poll()` into `hi`, then if `hi` is bigger
  move its top back to `lo` — this both orders and rebalances.
- Median: odd ⇒ `lo.peek()`; even ⇒ `(lo.peek() + hi.peek()) / 2.0` (the `.0`
  matters — integer division otherwise truncates).

**Active recall.**
- Q: What two invariants keep findMedian O(1)? → A: (1) every element of `lo` ≤
  every element of `hi`; (2) their sizes differ by at most 1. The median is then
  always at the heap tops.

**Interviewer follow-up.**
- "Now support `remove(x)` (e.g. sliding-window median)" → Java's heaps can't
  remove an arbitrary element in O(log n); switch to two `TreeMap`-based
  multisets (see TODO) sized like `lo`/`hi`, using `firstKey`/`lastKey` in place
  of `peek`.

**Practice.**
- [Find Median from Data Stream](https://leetcode.com/problems/find-median-from-data-stream/)
- [Sliding Window Median](https://leetcode.com/problems/sliding-window-median/)
- [IPO](https://leetcode.com/problems/ipo/)
- Deeper (CSES):
  - [Stick Lengths](https://cses.fi/problemset/task/1074) (median minimizes total cost)

---

## Meeting Rooms II — `MeetingRoomsII.java`

**What & when.** Minimum rooms for overlapping intervals = max concurrency. The
heap-of-end-times pattern for any "max simultaneous / min resources" problem.
**Complexity.** O(n log n).
**Edge cases & gotchas.**
- Sort by **start**, keep a **min-heap of end times**. Before adding a meeting,
  free the earliest-ending room if `endTimes.peek() <= start`.
- **Touching intervals**: `end <= start` treats `[1,5]` and `[5,8]` as
  non-overlapping (one room). Use `<` if ending and starting at the same instant
  should conflict — confirm from the problem statement.
- Equivalent **sweep-line**: split into `+1` at starts / `-1` at ends, sort, track
  the running max (no heap). Good alternative to remember.

**Active recall.**
- Q: Why does the heap of end times give the room count? → A: Its size is the
  number of meetings currently running; we only add a room when no existing one has
  freed up, so the peak size is the minimum rooms needed.

**Practice.**
- [Meeting Rooms II](https://leetcode.com/problems/meeting-rooms-ii/)
- [Minimum Number of Arrows to Burst Balloons](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/)
- [Car Pooling](https://leetcode.com/problems/car-pooling/)
- [Task Scheduler](https://leetcode.com/problems/task-scheduler/)
- Deeper (CSES):
  - [Room Allocation](https://cses.fi/problemset/task/1164) (= Meeting Rooms II)
  - [Tasks and Deadlines](https://cses.fi/problemset/task/1630) (greedy + heap/sort)

---

## TODO — future additions

Heap-adjacent topics worth a later pass (same template + notes treatment):

- **Indexed priority queue / decrease-key** — a real heap with `decreaseKey` for
  textbook Dijkstra/Prim (vs. the lazy-deletion trick used now).
- **`TreeMap` as a multiset** — order statistics with arbitrary deletion (e.g.
  sliding-window max/median where elements leave from the *back*).
- Cross-references already live elsewhere: **Dijkstra** (`../graphs/Dijkstra.java`)
  and **Prim** (`../graphs/Mst.java`) are heap algorithms; **sliding-window
  maximum** is a monotonic deque, not a heap — see `../monotonic/`.

## Cross-cutting reminders

- **Min vs max**: default PQ is min-heap; flip with `Collections.reverseOrder()`.
- **Comparator overflow**: use `Integer.compare` / `Long.compare`, not `a - b`.
- **Size-K trick**: keep K largest with a *min*-heap of size K (and vice-versa).
- **No sorted iteration**: only `poll()` returns elements in order.
- **Overflow in keys/sums**: cast to `long` for distances and running sums.
- **Empty-heap guard**: `poll()`/`peek()` return `null` → check `isEmpty()`.
- **Sliding-window max is NOT a heap problem** — a heap gives O(n log k); a
  monotonic deque (`../monotonic/MonotonicDeque.java`) gives O(n).
