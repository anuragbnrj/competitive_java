# Segment Tree — revision notes

Pre-OA cheat sheet for `library/segmenttree/`. The template is a self-contained,
runnable class (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/segmenttree/SegmentTreeMax.java && java library.segmenttree.SegmentTreeMax
```

## Decision guide — which range structure?

| Problem signal | Use | File |
| --- | --- | --- |
| Range sum, **no updates** | Prefix sums | `../prefixsum/` |
| Range max/min/sum, **with point updates** | Segment tree | `SegmentTreeMax.java` |
| Range **sum** with point updates, simplest to code | Fenwick / BIT | TODO |
| **Range update** (assign/add to a whole range) + range query | Segment tree + lazy propagation | TODO |
| Static array, **no updates**, repeated range min/max | Sparse table (O(1) query, O(n log n) build) | TODO |

**The unifying idea:** a segment tree stores, at each node, the answer for the
range that node covers, built from its two children's answers — query/update
walk O(log n) nodes instead of O(n) elements.

---

## Point-update, range-query (max) — `SegmentTreeMax.java`

**What & when.** Range max over `[l, r]` (inclusive), with O(log n) point
updates. Swap `Math.max` for `+`/`Math.min`/gcd/etc. to get sum/min/gcd segment
trees — same shape.
**Complexity.** O(n) build, O(log n) query and update, O(4n) space.
**Edge cases & gotchas.**
- **`4 * n` sizing**: the array represents a binary tree with root at index `1`
  (children `2*idx`, `2*idx+1`); for non-power-of-2 `n` this needs up to
  `4*n` slots to be safe (`2 * nextPowerOfTwo(n)` is the tight bound, but `4*n`
  is the standard "don't think about it" size). Index `0` is unused.
- **Identity element per operation** — the "no overlap" branch of `query`
  returns the identity for the combine op: `Integer.MIN_VALUE` for **max**,
  `0` for **sum**, `Integer.MAX_VALUE` for **min**. Swapping `Math.max` for
  `Math.min`/`+` **without** updating this return value silently corrupts every
  query that hits a partial-overlap node.
- **Recursion order**: `build`/`update`/`query` all recurse into children
  *before* combining at the parent — the same bottom-up dependency as
  Floyd-Warshall's "k outermost", just expressed via recursion instead of a
  loop. No recursion-depth risk here (depth is O(log n)), unlike DFS-based
  graph algorithms.
- `query(l, r)` here is **inclusive** on both ends — confirm the problem's range
  semantics (some use half-open `[l, r)`).
- A **range update** (e.g. "add `x` to every element in `[l, r]`") with this
  point-update tree degrades to O((r-l)·log n) — that's what lazy propagation
  fixes (see TODO).

**Active recall.**
- Q: Why size the backing array `4 * n`? → A: 1-indexed binary tree
  (`2*idx`/`2*idx+1`); for arbitrary `n` the tree's last level can need up to
  `~2 * nextPow2(n)` nodes, and `4n` is a simple safe upper bound.
- Q: Converting this to a **sum** segment tree — what two things change? → A:
  the combine op (`Math.max` → `+`) **and** the "no overlap" identity
  (`Integer.MIN_VALUE` → `0`).
- Q: `update(pos, val)` touches one leaf — why is it O(log n), not O(1)? → A: It
  must recompute every ancestor of that leaf (there are O(log n) of them) so
  their stored ranges stay correct.

**Practice.**
- [Range Sum Query - Mutable](https://leetcode.com/problems/range-sum-query-mutable/) (Medium, sum variant)
- [Range Sum Query 2D - Mutable](https://leetcode.com/problems/range-sum-query-2d-mutable/) (Hard)
- Deeper (CSES):
  - [Static Range Minimum Queries](https://cses.fi/problemset/task/1647)
  - [Dynamic Range Minimum Queries](https://cses.fi/problemset/task/1649)
  - [Dynamic Range Sum Queries](https://cses.fi/problemset/task/1648)

---

## TODO — future additions

- **Fenwick tree / BIT** — simpler O(log n) point-update range-**sum**
  alternative when you only ever need sums (less code, no recursion).
- **Lazy propagation** — defer range updates to a `lazy[]` array, applied only
  when a subtree is actually visited; turns O(range·log n) updates into
  O(log n).
- **Sparse table** — O(n log n) build, O(1) query for **static** (no-update)
  range min/max/gcd; beats a segment tree when there are zero updates.

## Cross-cutting reminders

- **Pick the lightest structure that fits**: no updates → prefix sums; sum +
  point updates → Fenwick; max/min/anything + point updates → segment tree;
  range updates too → segment tree + lazy propagation.
- **Identity element travels with the combine op** — change both together or
  queries silently return wrong values for partial-overlap ranges.
- **Inclusive vs half-open ranges** — this template is inclusive `[l, r]`;
  double-check against the problem statement before reusing.
