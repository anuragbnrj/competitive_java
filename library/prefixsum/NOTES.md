# Prefix Sums & Difference Arrays — revision notes

Pre-OA cheat sheet for `library/prefixsum/`. The template is a self-contained,
runnable class (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/prefixsum/PrefixSum.java && java library.prefixsum.PrefixSum
```

## Decision guide — which technique?

| Problem signal | Use | File |
| --- | --- | --- |
| Many range-sum queries, **array doesn't change** | 1D prefix sums | `PrefixSum.java#buildPrefix/rangeSum` |
| Many rectangle-sum queries on a **static grid** | 2D prefix sums | `PrefixSum.java#buildPrefix2D/rangeSum2D` |
| Many "add `v` to every index in `[l, r]`", read final array **once at the end** | difference array | `PrefixSum.java#applyRangeUpdates` |
| Range sum **with interleaved updates** | segment tree / Fenwick | `../segmenttree/` |
| "Subarray sum equals K" (count, not just one range) | prefix sums + `HashMap` of seen sums | see Practice below |

**The unifying idea:** prefix sums turn "sum of a range" into "subtract two
precomputed totals" (O(1) per query after O(n) build). A difference array is the
*inverse* trick: turn "add to a whole range" into two O(1) point updates,
deferred to a single prefix-sum pass at the end.

---

## 1D prefix sums — `PrefixSum.java#buildPrefix` / `#rangeSum`

**What & when.** O(1) range-sum queries on a **static** array after an O(n)
precompute.
**Complexity.** O(n) build, O(1) per query.
**Edge cases & gotchas.**
- `prefix` has length `n + 1` with `prefix[0] = 0` — this sentinel avoids a
  special case for `l == 0`: `rangeSum(l, r) = prefix[r+1] - prefix[l]` works
  uniformly.
- This template's `rangeSum(l, r)` is **inclusive** on both ends — confirm
  against the problem's convention (some define `prefixSum(i)` = sum up to but
  not including `i`).
- Updates to the underlying array invalidate the prefix array from that index
  onward — if updates happen, use a Fenwick/segment tree instead
  (`../segmenttree/`).

**Active recall.**
- Q: Why does `prefix` have size `n+1`, not `n`? → A: `prefix[0] = 0` lets
  `rangeSum(0, r) = prefix[r+1] - prefix[0] = prefix[r+1]` without a special
  case for the start of the array.

**Practice.**
- [Range Sum Query - Immutable](https://leetcode.com/problems/range-sum-query-immutable/) (Easy)
- [Find Pivot Index](https://leetcode.com/problems/find-pivot-index/) (Easy)
- [Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) (Medium, prefix sums + `HashMap<runningSum, count>`)
- [Contiguous Array](https://leetcode.com/problems/contiguous-array/) (Medium, prefix of `+1/-1`)
- Deeper (CSES):
  - [Subarray Sums I](https://cses.fi/problemset/task/1660) (exact target sum)
  - [Subarray Sums II](https://cses.fi/problemset/task/1661) (target sum, negatives allowed)

---

## 2D prefix sums — `PrefixSum.java#buildPrefix2D` / `#rangeSum2D`

**What & when.** O(1) rectangle-sum queries on a **static** grid.
**Complexity.** O(m·n) build, O(1) per query.
**Visual.** Query rows `r1..r2 = 2..4`, cols `c1..c2 = 2..4` on a 5×5 grid
filled 1..25. The vertical `||` and horizontal dashed line show where the prefix
array "cuts" the grid into four rectangular regions A/B/C/D.
```
          col 0   1  || col 2   3   4
        +-----------++---------------+
 row 0  |   1    2  ||   3    4    5 |
 row 1  |   6    7  ||   8    9   10 |   A (sum=16)  |  B (sum=39)
        +-----------++---------------+   ← row r1=2 boundary
 row 2  |  11   12  ||  13   14   15 |
 row 3  |  16   17  ||  18   19   20 |   C (sum=99)  |  D (sum=171) ← TARGET
 row 4  |  21   22  ||  23   24   25 |
        +-----------++---------------+
         cols 0..1     cols 2..4
                      ↑ col c1=2 boundary

  prefix[r1  ][c1  ] = prefix[2][2] = A             =  16
  prefix[r1  ][c2+1] = prefix[2][5] = A + B         =  55
  prefix[r2+1][c1  ] = prefix[5][2] = A + C         = 115
  prefix[r2+1][c2+1] = prefix[5][5] = A + B + C + D = 325

  D = (A+B+C+D) − (A+B) − (A+C) + A
                                ↑ subtracting (A+B) and (A+C) removes A twice,
                                  so add it back once
    = prefix[5][5] − prefix[2][5] − prefix[5][2] + prefix[2][2]
    = 325          − 55           − 115           + 16
    = 171  ✓

  Build recurrence — same idea in reverse (filling prefix cell by cell):
    prefix[i+1][j+1] = prefix[i+1][j]  +  prefix[i][j+1]  −  prefix[i][j]  +  grid[i][j]
                         (left strip)       (top strip)        (overlap cell,     (new cell)
                                                                added twice →
                                                                subtract once)
```
**Edge cases & gotchas.**
- Inclusion-exclusion: `rangeSum2D(r1,c1,r2,c2)` =
  `prefix[r2+1][c2+1] - prefix[r1][c2+1] - prefix[r2+1][c1] + prefix[r1][c1]` —
  the `+prefix[r1][c1]` corrects for double-subtracting the top-left rectangle.
- `prefix` is `(m+1) x (n+1)` with row/column `0` all zero, same sentinel idea
  as the 1D version.
- All ranges here are **inclusive**, 0-indexed.

**Active recall.**
- Q: Why `+ prefix[r1][c1]` in the rectangle formula? → A: Subtracting the two
  bordering rectangles (`prefix[r1][c2+1]` and `prefix[r2+1][c1]`) removes the
  top-left `r1 x c1` corner **twice**; adding it back once corrects for that
  (classic inclusion-exclusion).

**Practice.**
- [Range Sum Query 2D - Immutable](https://leetcode.com/problems/range-sum-query-2d-immutable/) (Medium)
- [Matrix Block Sum](https://leetcode.com/problems/matrix-block-sum/) (Medium)
- [Maximum Side Length of a Square with Sum Less than or Equal to Threshold](https://leetcode.com/problems/maximum-side-length-of-a-square-with-sum-less-than-or-equal-to-threshold/) (Medium, 2D prefix + binary search)

---

## Difference array — `PrefixSum.java#applyRangeUpdates`

**What & when.** Apply many "add `v` to every element in `[l, r]`" updates,
where the final array is only needed **once, after all updates** (not
interleaved with queries).
**Complexity.** O(1) per update, O(n) for the final prefix-sum pass — O(n + q)
total vs. O(n·q) for naively looping over each range.
**Edge cases & gotchas.**
- `diff[l] += val; diff[r+1] -= val;` — the `r+1` is what makes the effect
  "turn off" after index `r` once the running sum is taken.
- `diff` has size `n + 1` to safely write `diff[r+1]` when `r == n - 1`.
- **Interleaved queries break this** — if you need the array's state *between*
  updates, this isn't the right tool (use a Fenwick tree / BST instead).

**Active recall.**
- Q: Why does `diff[r+1] -= val` "cancel" the update? → A: The final array is a
  running sum (prefix sum) of `diff`. Adding `val` at `l` and subtracting it at
  `r+1` means the running sum includes `+val` for indices `l..r` and reverts to
  its prior value from `r+1` onward.
- Q: What if you have updates AND need the array after each one? → A: Difference
  arrays only support a single final materialization — use a segment tree /
  Fenwick tree for interleaved range-update + query (`../segmenttree/`).

**Practice.**
- [Range Addition](https://leetcode.com/problems/range-addition/) (Medium — the canonical difference-array problem)
- [Corporate Flight Bookings](https://leetcode.com/problems/corporate-flight-bookings/) (Medium)
- [Car Pooling](https://leetcode.com/problems/car-pooling/) (Medium)

---

## TODO — future additions

- **2D difference array** — the rectangle-update analog of `applyRangeUpdates`,
  for "add `v` to every cell in a sub-rectangle, read the grid once at the end".
- **Prefix XOR / prefix product** — same inclusion-exclusion idea with a
  different combine operator (XOR is its own inverse, so `xor(l,r) =
  prefix[r+1] ^ prefix[l]` with no subtraction).

## Cross-cutting reminders

- **Static data, many queries** → prefix sums. **Static updates, one final
  read** → difference array. **Both, interleaved** → segment tree / Fenwick
  (`../segmenttree/`).
- **Off-by-one is the recurring bug here** — always sanity-check with the
  smallest/whole-array case (`rangeSum(0, n-1)` should equal the total sum).
- Cast to `long` for running sums if individual values × n can exceed
  `Integer.MAX_VALUE`.
