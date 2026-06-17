# Binary Search — revision notes

Pre-OA cheat sheet for `library/binarysearch/`. Each template is a self-contained,
runnable class (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/binarysearch/BinarySearchOnAnswer.java && java library.binarysearch.BinarySearchOnAnswer
```

This folder is the hand-rolled **algorithm**. The JDK exact-match **API**
(`Arrays.binarySearch` / `Collections.binarySearch`, insertion-point semantics)
lives in `../collectionssyntax/BinarySearch.java`.

## Decision guide — which template?

| Problem signal | Technique | File |
| --- | --- | --- |
| Exact value in a sorted array | `search` (or `Arrays.binarySearch`) | `BinarySearchBasics.java` |
| First/last occurrence, or count, of a value | lower/upper bound | `BinarySearchBasics.java` |
| "smallest index where condition holds" | `firstTrue` (predicate) | `BinarySearchBasics.java` |
| "minimize / maximize a value s.t. a check passes" | binary search on the answer | `BinarySearchOnAnswer.java` |
| Search / find-min in a rotated sorted array | rotated template | `RotatedSortedArray.java` |
| Monotonic **real-valued** function (e.g. sqrt) | fixed-iteration real BS | `BinarySearchReal.java` |
| Matrix sorted **row-major as one sequence** | flatten + `search` | `BinarySearchBasics.java` (index map, see below) |
| Local maximum in an **unsorted** array | BS on the slope | see *Peak finding* below |
| Median of two sorted arrays | BS on a partition index | see *Median of Two Sorted Arrays* below |

**The unifying idea:** binary search needs a **monotonic predicate** —
`false…false, true…true` — and finds the boundary. "On the answer" just means the
search space is the *answer value*, not an array index.

## Template anatomy

- **Half-open `[lo, hi)`** (lower/upper bound, firstTrue): `hi = n`,
  `while (lo < hi)`, move `lo = mid + 1` (fail) or `hi = mid` (pass); answer is `lo`.
  Clean because `hi = n` is a valid "past the end / not found" result.
- **Closed `[lo, hi]`** (exact `search`): `hi = n - 1`, `while (lo <= hi)`,
  `lo = mid + 1` / `hi = mid - 1`.
- **largestFeasible** (last true): bias mid UP with `lo + (hi - lo + 1) / 2`,
  then `lo = mid` / `hi = mid - 1` — otherwise `lo = mid` never advances and loops.

## Gotcha catalog

- **mid overflow** — use `lo + (hi - lo) / 2`, never `(lo + hi) / 2`.
- **infinite loop** — if a branch sets `lo = mid` (not `mid + 1`), you MUST round
  mid up (`+1` bias); otherwise it spins. The half-open form avoids this by always
  using `lo = mid + 1` / `hi = mid`.
- **off-by-one** — exact search uses `lo <= hi` + `±1`; bounds use `lo < hi` +
  `hi = mid`. Don't mix the two styles.
- **predicate must be monotonic** — if `feasible` isn't false…false,true…true the
  result is meaningless. Sanity-check the boundary by hand.
- **overflow in the answer range / sums** — use `long` for `lo/hi` and any
  accumulation inside `feasible` (e.g. total hours, total weight).
- **input must be sorted** for the array templates (rotated is the deliberate
  exception).
- **real BS**: use a fixed iteration count (~100), not `while (hi - lo > eps)`.

## Active recall

- Q: How do you get first occurrence, last occurrence, and count of `x` from the
  bounds? → A: first = `lowerBound(x)`; last = `upperBound(x) - 1`; count =
  `upperBound(x) - lowerBound(x)`.
- Q: Your "last true" search hangs — why? → A: `lo = mid` with
  `mid = (lo+hi)/2` never advances when `hi = lo + 1`; bias mid up with `+1`.
- Q: What single property must hold to binary-search on the answer? → A: the
  feasibility predicate is monotonic in the answer value.
- Q: Why fixed iterations for real-valued BS? → A: floating-point rounding can make
  `hi - lo > eps` never become false; ~100 halvings shrink below any practical eps.

## Interviewer follow-ups

- "Why is this correct?" on a BS-on-answer problem → state the monotonicity of
  `feasible` out loud (false…false, true…true in the *answer* value) before
  coding; interviewers probe this far more than the loop mechanics.
- "Can you do it without binary search?" → most BS-on-answer problems also admit
  a two-pointer or greedy O(n) scan once the monotonicity is clear; naming the
  alternative shows you understand *why* BS works, not just *that* it works.

## Practice (LeetCode, progressive: Easy → Medium → Hard)

- **Basics / bounds**
  - [Binary Search](https://leetcode.com/problems/binary-search/) (Easy)
  - [Search Insert Position](https://leetcode.com/problems/search-insert-position/) (Easy)
  - [First Bad Version](https://leetcode.com/problems/first-bad-version/) (Easy, predicate)
  - [Find First and Last Position of Element in Sorted Array](https://leetcode.com/problems/find-first-and-last-position-of-element-in-sorted-array/) (Medium)
- **On the answer**
  - [Koko Eating Bananas](https://leetcode.com/problems/koko-eating-bananas/) (Medium)
  - [Capacity To Ship Packages Within D Days](https://leetcode.com/problems/capacity-to-ship-packages-within-d-days/) (Medium)
  - [Minimum Number of Days to Make m Bouquets](https://leetcode.com/problems/minimum-number-of-days-to-make-m-bouquets/) (Medium)
  - [Split Array Largest Sum](https://leetcode.com/problems/split-array-largest-sum/) (Hard)
- **Rotated**
  - [Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/) (Medium)
  - [Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/) (Medium)
  - [Search in Rotated Sorted Array II](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/) (Medium, duplicates)
- **Real-valued**
  - [Sqrt(x)](https://leetcode.com/problems/sqrtx/) (Easy)
  - [Find the Smallest Divisor Given a Threshold](https://leetcode.com/problems/find-the-smallest-divisor-given-a-threshold/) (Medium)
- **Deeper (CSES):**
  - [Factory Machines](https://cses.fi/problemset/task/1620)
  - [Array Division](https://cses.fi/problemset/task/1085)

---

## Search a 2D sorted matrix

**What & when.** A matrix where each row is sorted ascending and the first
element of each row is greater than the last element of the previous row (LC74)
— the whole matrix is **one sorted sequence** in row-major order.
**Pattern.** Treat it as a 1D array of size `m*n`: binary search with
`mid -> (mid / cols, mid % cols)` to read `matrix[r][c]`. Same `search` template
as `BinarySearchBasics.java`, just with an index mapping.
**Edge cases & gotchas.**
- LC240 ("Search a 2D Matrix II": rows AND columns sorted independently, but rows
  don't chain) is **not** this pattern — that's a staircase two-pointer walk from
  a corner, O(m+n), not binary search.
- Don't transpose the mapping: `mid / cols` is the row, `mid % cols` is the
  column.

**Active recall.**
- Q: LC74 vs LC240 — same algorithm? → A: No. LC74's matrix is one sorted
  sequence (binary search works); LC240 only has sorted rows/cols independently
  — binary search per row is O(m log n) at best, but the intended O(m+n)
  staircase walk beats it.

**Practice.**
- [Search a 2D Matrix](https://leetcode.com/problems/search-a-2d-matrix/) (Medium)
- [Search a 2D Matrix II](https://leetcode.com/problems/search-a-2d-matrix-ii/) (Medium, staircase — not BS)

---

## Peak finding

**What & when.** Find any local maximum (`nums[i] > nums[i-1]` and
`nums[i] > nums[i+1]`) in an array with **no sorted-order requirement**.
**Pattern.** Binary search on the *slope*: predicate `nums[mid] > nums[mid+1]`.
The false…false,true…true shape still holds — if the slope is rising at `mid`
(`nums[mid] < nums[mid+1]`), a peak exists to the right; if falling, a peak
exists at or before `mid`. Same half-open `firstTrue` shape as
`BinarySearchBasics.java`, just a different predicate.
**Edge cases & gotchas.**
- Treat out-of-bounds neighbors as `-∞` — the array edges are valid peaks.
- Multiple peaks may exist; any one is acceptable unless the problem says
  otherwise.

**Active recall.**
- Q: The array isn't sorted — why does binary search still work? → A: The
  predicate isn't about *values* being sorted, it's about the *slope sign*
  being monotonic enough to guarantee a peak lies in the "true" half.

**Practice.**
- [Find Peak Element](https://leetcode.com/problems/find-peak-element/) (Medium)
- [Peak Index in a Mountain Array](https://leetcode.com/problems/peak-index-in-a-mountain-array/) (Easy)

---

## Median of Two Sorted Arrays

**What & when.** Median of two sorted arrays in `O(log(min(m,n)))` — the
canonical Hard binary-search problem.
**Pattern.** Binary search **on the answer** = the partition index `i` into the
shorter array (`0..m`); the partner partition is `j = (m+n+1)/2 - i` into the
other array. `feasible(i)` iff the cross-partition values don't cross
(`left1 <= right2 && left2 <= right1`) — same `feasible`-predicate shape as
`BinarySearchOnAnswer.java`.
**Edge cases & gotchas.**
- Always binary search the **shorter** array — keeps `j` in `[0, n]` and the
  complexity at `log(min(m,n))`.
- Guard partition edges with `±∞` (`Integer.MIN_VALUE`/`MAX_VALUE`) when `i` or
  `j` is `0` or at the array length.
- Odd total length → median is `max(left1, left2)`; even → average of
  `max(left1, left2)` and `min(right1, right2)`.

**Active recall.**
- Q: Why binary search the shorter array? → A: `j = (m+n+1)/2 - i` must stay in
  `[0, n]`; searching the shorter array guarantees `i` stays small enough that
  `j` never goes negative or exceeds `n`.

**Practice.**
- [Median of Two Sorted Arrays](https://leetcode.com/problems/median-of-two-sorted-arrays/) (Hard)

---

## TODO — future additions

- "Aggressive cows" / Magnetic Force — maximize the minimum spacing (same BS-on-
  the-answer pattern as `BinarySearchOnAnswer.java`, "largest feasible" variant).

## Cross-cutting reminders

- Prefer the **half-open `[lo, hi)`** template as your default — it sidesteps the
  `lo = mid` infinite-loop trap and most off-by-ones.
- Reduce a new problem to a **monotonic predicate**, then it's just `firstTrue` /
  `smallestFeasible`.
- Exact-match on sorted data with the JDK? Use `Arrays.binarySearch`
  (`../collectionssyntax/BinarySearch.java`) — but remember its `-(ip)-1` miss code.
