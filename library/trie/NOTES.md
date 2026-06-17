# Trie — revision notes

Pre-OA cheat sheet for `library/trie/`. Each template is a self-contained,
runnable class (`main` has a worked self-test with `// expect:` outputs). Verify:

```
javac library/trie/Trie.java      && java library.trie.Trie
javac library/trie/TrieCount.java && java library.trie.TrieCount
```

## Decision guide — which file?

| Problem signal | Use | File |
| --- | --- | --- |
| LC208: `insert` / `search` (exact) / `startsWith` (prefix) | canonical Trie | `Trie.java` |
| Count how many times a word was inserted; count words sharing a prefix; `erase`; min-cost per word | count/cost Trie | `TrieCount.java` |
| Autocomplete / word-break / "longest word built from other inserted words" | canonical Trie — add a DFS/BFS over nodes | `Trie.java` |
| Characters outside lowercase `a`-`z` (digits, mixed case, unicode) | swap the 26-array for `Map<Character, Node>` per node | adapt either file |

---

## Canonical Trie — `Trie.java`

**What & when.** A 26-ary prefix tree: insert words, then answer exact-word and
prefix queries in O(L) per operation (L = word length). Standard LC208 interface.
**Complexity.** O(L) per `insert`/`search`/`startsWith`; O(total characters) space.
**Edge cases & gotchas.**
- `links[26]` assumes lowercase `a`-`z` only — for other alphabets swap to
  `Map<Character, Node>` inside each node.
- `search` checks `node.isEnd`: reaching the node for the last character is
  **not** sufficient — `startsWith("app")` returns `true` even if "app" was never
  inserted.
- Empty string: `insert("")` sets `root.isEnd = true`; `search("")` returns
  `root.isEnd` (which may be unexpected — guard if the problem disallows it).
- This is the interface expected by LC208/LC211/LC212; do **not** try to repurpose
  `TrieCount`'s `countWordsEqualTo` / `countWordsStartingWith` as boolean returns.

**Active recall.**
- Q: What's the difference between `search("app")` returning false and
  `startsWith("app")` returning true after only inserting "apple"? → A: `search`
  checks `node.isEnd` (was this exact word inserted?); `startsWith` only checks
  that the path exists — no `isEnd` check needed.
- Q: Why a 26-element array instead of a `HashMap<Character, Node>`? → A: O(1)
  child lookup with no hashing overhead, at the cost of assuming lowercase
  `a`-`z` and wasting space for sparse tries.

**Practice.**
- [Implement Trie (Prefix Tree)](https://leetcode.com/problems/implement-trie-prefix-tree/) (Medium, LC208 — this template directly)
- [Design Add and Search Words Data Structure](https://leetcode.com/problems/design-add-and-search-words-data-structure/) (Medium, wildcard `.` → DFS over all children)
- [Word Search II](https://leetcode.com/problems/word-search-ii/) (Hard, Trie + grid DFS)
- [Replace Words](https://leetcode.com/problems/replace-words/) (Medium, stop at first `isEnd` during traversal)
- [Longest Word in Dictionary](https://leetcode.com/problems/longest-word-in-dictionary/) (Medium, BFS over trie nodes where `isEnd` is true)

---

## Count/cost Trie — `TrieCount.java`

**What & when.** Extends the prefix tree with per-node insertion counters
(`countEndsWith`, `countPrefix`) and a per-word minimum cost (`minCostEndAt`).
Use when the problem asks "how many words share this prefix" or "how many times
was this exact word inserted" — not answerable with a boolean `isEnd` alone.
**Complexity.** O(L) per operation; O(total characters) space.
**Edge cases & gotchas.**
- `erase` **decrements** counters but does **not** unlink nodes — orphaned paths
  remain in memory. Correct for counting use-cases; irrelevant for OA-sized input.
- `countWordsStartingWith` returns the `countPrefix` at the last node of the
  prefix, **not** `countEndsWith` — it counts all insertions whose word passed
  through that node (not just words ending there).
- `minCostEndAt` is maintained inside `Node` but not exposed through a getter in
  this template — add one if your problem needs it.
- Insert the same word multiple times: `countWordsEqualTo` grows by 1 each time;
  `erase` removes one insertion (decrements by 1), not all.

**Active recall.**
- Q: After inserting "apple" twice and "app" once, what does
  `countWordsStartingWith("app")` return? → A: 3 — three insertions passed
  through the second 'p' node.
- Q: `erase("apple")` is called once after two inserts — what does
  `countWordsEqualTo("apple")` return? → A: 1 (one insertion remaining).

**Practice.**
- [Design a Number Container System](https://leetcode.com/problems/design-a-number-container-system/) (Medium, counting variant mindset)
- [Map Sum Pairs](https://leetcode.com/problems/map-sum-pairs/) (Medium, sum-at-prefix — same shape as `countPrefix` but summing values)
- [Implement Magic Dictionary](https://leetcode.com/problems/implement-magic-dictionary/) (Medium, one-character-off search — walk the trie with a mismatch budget)

---

## TODO — future additions

- **Trie with `Map<Character, Node>`** — drop-in replacement when the alphabet
  isn't lowercase `a`-`z` (digits, uppercase, unicode). Worth a short variant
  once a problem requires it.
- **Suffix array / suffix automaton** — advanced, rarely needed for SDE2 OAs;
  listed for completeness.

## Cross-cutting reminders

- `Trie.java` answers boolean queries; `TrieCount.java` answers count queries —
  pick deliberately, don't conflate them.
- **26-array = fast but alphabet-constrained.** If the problem says "any
  character", switch to `Map<Character, Node>` before starting.
- Trie **organizes** many strings for prefix queries — a different shape from
  KMP/Rabin-Karp, which **search** for one pattern inside a text. See
  `../strings/NOTES.md` for the string-matching templates.
