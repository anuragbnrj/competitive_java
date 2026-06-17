# Monotonic Stack & Deque — revision notes

Pre-OA cheat sheet for `library/monotonic/`. Each template is a self-contained,
runnable class (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/monotonic/MonotonicStack.java && java library.monotonic.MonotonicStack
javac library/monotonic/MonotonicDeque.java && java library.monotonic.MonotonicDeque
```

## Decision guide — which technique?

| Problem signal | Technique | File |
| --- | --- | --- |
| "next/previous **greater**" for every element | monotonic stack (decreasing) | `MonotonicStack.java#nextGreaterIndex` |
| "next/previous **smaller**" for every element | monotonic stack (increasing) | `MonotonicStack.java#prevSmallerIndex` |
| "for each bar, how far can it extend" (histogram-style) | both directions via the stack above | `MonotonicStack.java` |
| "sliding window **maximum/minimum**" | monotonic deque | `MonotonicDeque.java` |
| "k-th largest" / "top K" (no ordering constraint) | heap, not a monotonic structure | `../heaps/` |

**The unifying idea:** both structures keep elements in **sorted order** by
discarding ones that can never be the answer again — a stack discards from one
end as you scan forward (amortized O(n) total pops), a deque discards from
*both* ends as the window slides.

---

## Building intuition — is this a monotonic-stack problem?

Knowing *how* `nextGreaterIndex`/`prevSmallerIndex` work is the easy part — the
hard part is recognizing, from a fresh problem statement, that one of them
applies at all. Here's the recognition checklist.

**The litmus test.** Ask: as I scan left → right, does an element become
**permanently useless the moment a later element dominates it** (is bigger,
smaller, taller, etc.), such that all I'll ever need from the past is the
*nearest survivor*? If yes, a monotonic stack applies — the stack *is* the set
of not-yet-dominated candidates, and **the act of popping an element is usually
where its answer gets produced** (its "next greater/smaller" is whatever just
dominated it).

**Signal phrases and what they reduce to.**
- "next/previous greater/smaller element" → literally `nextGreaterIndex` /
  `prevSmallerIndex`.
- "how many days until a warmer day" / "span since the last higher price" →
  next-greater, read as a *distance* instead of an index (Daily Temperatures,
  Online Stock Span).
- "for each bar/element, the range over which it is the max (or min)" →
  combine a previous- and a next- query (smaller or greater, depending on the
  problem) to get a width — Largest Rectangle in Histogram, Sum of Subarray
  Minimums.
- "largest rectangle / area under a skyline" → the same both-sides width idea,
  applied per bar as that bar's height.
- "water trapped between bars" → for each bar, the bound on each side is the
  nearest *taller* bar — a next/previous-greater problem in disguise.
- "remove k digits/characters to make the result smallest (or largest)
  possible" → a *greedy* monotonic stack: pop while the top is worse than the
  incoming character and you still have removals to spend (Remove K Digits,
  Remove Duplicate Letters).
- "sum/count something over all subarrays, weighted by each subarray's min or
  max" → contribution counting (see below) — almost always a monotonic stack,
  even though the word "stack" never appears in the statement.

**The mental model: dominance / obsolescence.** Picture the stack as your
"still relevant" set, kept in monotonic order. When a new element arrives that
dominates the top of the stack, every dominated element can **never again** be
the answer to anything further right — so you pop it, and the new element (or
its index) becomes that popped element's answer. The **contrapositive** is the
disqualifier: **if every element seen so far could still matter no matter what
arrives later, this is not a monotonic-stack problem** — nothing ever becomes
obsolete, so there's nothing for the stack to discard.

**The big leap — from "find an index" to "count contributions."** The
easy/medium tier asks "what is the next greater element's *index*?" The
medium/hard tier (Sum of Subarray Minimums, 132 Pattern, "sum of max − min over
all subarrays") instead asks you to count *how many subarrays* an element is
the min (or max) of, and weight its value by that count. The count is just
`leftSpan * rightSpan`, where each span comes from a previous/next
greater-or-smaller computation you already know how to do. Mini-example: in
`[3, 1, 2, 4]`, the `1` at index 1 is the minimum of **6** subarrays
(`leftSpan = 2`, since nothing to its left is smaller; `rightSpan = 3`, since
nothing to its right is smaller-or-equal) — it contributes `1 * 6 = 6` toward
the total sum of subarray minimums, which is `17`. Once you see "weighted by
min/max over all subarrays," reach for spans-as-counts, not indices.

**When it's NOT a monotonic stack.**
- The question is about a **global** k-th-largest / top-k, independent of
  position → heap, see `../heaps/`.
- There's a **sliding window** (both ends move) and you want its max/min →
  monotonic **deque**, see the next section and `MonotonicDeque.java`.
- You need order statistics with **arbitrary** deletion (not just from one
  end) → TreeMap multiset (see `../heaps/` TODO).
- The relationship is about **sums hitting a target**, not about
  greater/smaller neighbors → prefix sums (`../prefixsum/`) or two pointers
  (`../slidingwindow/`).

**Worked recognition walkthrough — Sum of Subarray Minimums.** The brute force
is "for every subarray, scan it to find its min" — O(n²) subarrays, each
costing up to O(n) to scan (or O(n²) total if you track a running min while
extending the right end). To do better, flip the question: instead of "what's
the min of this subarray," ask "for this *element*, in how many subarrays is
it *the* min?" `a[i]` is the min of subarray `[l, r]` iff `l` lies in the gap
between the previous smaller element and `i` (its `leftSpan`) and `r` lies in
the gap between `i` and the next smaller-or-equal element (its `rightSpan`) —
because if `[l, r]` reached past either boundary, that boundary element
(smaller than `a[i]`) would be the min instead. Both boundaries are exactly
`prevSmallerIndex`/`nextGreaterIndex`-style queries — hence monotonic stack,
with a *count* (not an index) as the payload.

**Active recall.**
- Q: What single property of a problem tells you a monotonic stack applies? →
  A: elements become *permanently* useless once a later element dominates
  them, and you only ever need the nearest surviving one — the contrapositive
  (every past element could still matter later) rules it out.

---

## Building intuition — is this a monotonic-deque problem?

The deque is the stack's "dominance" idea **plus a second reason to discard**:
expiry from a sliding window. If the previous section's litmus test feels close
but something doesn't quite fit, check this one before reaching for a heap.

**The litmus test.** Ask: do I want the best (max/min, or any "best-so-far")
value over a **window that slides** — a new element enters on one side *and*
an old element must leave from the other — where a candidate becomes useless
either (a) because a **newer element dominates it** (the stack's reason), or
(b) because **its index has fallen out of the window** (a new reason, with no
analog in a plain stack)? Two independent eviction reasons, one per end, is
what makes it a *deque* rather than a *stack*.

**Signal phrases and what they reduce to.**
- "maximum (or minimum) of every window of size k" → the canonical case,
  `MonotonicDeque.java#maxSlidingWindow` directly.
- "for each position, the best value within the last k elements" (a bounded
  look-back, not necessarily phrased as "windows") → the same template,
  applied over the array or over a derived array (e.g. prefix sums).
- "longest/shortest subarray such that max − min ≤ limit" → **two** monotonic
  deques moving together (one tracking the window max, one the window min),
  shrinking the window whenever the gap exceeds `limit`.
- a DP recurrence of the form `dp[i] = best(dp[i-k .. i-1]) + cost(i)` (the
  transition only looks back a bounded distance) → maintain the window of
  `dp` values in a monotonic deque to get `best(...)` in O(1) amortized
  instead of O(k) per step (Constrained Subsequence Sum, Jump Game VI).

**The mental model: stack + an expiry clock.** Keep the exact same picture as
above — a monotonic front-to-back list of not-yet-dominated candidates, front
= current best — but now each candidate also carries an **expiry index** (when
it slides out of the window). Two pops per step instead of one: pop from the
**back** when the incoming element dominates (obsolescence, same as the
stack), and pop from the **front** when its index has expired (new). The
contrast that pins this down: **if the look-back is unbounded — nothing ever
expires — the front-eviction rule never fires, and the structure collapses back
into a plain monotonic stack / previous-greater-style scan.** The deque only
earns its second end when there's an actual window.

**When it's NOT a monotonic deque.**
- The window is fixed but you need the **k-th largest or the median**, not
  just the max/min → heap, or two heaps for the median, see `../heaps/`.
- There's **no sliding window** — you want the nearest greater/smaller over the
  whole array → monotonic **stack**, `MonotonicStack.java`.
- You need the window's **sum** (not its max/min) → that's a running total /
  prefix sum, not a deque — see `../slidingwindow/` and `../prefixsum/`.
- The window's max needs **arbitrary** removals (not just from the expiring
  end) → TreeMap multiset (see `../heaps/` TODO).

**Worked recognition walkthrough — Shortest Subarray with Sum at Least K
(LC 862).** Nothing in the statement says "window," so the deque is hidden.
Build prefix sums `P[0..n]`. A subarray `(i, j]` has sum `P[j] - P[i] >= K`,
and you want to minimize `j - i`. For a fixed `j`, scanning all `i < j` is
O(n²) — but notice: if two earlier prefix indices `i1 < i2` have `P[i1] >=
P[i2]`, then `i1` is **permanently dominated** by `i2` — `i2` is both *closer*
to any future `j` (a shorter subarray) *and* at least as easy to satisfy the
sum condition (a smaller `P` makes `P[j] - P[i]` at least as large). So `i1`
can be discarded the moment `i2` appears — exactly the stack's obsolescence
rule, now applied to prefix-sum indices rather than raw array values. The
"window" appears implicitly too: once the front index `i` satisfies `P[j] -
P[i] >= K`, it can only get *worse* (a longer subarray) for any larger `j`, so
it's consumed and popped from the front as well. Two discard rules on
prefix-sum indices → monotonic deque, even though the original array had
nothing to do with sliding windows.

**Active recall.**
- Q: Same "discard dominated candidates" idea as the stack — what makes it a
  *deque*? → A: a second, independent eviction rule: candidates also expire
  when their index leaves the sliding window, so you must be able to pop from
  the front as well as the back.

---

## Monotonic stack — `MonotonicStack.java`

**What & when.** For every element, find the nearest element to its left/right
that is greater/smaller than it. Underpins "Daily Temperatures", "Largest
Rectangle in Histogram", "Trapping Rain Water" (stack variant), and any "how far
until a bigger/smaller element" problem.
**Complexity.** O(n) — each index is pushed once and popped at most once.
**Edge cases & gotchas.**
- **Strict vs non-strict comparison** changes behavior on duplicates: `<` keeps
  equal elements on the stack (so a later *equal* element does **not** count as
  "greater"); use `<=`/`>=` if equal elements should resolve each other.
- `nextGreaterIndex` scans **left → right** and pops while the stack's top is
  smaller (the popped element's answer is the current index). `prevSmallerIndex`
  scans the same direction but the stack top *is* the answer (no popping of the
  answer itself).
- For "next smaller" (as opposed to `prevSmallerIndex`), either scan
  **right → left** with the same logic as `nextGreaterIndex` (flip the
  comparator), or run `nextGreaterIndex` on the negated array.
- **Largest Rectangle in Histogram**: for each bar, the rectangle's width is
  `nextSmaller[i] - prevSmaller[i] - 1` — both directions are needed.
- The stack stores **indices**, not values — you need the index to compute
  distances/widths, and `a[stack.peek()]` to compare values.

**Active recall.**
- Q: Why is the total work O(n) even though there's a nested `while` loop? → A:
  Each index is pushed exactly once and popped at most once — the *total*
  number of pops across the whole run is bounded by n (amortized analysis).
- Q: `nextGreaterIndex` uses `<` — what changes with `<=`? → A: With `<=`, an
  equal value also triggers a pop, so the "next greater" of a run of equal
  values becomes the first element strictly after that run that is `>=`
  (changes how ties resolve — match the problem's definition of "greater").

**Practice.**
- [Daily Temperatures](https://leetcode.com/problems/daily-temperatures/) (Medium, `nextGreaterIndex`)
- [Next Greater Element I](https://leetcode.com/problems/next-greater-element-i/) (Easy)
- [Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/) (Hard, both directions)
- [Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) (Hard, stack or two-pointer)
- [Online Stock Span](https://leetcode.com/problems/online-stock-span/) (Medium)

---

## Monotonic deque — `MonotonicDeque.java`

**What & when.** The maximum (or minimum) of every fixed-size sliding window, in
O(n) total — the O(n log k) heap approach is correct but slower for this
specific shape.
**Complexity.** O(n) — each index enters and leaves the deque at most once.
**Edge cases & gotchas.**
- Front of the deque is always the **current window's max** (values are
  strictly decreasing front-to-back).
- Two independent eviction checks per step, **order matters**: (1) pop from the
  **back** while it's `<= a[i]` (it can never be the max again); (2) pop from
  the **front** if its index has slid out of the window (`<= i - k`). Do (1)
  before pushing `i`, and (2) after.
- Store **indices**, not values — needed to detect when the front has expired
  from the window.
- For **minimum** instead of maximum, flip the back-eviction comparator
  (`>=` instead of `<=`).

**Active recall.**
- Q: Why is this O(n) total and not O(n·k)? → A: Each index is pushed once and
  popped at most once (from either end) — total deque operations are bounded by
  2n, independent of `k`.
- Q: Why must the front-eviction check come *after* pushing the new index? → A:
  The new index could itself be the one that should now occupy the front if the
  old front just expired — order doesn't actually break correctness either way
  here since they're independent checks, but checking window-bounds against the
  *current* `i` requires `i` to be the index just considered.

**Practice.**
- [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) (Hard)
- [Shortest Subarray with Sum at Least K](https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/) (Hard, monotonic deque on prefix sums)
- [Constrained Subsequence Sum](https://leetcode.com/problems/constrained-subsequence-sum/) (Hard, monotonic deque + DP)
- Deeper (CSES):
  - [Sliding Window Minimum](https://cses.fi/problemset/task/3221) *(if available — otherwise: Sliding Window Cost / Sliding Window Median for the broader family)*

---

## TODO — future additions

- **Largest Rectangle in Histogram** as a worked combined example (uses both
  `nextGreaterIndex`-style and `prevSmallerIndex` in one pass with a single
  stack) — currently only described in gotchas above.
- **Monotonic stack for 132 pattern / sum of subarray minimums** — variants that
  use the stack to count contributions rather than just find indices.

## Cross-cutting reminders

- **Stack = one sliding boundary** (process left-to-right, discard from the
  top); **deque = two sliding boundaries** (a window with both ends moving).
- **Store indices, not values** — both techniques need positions to compute
  distances or detect window expiry.
- **Strict vs non-strict comparators** change tie-breaking on duplicates —
  pick deliberately and match the problem statement.
- Cross-reference: sliding-window **maximum** is *not* a heap problem — see
  `../heaps/NOTES.md`.
