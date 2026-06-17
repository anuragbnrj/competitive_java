# Intervals — revision notes

Pre-OA cheat sheet for `library/intervals/`. The template is a self-contained,
runnable class (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/intervals/Intervals.java && java library.intervals.Intervals
```

## Decision guide — which technique?

| Problem signal | Technique | File |
| --- | --- | --- |
| "merge all overlapping intervals" | sort by start, extend last merged | `Intervals.java#merge` |
| "insert a new interval into an already-sorted, non-overlapping list" | three-phase scan (before / overlapping / after) | `Intervals.java#insert` |
| "minimum removals so the rest don't overlap" / "maximum non-overlapping intervals" | sort by **end**, greedy keep | `Intervals.java#eraseOverlapIntervals` |
| "minimum number of rooms/resources for overlapping intervals at once" | sort by start + min-heap of end times | `../heaps/MeetingRoomsII.java` |

**The unifying idea:** almost every interval problem starts with a **sort** —
the only question is *which* field to sort by (start for merging/inserting,
end for the greedy "max non-overlapping" family) — followed by a single linear
scan that tracks one running interval/value.

---

## Merge Intervals — `Intervals.java#merge`

**What & when.** Collapse a list of intervals into the minimal set of
non-overlapping intervals covering the same ranges.
**Complexity.** O(n log n) for the sort, O(n) for the scan.
**Edge cases & gotchas.**
- Sort by **start** first — without this, the single-pass merge doesn't work.
- The overlap test is `next.start <= current.end` (note `<=`, not `<`) —
  **touching** intervals like `[1,3]` and `[3,5]` are typically merged into
  `[1,5]`. If the problem treats touching intervals as separate, use `<`
  instead — confirm the problem's convention before coding.
- When extending, take `Math.max(current.end, next.end)` — don't just
  overwrite, since `next` could be entirely contained within `current`
  (e.g. `[1,10]` then `[2,3]`).
- Clone the input row before mutating it (`sorted[0].clone()`) if the caller's
  array shouldn't be modified.

**Active recall.**
- Q: Why must the array be sorted by start before merging in one pass? → A:
  Without sorting, an interval that should merge with `current` could appear
  anywhere later in the array — a single forward scan would miss it (and a
  multi-pass or nested-loop approach would be O(n²)).

**Practice.**
- [Merge Intervals](https://leetcode.com/problems/merge-intervals/) (Medium)
- [Meeting Rooms](https://leetcode.com/problems/meeting-rooms/) (Easy — premium; "any overlap at all?" = sort by start, check `intervals[i][0] < intervals[i-1][1]`)
- [Employee Free Time](https://leetcode.com/problems/employee-free-time/) (Hard, merge + gaps between merged intervals)

---

## Insert Interval — `Intervals.java#insert`

**What & when.** Given a list that's **already sorted and non-overlapping**,
insert a new interval and re-merge only where necessary, in O(n) (no need to
re-sort and re-merge everything).
**Complexity.** O(n) — one linear scan, three phases.
**Edge cases & gotchas.**
- The three phases are: (1) intervals entirely **before** `newInterval` (its
  end is `< newInterval.start`) — copy as-is; (2) intervals that **overlap**
  `newInterval` (its start is `<= mergedEnd`) — absorb into a single
  growing `[start, end]`; (3) everything **after** — copy as-is.
- Phase boundaries use **strict `<`** for "before" but **`<=`** for
  "overlapping" — get these backwards and you'll either miss a merge or merge
  one interval too many. Trace through `[[1,3],[6,9]]` + `[2,5]` (expect
  `[[1,5],[6,9]]`) to sanity-check.
- This relies on the **precondition** that the input is sorted and
  non-overlapping — if it isn't, sort and run `merge` first (or just append
  `newInterval` and call `merge` on everything, accepting O(n log n)).

**Active recall.**
- Q: Why is `insert` O(n) while `merge` is O(n log n)? → A: `merge` must sort
  an arbitrary list first; `insert`'s input is *already* sorted, so a single
  linear pass suffices — sorting would be wasted work.

**Practice.**
- [Insert Interval](https://leetcode.com/problems/insert-interval/) (Medium)
- [My Calendar I](https://leetcode.com/problems/my-calendar-i/) (Medium, insert-with-rejection-if-overlap, repeated)

---

## Non-overlapping Intervals — `Intervals.java#eraseOverlapIntervals`

**What & when.** "Remove the fewest intervals so none of the rest overlap" —
equivalent to "find the maximum subset of mutually non-overlapping
intervals" (answer = `n - maxSubsetSize`). Classic interval-scheduling greedy.
**Complexity.** O(n log n) for the sort, O(n) for the scan.
**Edge cases & gotchas.**
- Sort by **end** time, not start — this is the opposite of `merge`/`insert`
  and is the most common mistake on this problem.
- Greedy correctness: always keep the interval that **ends earliest** among
  candidates — it leaves the most room for future intervals. Formally, an
  exchange argument shows any optimal solution can be transformed to include
  the earliest-ending interval without becoming worse.
- The overlap check when deciding to drop is `sorted[i].start < lastEnd`
  (strict `<`) — touching intervals (`start == lastEnd`) are kept, mirroring
  the "touching is not overlapping" convention; flip to `<=` if the problem
  says otherwise.

**Active recall.**
- Q: Why sort by end time here but by start time in `merge`? → A: `merge`
  needs to detect overlaps *in arrival order* to build contiguous merged
  ranges; this problem is a **scheduling** greedy where keeping the
  earliest-finishing option maximizes remaining capacity — a fundamentally
  different objective that requires a different sort key.
- Q: How does this relate to "maximum number of non-overlapping intervals"? →
  A: They're complements: `removed = n - kept`, where `kept` is exactly the
  greedy count of intervals selected by this algorithm.

**Practice.**
- [Non-overlapping Intervals](https://leetcode.com/problems/non-overlapping-intervals/) (Medium)
- [Minimum Number of Arrows to Burst Balloons](https://leetcode.com/problems/minimum-number-of-arrows-to-burst-balloons/) (Medium, same greedy with `<=` overlap)
- [Maximum Length of Pair Chain](https://leetcode.com/problems/maximum-length-of-pair-chain/) (Medium, same greedy, asks for `kept` directly)

---

## Counting overlaps / resources — see `../heaps/`

"How many rooms/resources are needed if all these intervals run concurrently?"
is **not** in this folder — it's a min-heap-of-end-times problem, already
covered in `../heaps/NOTES.md` and `../heaps/MeetingRoomsII.java`. The
sort-by-start step is shared with `merge`, but the data structure (heap, not a
single running interval) is different.

---

## TODO — future additions

- **Interval intersection** (LC 986, two sorted lists of intervals →
  intersections) — a two-pointer variant not yet covered here.
- **2D version**: rectangles / calendar double/triple booking (My Calendar
  II/III) — extends the "count overlaps" idea to counting *how many* overlap,
  not just *whether*.

## Cross-cutting reminders

- **Sort key is the decision**: start-sort for merging/building, end-sort for
  greedy scheduling. State out loud *why* before coding.
- **`<` vs `<=` on overlap checks** is the #1 source of off-by-one bugs here —
  always state the convention ("touching counts as overlapping?") before
  writing the condition.
- Resource-counting ("min rooms") lives in `../heaps/`, not here — don't
  reach for a heap when a single running interval suffices.
