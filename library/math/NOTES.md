# Math — revision notes

Pre-OA cheat sheet for `library/math/`. Each template is a self-contained,
runnable class (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/math/Sieve.java && java library.math.Sieve
javac library/math/MathUtils.java && java library.math.MathUtils
```

## Decision guide — which technique?

| Problem signal | Use | File |
| --- | --- | --- |
| Prime check / list all primes up to N | Sieve of Eratosthenes | `Sieve.java` |
| GCD / LCM of two numbers | Euclidean algorithm | `MathUtils.java#gcd/lcm` |
| `(base ^ exp) % mod` for huge `exp` | fast modular exponentiation | `MathUtils.java#modPow` |
| "divide" under a modulus (`a / b mod p`) | modular inverse (Fermat, prime `p`) | `MathUtils.java#modInverse` |
| Count combinations `nCr` with large `n`, mod `p` | factorials + modular inverse | `MathUtils.java#nCrMod` |

---

## Sieve of Eratosthenes — `Sieve.java`

**What & when.** Mark all primes up to `n` in one pass; O(1) prime checks
afterward.
**Complexity.** O(n log log n) time, O(n) space.
**Edge cases & gotchas.**
- `isPrime[0] = isPrime[1] = false` — 0 and 1 are not prime.
- Outer loop condition `i * i <= n` uses `long i` so `i * i` can't overflow
  `int` for large `n`.
- Only mark multiples starting from `i * i` (smaller multiples of `i` were
  already marked by smaller primes) — starting from `2*i` is correct but
  wasteful, not wrong.
- `getSieve` is **private** here — make it `public` (or copy the body) to call
  it from another class.

**Active recall.**
- Q: Why start marking multiples at `i*i` instead of `2*i`? → A: Every multiple
  of `i` smaller than `i*i` has a prime factor smaller than `i` and was already
  marked when processing that smaller factor.

**Practice.**
- [Count Primes](https://leetcode.com/problems/count-primes/) (Medium)
- Deeper (CSES):
  - [Counting Coprime Pairs](https://cses.fi/problemset/task/2417)

---

## GCD / LCM — `MathUtils.java#gcd` / `#lcm`

**What & when.** Reduce fractions, find a common period/cycle length, "smallest
array that..." problems.
**Complexity.** O(log(min(a,b))).
**Edge cases & gotchas.**
- `gcd(0, x) == x` — the recursive base case (`b == 0 → return a`) handles this
  automatically.
- **`lcm`: divide before multiplying** — `a / gcd(a,b) * b`, not
  `a * b / gcd(a,b)`. The latter can overflow `long` even when the final LCM
  fits.

**Active recall.**
- Q: Why does `lcm` divide by `gcd` *before* multiplying by `b`? → A: `a * b`
  can overflow before the division brings the value back down to a result that
  fits; dividing first keeps every intermediate value ≤ the final answer.

**Practice.**
- [Greatest Common Divisor of Strings](https://leetcode.com/problems/greatest-common-divisor-of-strings/) (Easy)
- [Nice Subarray](https://leetcode.com/problems/find-the-longest-substring-containing-vowels-in-even-counts/) — *(LCM/period idea, not literal GCD)*

---

## Fast modular exponentiation — `MathUtils.java#modPow`

**What & when.** `(base^exp) % mod` where `exp` can be astronomically large
(e.g. `10^9`, `10^18`) — repeated multiplication would be O(exp).
**Complexity.** O(log exp) — binary exponentiation ("square and multiply").
**Edge cases & gotchas.**
- Reduce `base %= mod` **before** the loop; if `base` was negative, add `mod`
  back (Java's `%` can return a negative result).
- Multiply in `long` and reduce mod `m` at **every** step
  (`result * base % mod`, `base * base % mod`) — never let the product itself
  overflow before reducing.
- This assumes `mod` small enough that `(mod-1)^2` fits in `long`
  (true for the usual `1e9+7`); for `mod` near `2^63` you'd need 128-bit
  multiplication (`Math.multiplyHigh` or `BigInteger`).

**Active recall.**
- Q: Why is binary exponentiation O(log exp) instead of O(exp)? → A: Each
  iteration either squares the base or not, halving `exp`; `exp` reaches 0 in
  `log2(exp)` steps, and the running product accumulates the bits of `exp` that
  are set.

**Practice.**
- Deeper (CSES):
  - [Exponentiation](https://cses.fi/problemset/task/1095)
  - [Exponentiation II](https://cses.fi/problemset/task/1712)

---

## Modular inverse & `nCr mod p` — `MathUtils.java#modInverse` / `#nCrMod`

**What & when.** Combinatorics ("count the ways...") where the answer is taken
mod a large prime (commonly `1e9+7`) — division isn't defined mod `p`, so
"divide by `x`" becomes "multiply by `x`'s modular inverse".
**Complexity.** `modInverse` is O(log p) (one `modPow` call). `nCrMod` here is
O(n) per call (recomputes factorials).
**Edge cases & gotchas.**
- **`modInverse` requires `mod` to be PRIME** (Fermat's little theorem:
  `a^(p-1) ≡ 1 (mod p)` ⇒ `a^(p-2) ≡ a^-1`). For a composite modulus, use the
  extended Euclidean algorithm instead (TODO).
- Also requires `gcd(a, mod) == 1` — if `a` is a multiple of `p`, no inverse
  exists (not an issue for `1e9+7` with typical small factorials, but worth
  knowing).
- `nCrMod` recomputes the factorial array **every call** — fine for one-off use;
  if called in a loop with the same `n`, precompute `fact[]`/`invFact[]` once
  (O(n)) and answer each query in O(1).
- `nCrMod(n, r, ...)` returns `0` for `r < 0 || r > n` — the conventional "0
  ways" rather than throwing.

**Active recall.**
- Q: Why does `modInverse` need `mod` to be prime? → A: Fermat's little theorem
  (`a^(p-1) ≡ 1 mod p`) only holds for prime `p`; composite moduli need the
  extended Euclidean algorithm.
- Q: `nCrMod` is called 10^5 times with the same `n` — what's the fix? → A:
  precompute `fact[]` and `invFact[]` once in O(n), then each `nCr` is O(1)
  (`fact[n] * invFact[r] % p * invFact[n-r] % p`).

**Practice.**
- [Unique Paths](https://leetcode.com/problems/unique-paths/) (Medium, `nCr`
  alternative to the DP solution)
- Deeper (CSES):
  - [Binomial Coefficients](https://cses.fi/problemset/task/1079)

---

## TODO — future additions

- **Extended Euclidean algorithm** — modular inverse for non-prime `mod`, and
  general `ax + by = gcd(a,b)` (linear Diophantine) problems.
- **Smallest-prime-factor sieve** — O(n log log n) precompute, then O(log n)
  prime factorization of any number ≤ n.
- **Matrix exponentiation** — linear recurrences (Fibonacci-style) in
  O(k³ log n).

## Cross-cutting reminders

- **`1e9+7` is the standard modulus** — prime, fits comfortably so
  `(mod-1)^2 < Long.MAX_VALUE`.
- **Reduce mod at every multiplication**, not just at the end — intermediate
  products overflow `int` almost immediately and can overflow `long` too if you
  chain several multiplies unreduced.
- **Java's `%` can return negative** for negative operands —
  `((a % m) + m) % m` to normalize.
