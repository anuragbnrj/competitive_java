# Sliding Window — revision notes

Pre-OA cheat sheet for `library/slidingwindow/`. Each template is a
self-contained, runnable class (`main` has a worked self-test with `// expect:`
outputs). Verify with:

```
javac library/slidingwindow/SlidingWindow.java && java library.slidingwindow.SlidingWindow
```

## Decision guide — which technique?

| Problem signal | Technique | File |
| --- | --- | --- |
| "max/min sum (or avg) of a **fixed-size** window k" | fixed window, add-one/drop-one | `SlidingWindow.java` |
| "longest / count of subarrays with **at most K** distinct (or similar constraint)" | variable window, shrink while invalid | `SlidingWindow.java` |
| "**exactly K** distinct" | `atMost(K) - atMost(K-1)` | `SlidingWindow.java` (see gotchas), `../twopointers/TwoPointer.java` |
| "smallest window whose sum/condition reaches a target" | variable window, shrink while *valid* | `SlidingWindow.java` |
| Sorted pair search / in-place partition / linked-list cycle | two pointers | `../twopointers/` |

**The unifying idea:** a window `[left, right]` (or a pair of indices) moves
**monotonically** — never re-scan from scratch. Total work is O(n) because each
pointer only ever moves forward.

---

## Fixed-size window — `SlidingWindow.java#maxSumFixedWindow`

**What & when.** Best/worst sum (or average, count, etc.) over every contiguous
window of a fixed size `k`.
**Complexity.** O(n) time, O(1) extra space.
**Edge cases & gotchas.**
- Add the entering element **and** subtract the leaving element each step —
  don't recompute the sum from scratch (that's the O(n·k) trap).
- `k > a.length` → no valid window; guard before the loop.
- For max/min of a sum, only consider `best` once the first full window of size
  `k` has formed (`i >= k - 1`).

**Active recall.**
- Q: Why is recomputing each window's sum O(n·k) instead of O(n)? → A: Each
  window re-sums all `k` elements; add-one/drop-one reuses the previous sum in
  O(1) per step.

**Practice.**
- [Maximum Average Subarray I](https://leetcode.com/problems/maximum-average-subarray-i/) (Easy)
- [Maximum Number of Vowels in a Substring of Given Length](https://leetcode.com/problems/maximum-number-of-vowels-in-a-substring-of-given-length/) (Medium)

---

## Variable window — `SlidingWindow.java#countAtMostKDistinct` / `#longestAtMostKDistinct`

**What & when.** "Longest / count of subarrays satisfying a constraint that can
only get **worse** as the window grows" (distinct count, character frequency,
etc.). Expand `right`; while the constraint is violated, shrink from `left`.
**Complexity.** O(n) amortized — `left` only moves forward, so total shrink
steps are bounded by n.
**Edge cases & gotchas.**
- The **count** variant (`ans += right - left + 1` per `right`) relies on
  monotonicity: if `[left, right]` satisfies the constraint, so does every
  `[l, right]` for `l > left`. True for "at most K distinct" (shrinking can't
  *add* distinct values) — verify this holds for *your* constraint before
  reusing the formula.
- Use a frequency `HashMap` and **remove the key when its count hits 0**, so
  `map.size()` reflects the true distinct count, not stale zero-entries.
- **"Exactly K distinct"** (LC1248) is *not* "at most K" directly — compute
  `atMost(K) - atMost(K - 1)`. Edge case `K == 0`: `atMost(-1)` must be `0`.

**Active recall.**
- Q: Why does `ans += (right - left + 1)` correctly count all valid subarrays
  ending at `right`? → A: `left` is the *smallest* valid left for this `right`;
  every `l` in `[left, right]` also keeps the window valid (shrinking can't
  violate an "at most" constraint), so each gives a distinct valid subarray.
- Q: How do you turn "at most K" into "exactly K"? → A: `atMost(K) - atMost(K-1)`
  — subarrays with ≤K distinct minus those with ≤K-1 distinct leaves exactly K.

**Practice.**
- [Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) (Medium)
- [Longest Substring with At Most K Distinct Characters](https://leetcode.com/problems/longest-substring-with-at-most-k-distinct-characters/) (Medium)
- [Fruit Into Baskets](https://leetcode.com/problems/fruit-into-baskets/) (Medium, = at-most-2-distinct)
- [Subarrays with K Different Integers](https://leetcode.com/problems/subarrays-with-k-different-integers/) (Hard, exactly-K)
- Deeper (CSES):
  - [Distinct Values Subarrays](https://cses.fi/problemset/task/2428)

---

## Variable window, shrink-while-valid — `SlidingWindow.java#minWindowSumAtLeast`

**What & when.** "**Smallest** window that *satisfies* a condition" (sum ≥
target, contains all characters of a pattern). Mirror of the above: expand
`right` until the window becomes valid, then shrink `left` **while still
valid**, recording the best length each time.
**Complexity.** O(n) amortized.
**Edge cases & gotchas.**
- Opposite shrink condition from the "at most K" case: here you shrink **while
  the window is still valid**, not while it's invalid.
- Assumes **all-positive values** (sum only increases as the window grows) —
  with negatives this monotonicity breaks and sliding window no longer applies.
- No valid window found → return a sentinel (`0` here); the caller must check
  for it.

**Active recall.**
- Q: Why shrink *while valid* here but *while invalid* for "at most K"? → A:
  Different goals — "at most K" wants the **largest** valid window per `right`
  (shrink only on violation); "smallest window" wants the **smallest** valid
  window, so every shrink that preserves validity is a new candidate answer.

**Practice.**
- [Minimum Size Subarray Sum](https://leetcode.com/problems/minimum-size-subarray-sum/) (Medium)
- [Minimum Window Substring](https://leetcode.com/problems/minimum-window-substring/) (Hard)
- Deeper (CSES):
  - [Subarray Sums II](https://cses.fi/problemset/task/1661) (prefix sums + map, *not* sliding window — note the difference)


---

## TODO — future additions

- `../twopointers/TwoPointer.java` solves the same "at most/exactly K distinct"
  problem with a full CP I/O harness, iterating from the left pointer instead of
  the right — a second worked example of the same idea.

## Cross-cutting reminders

- **Monotonic pointers only** — if a pointer would ever need to move backward,
  this isn't a sliding-window problem (or the window definition is wrong).
- **Frequency maps**: remove the key on count-0 so `.size()` is the true
  distinct count.
- **All-positive assumption**: shrink-while-valid (minimum-window) techniques
  need monotonic running totals — breaks with negative numbers.
- For **two-pointer** patterns (sorted pair search, in-place partition, fast/slow),
  see `../twopointers/NOTES.md`.
