# Bit Manipulation — revision notes

Pre-OA cheat sheet for `library/bits/`. The template is a self-contained,
runnable class (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/bits/BitTricks.java && java library.bits.BitTricks
```

## Decision guide — which technique?

| Problem signal | Technique | File |
| --- | --- | --- |
| "isolate the lowest set bit" / Fenwick-tree index math | `x & -x` | `BitTricks.java#lowestSetBit` |
| "count bits" / "number of 1s" | popcount (`Integer.bitCount` or Brian Kernighan) | `BitTricks.java#countSetBits` |
| "iterate all subsets of a set/mask" (bitmask DP, brute-force subsets) | submask enumeration | `BitTricks.java#subsetsOf` |
| "max/min XOR of a subset" / "can we form XOR value V from a subset" | XOR linear basis | `BitTricks.java#maxXorSubset` |
| appears exactly once / twice while others appear 2x / 3x | XOR (once) or per-bit counting mod 3 (twice) | see Practice below |
| "add without `+`" | bit-by-bit add with carry via AND/XOR/shift | see Practice below |

**The unifying idea:** bits are independent — most tricks either (a) manipulate
one bit in isolation (`x & -x`, `x & (x-1)`), (b) treat each of the 32 bit
positions as its own independent counter (popcount, "appears 3x" problems,
prefix-XOR), or (c) enumerate the 2^k combinations of a small set (submasks,
bitmask DP).

---

## Lowbit & popcount — `BitTricks.java#lowestSetBit` / `#countSetBits`

**What & when.** `x & -x` isolates the lowest set bit — the core of Fenwick
tree index arithmetic (`i += i & -i` / `i -= i & -i`) and "lowest set bit"
puzzles. `x & (x - 1)` clears the lowest set bit — the core of popcount and
"power of two" checks (`x != 0 && (x & (x-1)) == 0`).
**Complexity.** O(1) for `lowestSetBit`; O(popcount(x)) for the Brian Kernighan
loop (only iterates once per set bit, not once per bit position).
**Edge cases & gotchas.**
- `x & -x` on `x == 0` returns `0` — make sure callers don't divide by it or
  loop forever.
- `Integer.MIN_VALUE` (`0x80000000`) is its own negation in two's complement —
  `x & -x` still correctly returns `0x80000000` (the sign bit), but be aware
  `-x` overflows silently for this one value.
- In real code, prefer `Integer.bitCount(x)` / `Long.bitCount(x)` (JIT-compiled
  to a hardware popcount instruction) — the manual loop here is for when an
  interviewer asks you to implement it.

**Active recall.**
- Q: Why does `x & (x - 1)` clear exactly the lowest set bit? → A: `x - 1`
  flips the lowest set bit to `0` and turns every bit below it (which were all
  `0`) into `1`. ANDing with `x` keeps all higher bits the same, zeroes the
  lowest set bit, and the now-mismatched lower bits (1 in `x-1`, 0 in `x`) AND
  to `0`.
- Q: How does `x & -x` relate to Fenwick tree traversal? → A: `-x` (two's
  complement) equals `~x + 1`; ANDing with `x` isolates the lowest set bit,
  which is exactly the "range size" a Fenwick index `x` is responsible for —
  `x += x & -x` moves to the next index that includes `x`'s range, `x -= x &
  -x` moves to the parent range.

**Practice.**
- [Number of 1 Bits](https://leetcode.com/problems/number-of-1-bits/) (Easy)
- [Power of Two](https://leetcode.com/problems/power-of-two/) (Easy, `x & (x-1) == 0`)
- [Counting Bits](https://leetcode.com/problems/counting-bits/) (Easy, DP: `bits[i] = bits[i & (i-1)] + 1`)
- [Sum of Two Integers](https://leetcode.com/problems/sum-of-two-integers/) (Medium, add via `a ^ b` + carry `(a & b) << 1`, loop until carry is 0)

---

## Submask enumeration — `BitTricks.java#subsetsOf`

**What & when.** Enumerate every subset of a small set (encoded as a bitmask)
— the backbone of bitmask DP ("traveling salesman over ≤ 20 items",
"partition into K equal-sum subsets") and brute-force "try all subsets"
problems where `n` is small (≤ ~20).
**Complexity.** O(3^n) to enumerate, for every mask, all of its submasks (the
classic SOS/bitmask-DP bound) — O(2^n) to enumerate the submasks of one fixed
mask.
**Edge cases & gotchas.**
- The loop `for (int sub = mask; ; sub = (sub-1) & mask)` must check `sub ==
  0` **after** adding it and **break**, not loop — `(0 - 1) & mask` would wrap
  to `mask` and loop forever.
- This enumerates submasks of **one given mask** (`2^popcount(mask)` of them),
  not all `2^n` masks of `n` bits — for the latter just loop
  `for (int mask = 0; mask < (1 << n); mask++)`.
- The order produced is **descending** (`mask` first, `0` last) — fine for most
  uses, but don't assume ascending order if the problem cares.

**Active recall.**
- Q: Why is enumerating submasks of every mask O(3^n) and not O(4^n)? → A:
  Each pair `(mask, sub)` where `sub ⊆ mask` corresponds to assigning each of
  the n bits to one of **3** states: "in `sub`", "in `mask` but not `sub`", or
  "not in `mask`" — hence 3^n total pairs across all masks.

**Practice.**
- [Subsets](https://leetcode.com/problems/subsets/) (Medium, bitmask or backtracking)
- [Partition to K Equal Sum Subsets](https://leetcode.com/problems/partition-to-k-equal-sum-subsets/) (Medium, bitmask DP)
- [Smallest Sufficient Team](https://leetcode.com/problems/smallest-sufficient-team/) (Hard, bitmask DP over skill sets)

---

## XOR linear basis — `BitTricks.java#maxXorSubset`

**What & when.** Given an array, find the maximum (or check feasibility of a
target) XOR achievable by XOR-ing together any subset of its elements. The
basis is a set of ≤ 32 numbers, each contributing a distinct highest bit, such
that every achievable subset-XOR is some XOR-combination of basis elements.
**Complexity.** O(n · 32) to build the basis, O(32) per max/feasibility query.
**Edge cases & gotchas.**
- Building the basis: for each number, walk bits from high to low; if the
  number has bit `i` set and `basis[i]` is empty, place it there and stop;
  otherwise XOR it with `basis[i]` (eliminating that bit) and continue. A
  number that reduces to `0` contributes nothing new (it's a linear
  combination of existing basis vectors).
- Computing the max: greedily, from the highest bit down, XOR `basis[i]` into
  the running result **whenever doing so increases it** — this greedy choice
  is optimal because each `basis[i]` has a unique highest bit no other basis
  element shares.
- The **empty subset** (XOR = 0) is always achievable — if the problem requires
  a *non-empty* subset and the answer comes out `0`, that's still valid (0 is
  achievable by any element XORed with itself in a multiset, or by the empty
  subset if allowed).

**Active recall.**
- Q: Why does the greedy max-XOR pass work from high bit to low? → A: Each
  `basis[i]` is the *only* basis vector with bit `i` set (by construction), so
  including it flips bit `i` of the result without affecting any bit above
  `i` — making that choice independently optimal, bit by bit, from the top
  down.
- Q: How would you check if a target value `V` is achievable as a subset XOR?
  → A: Reduce `V` against the basis the same way numbers are inserted (XOR
  with `basis[i]` whenever bit `i` of the current value is set); `V` is
  achievable iff it reduces to `0`.

**Practice.**
- [Maximum XOR of Two Numbers in an Array](https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/) (Medium, trie-based — a different but related technique)
- [Maximum XOR With an Element From Array](https://leetcode.com/problems/maximum-xor-with-an-element-from-array/) (Hard, trie + offline queries)
- Single Number family (per-bit counting, not basis):
  - [Single Number](https://leetcode.com/problems/single-number/) (Easy, XOR everything)
  - [Single Number II](https://leetcode.com/problems/single-number-ii/) (Medium, count each bit mod 3)
  - [Single Number III](https://leetcode.com/problems/single-number-iii/) (Medium, XOR all then split by a distinguishing bit)

---

## TODO — future additions

- **Trie-based max XOR pair** (LC 421/1707) — a different family from the
  linear-basis "max XOR subset" above; worth its own template if revisited.
- **Bitmask DP worked example** (e.g. Partition to K Equal Sum Subsets) —
  `subsetsOf` is the enumeration primitive; a full DP template would show how
  it's used with memoization.

## Cross-cutting reminders

- **`Integer.bitCount`/`Long.bitCount`** exist and are hardware-accelerated —
  use them directly in real code; the manual loops here are for when an
  interviewer wants to see the bit-trick reasoning.
- **Watch the sign bit.** Java `int`/`long` are signed; `>>` is arithmetic
  (sign-extending) while `>>>` is logical (zero-filling) — use `>>>` when
  treating a value as an unsigned bit pattern.
- **Per-bit independence** is the recurring insight for "appears N times"
  problems: sum each bit position's count across all numbers independently,
  then reconstruct.
