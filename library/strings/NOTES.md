# Strings — revision notes

Pre-OA cheat sheet for `library/strings/`. Each template is a self-contained,
runnable class (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/strings/Kmp.java && java library.strings.Kmp
javac library/strings/RabinKarp.java && java library.strings.RabinKarp
```

## Decision guide — which technique?

| Problem signal | Use | File |
| --- | --- | --- |
| Find all occurrences of **one fixed pattern** in a text, O(n+m) | KMP (failure function) | `Kmp.java` |
| Compare/match **many different substrings** quickly (hash & compare) | Rabin-Karp (rolling hash) | `RabinKarp.java` |
| Prefix-based dictionary: insert/search/startsWith, autocomplete | Trie | `../trie/` |
| Longest prefix of `s` that's also a suffix | KMP failure function on `s` itself | `Kmp.java#buildFailure` |

---

## KMP — `Kmp.java`

**What & when.** Find every occurrence of `pattern` in `text` without the naive
O(n·m) re-scan on every mismatch.
**Complexity.** O(n + m): O(m) to build the failure function, O(n) to scan.
**Edge cases & gotchas.**
- `fail[i]` = length of the longest **proper** prefix of `p[0..i]` that's also a
  suffix of `p[0..i]` — "proper" excludes the whole `p[0..i]` itself.
- After a full match (`k == pattern.length()`), set `k = fail[k-1]` and keep
  going — **don't reset `k = 0`**, or you'll miss overlapping matches (e.g.
  "aa" in "aaaa" occurs at `[0, 1, 2]`, not just `[0, 2]`).
- Empty pattern: this template returns no matches; some problems (LC28
  `strStr`) define empty pattern → index `0` — check the spec.
- Results are **0-indexed** start positions.

**Active recall.**
- Q: Why `k = fail[k-1]` (not `k = 0`) after a match? → A: It allows overlapping
  matches — resetting to 0 would skip occurrences that share characters with
  the one just found.
- Q: What does `fail[i]` represent, and why does "proper" matter? → A: The
  longest prefix of `p` that's also a suffix of `p[0..i]`, excluding `p[0..i]`
  itself — on a mismatch it tells you how far you can "slide" `pattern` without
  re-comparing characters you already know match.

**Practice.**
- [Implement strStr()](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) (Easy)
- [Longest Happy Prefix](https://leetcode.com/problems/longest-happy-prefix/) (Hard, literally `buildFailure`)
- [Repeated String Match](https://leetcode.com/problems/repeated-string-match/) (Medium)
- [Shortest Palindrome](https://leetcode.com/problems/shortest-palindrome/) (Hard, KMP on `s + '#' + reverse(s)`)

---

## Rabin-Karp — `RabinKarp.java`

**What & when.** Same single-pattern matching as KMP, via a rolling polynomial
hash. The real value is the **rolling hash technique itself** — once you can
hash any substring in O(1) (after an O(n) precompute), you can compare
arbitrary substrings for problems KMP doesn't directly address (e.g. "does a
substring of length L repeat anywhere?").
**Complexity.** O(n + m) average; a hash match is verified with
`regionMatches` (O(m) worst case per candidate, but candidates are rare).
**Edge cases & gotchas.**
- `pow` must equal `BASE^(m-1) mod MOD` for the rolling update — it's built
  incrementally in the same loop that computes the initial hashes.
- Java's `%` can return a **negative** result for negative operands — the
  rolling update adds `MOD` before reducing the subtraction term.
- Single base/modulus can theoretically collide on adversarial input; the
  `regionMatches` check makes this template **correct regardless**, just
  potentially slower on a pathological input. For CP with adversarial tests,
  use two independent (base, mod) pairs.
- For "does a length-L substring repeat" (e.g. Longest Duplicate Substring),
  precompute rolling hashes for **all** prefixes once, then compare
  `hash(i, i+L)` across different `i` in O(1) — a different shape from this
  single-pattern `search`.

**Active recall.**
- Q: KMP vs Rabin-Karp — when prefer which? → A: KMP for one fixed pattern,
  guaranteed O(n+m), no collision risk. Rabin-Karp when you need to compare many
  *different* substrings against each other (e.g. binary search on length +
  hashing for the longest duplicate substring).
- Q: Why verify with `regionMatches` even after the hashes match? → A: Hash
  collisions are possible (different strings, same hash mod M) — the direct
  compare guarantees correctness.

**Practice.**
- [Repeated DNA Sequences](https://leetcode.com/problems/repeated-dna-sequences/) (Medium)
- [Implement strStr()](https://leetcode.com/problems/find-the-index-of-the-first-occurrence-in-a-string/) (Easy, alternative to KMP)
- [Longest Duplicate Substring](https://leetcode.com/problems/longest-duplicate-substring/) (Hard, binary search + rolling hash)

---

## Trie — see `../trie/NOTES.md`

Trie templates (canonical LC208 `Trie.java` and counting/cost `TrieCount.java`)
are documented in `../trie/NOTES.md` with their own decision guide, gotchas,
active-recall Q&A, and practice links.

---

## TODO — future additions

- **Z-function** — alternative to the KMP failure function; sometimes more
  natural for "longest substring starting at `i` that's also a prefix" queries.
- **Manacher's algorithm** — O(n) longest palindromic substring (vs. an O(n log n)
  binary-search-on-length + hashing approach).
- **Suffix array / suffix automaton** — advanced, rarely needed for SDE2 OAs;
  listed for completeness.

## Cross-cutting reminders

- KMP/Rabin-Karp **find** a pattern; a Trie **organizes** many strings for
  prefix queries — different problem shapes, don't conflate them.
- `String` is immutable — build with `StringBuilder` for repeated concatenation
  (see `../collectionssyntax/NOTES.md`).
- All results here are **0-indexed**.
