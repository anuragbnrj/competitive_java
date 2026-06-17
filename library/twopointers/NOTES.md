# Two Pointers — revision notes

Pre-OA cheat sheet for `library/twopointers/`. Templates are self-contained,
runnable classes (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/twopointers/TwoPointers.java && java library.twopointers.TwoPointers
```

(`TwoPointer.java` reads stdin — compile-only: `javac library/twopointers/TwoPointer.java`)

## Decision guide — which pattern?

| Problem signal | Technique | File |
| --- | --- | --- |
| Sorted array, find a pair summing to target | opposite ends | `TwoPointers.java#twoSumSorted` |
| Partition / compact an array in place, preserve order | same direction (read+write) | `TwoPointers.java#moveZeroesToEnd` |
| Linked-list cycle / find middle / nth-from-end | fast & slow | TODO (see below) |
| "at most/exactly K distinct" with full CP I/O harness | same-direction worked example | `TwoPointer.java` |

**The unifying idea:** two indices move **monotonically** — no pointer ever moves
backward. Total work is O(n) because each pointer traverses the structure at most
once.

---

## Opposite ends — `TwoPointers.java#twoSumSorted`

**What & when.** Sorted array; find a pair (or check feasibility) by moving
pointers inward from both ends.
**Complexity.** O(n).
**Edge cases & gotchas.**
- Requires **sorted** input — if the array isn't sorted, sort first
  (O(n log n)) or use a `HashMap` (O(n), unsorted Two Sum).
- `sum == target` → return; `sum < target` → `lo++` (need a larger value);
  `sum > target` → `hi--`.
- Loop while `lo < hi` — a single element can't pair with itself.

**Active recall.**
- Q: Plain Two Sum uses a `HashMap` in O(n) — why use two pointers here instead?
  → A: Only worthwhile when the array is **already sorted** (or sorting is free /
  required anyway) — two pointers avoid the O(n) extra space of the map.

**Practice.**
- [Two Sum II - Input Array Is Sorted](https://leetcode.com/problems/two-sum-ii-input-array-is-sorted/) (Medium)
- [3Sum](https://leetcode.com/problems/3sum/) (Medium, fix one element + two-pointer the rest)
- [Container With Most Water](https://leetcode.com/problems/container-with-most-water/) (Medium)
- [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) (Hard)

---

## Same direction (read/write, partition) — `TwoPointers.java#moveZeroesToEnd`

**What & when.** Compact / partition an array in place: a "write" pointer trails
a "read" pointer, advancing only when the read element should be kept.
**Complexity.** O(n), O(1) extra space.
**Edge cases & gotchas.**
- This is the **fast/slow** pattern for arrays — `read` is the fast pointer
  (visits everything), `write` is the slow pointer (only the kept elements);
  invariant `write <= read` always holds.
- **Swap, don't overwrite** — overwriting loses the displaced element, which
  matters when it must end up elsewhere (e.g. zeroes moved to the end, not
  dropped).
- "Remove duplicates from sorted array" is the same shape: `write` advances only
  when `a[read] != a[write - 1]`.

**Active recall.**
- Q: How is this different from the opposite-ends pattern? → A: Both pointers
  move in the **same direction** at different "speeds" (`write <= read` always),
  vs. closing in from both ends of the array.

**Practice.**
- [Move Zeroes](https://leetcode.com/problems/move-zeroes/) (Easy)
- [Remove Duplicates from Sorted Array](https://leetcode.com/problems/remove-duplicates-from-sorted-array/) (Easy)
- [Sort Colors](https://leetcode.com/problems/sort-colors/) (Medium, Dutch national flag — three pointers)

---

## CP harness & worked examples

**`TwoPointer.java`** solves "Number of Subarrays with At Most K Distinct" with a
full stdin/stdout CP I/O harness. It iterates by advancing the left (`tail`)
pointer — a second worked example of the same sliding-left-pointer idea in
`../slidingwindow/SlidingWindow.java`. Note: no `// expect:` self-test (reads stdin).

**`TemplateAlgozenith.java`** provides `getLessOrEqual(k, arr, freq)` — the same
at-most-K-distinct idea as a plain reusable method with the same left-pointer
advancement. Key difference: it accumulates `currLen * (currLen + 1) / 2` per step,
which counts *all* subarrays within the current window (not just those ending at
`right`). This is the right accumulation when the problem asks for a total count
weighted by window length, not a per-right-endpoint count. Verify which shape your
problem has before reusing. No `main` / self-test.

---

## TODO — future additions

- **Fast & slow pointers on linked lists** (Floyd's cycle detection, find the
  middle, nth-from-end) — a different data structure than the array templates
  here; worth its own file when `library/linkedlist/` exists.

## Cross-cutting reminders

- **Monotonic pointers only** — if a pointer would ever need to move backward,
  this isn't a two-pointer problem (or the problem definition is wrong).
- **Sorted input** is a prerequisite for opposite-ends; unsorted input needs a
  `HashMap` instead.
- **Swap, don't overwrite** in partition patterns — the displaced element usually
  needs to land somewhere (not be dropped).
- For **sliding-window** problems (variable constraint over a contiguous window),
  see `../slidingwindow/NOTES.md`.
