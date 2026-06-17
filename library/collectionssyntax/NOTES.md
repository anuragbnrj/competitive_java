# Java Collections — revision notes

Pre-OA cheat sheet for `library/collectionssyntax/`. This folder is **API fluency**,
not algorithms: each file is a runnable demo whose `// expect:` comments show exact
behavior. Run any one with:

```
javac library/collectionssyntax/MapSyntax.java && java library.collectionssyntax.MapSyntax
```

## Decision guide — which structure when?

| Need | Use | File |
| --- | --- | --- |
| O(1) key → value lookup | `HashMap` | `MapSyntax.java` |
| Map in **insertion order** (or LRU) | `LinkedHashMap` | `MapSyntax.java` |
| Map sorted by key + neighbor/range queries | `TreeMap` | `TreeMapSyntax.java` |
| De-dup / membership test | `HashSet` | (see `MapSyntax` idioms) |
| Sorted **unique** + ceiling/floor | `TreeSet` | `TreeSetSyntax.java` |
| Count occurrences | `HashMap` + `merge` | `MapSyntax.java` |
| Index access / dynamic array | `ArrayList` | `ListSyntax.java` |
| **LIFO** (stack / DFS) | `ArrayDeque` (push/pop) | `StackSyntax.java` |
| **FIFO** (queue / BFS) | `ArrayDeque` (offer/poll) | `QueueSyntax.java` |
| Both ends (0-1 BFS / sliding window) | `ArrayDeque` (deque) | `DequeSyntax.java` |
| Top-K / priority | `PriorityQueue` | `../heaps/` |
| Built-in search over sorted data | `Collections`/`Arrays.binarySearch` | `BinarySearch.java` |
| Custom / multi-key ordering | `Comparator` (`comparing`/`thenComparing`) | `ComparatorSyntax.java` |
| Content-based key for a custom object | override `equals` + `hashCode` | `EqualsHashCode.java` |

**Stack = Queue = Deque = one class: `ArrayDeque`.** Only the method names (and the
mental model) differ:

| Role | add | remove | peek |
| --- | --- | --- | --- |
| Stack (LIFO) | `push` | `pop` | `peek` |
| Queue (FIFO) | `offer` | `poll` | `peek` |
| Deque (both) | `addFirst`/`addLast` | `pollFirst`/`pollLast` | `peekFirst`/`peekLast` |

## API cheat-sheet (most-used idioms)

- **HashMap**: `getOrDefault(k, 0)`, `merge(k, 1, Integer::sum)` (counting),
  `computeIfAbsent(k, x -> new ArrayList<>())` (multimap), `putIfAbsent`,
  `for (Map.Entry<K,V> e : map.entrySet())`.
- **TreeMap/TreeSet**: `ceiling(K)/floor(K)/higher(K)/lower(K)`,
  `firstKey()/lastKey()` (`first()/last()`), `subMap(from, to)` `[from, to)`.
- **ArrayDeque**: as stack `push/pop/peek`; as queue `offer/poll/peek`.
- **ArrayList**: `list.sort(Comparator...)`, `Comparator.reverseOrder()`,
  `Comparator.comparingInt(...).thenComparing(...)`.
- **Arrays**: `sort`, `fill`, `copyOf`/`copyOfRange`, `toString`/`deepToString`,
  `binarySearch`, `Arrays.stream(a).sum()`.
- **Collections**: `sort`, `reverse`, `max`/`min`, `frequency`, `swap`, `nCopies`,
  `reverseOrder()`.

## Gotcha catalog (the silent OA-killers)

- **`list.remove(int)` vs `remove(Object)`** — on an `Integer` list, `remove(2)`
  deletes **index 2**, not the value 2. Use `remove(Integer.valueOf(2))`.
- **`Arrays.asList(int[])`** → a `List<int[]>` of size **1** (primitives don't
  autobox into a list). Use `Integer[]` or a stream/loop.
- **Fixed-size / immutable**: `Arrays.asList(...)` is fixed-size (`add`/`remove`
  throw); `List.of(...)` / `Map.of(...)` are fully immutable (even `set` throws).
- **`Arrays.sort` on primitives takes no comparator** and only sorts ascending →
  box to `Integer[]` + `Collections.reverseOrder()` for descending.
- **`Integer` `==` vs `.equals`** — `==` compares references; only the −128..127
  cache makes it *seem* to work. Always `.equals` / `Integer.compare`.
- **Unboxing `null` → NPE** — `int x = map.get(missing)` throws. Use
  `getOrDefault`, or keep it as `Integer` and null-check.
- **`HashMap` iteration order is unspecified** — use `LinkedHashMap` (insertion) or
  `TreeMap` (sorted) when order matters.
- **`ConcurrentModificationException`** — don't `add`/`remove` on a collection
  during a for-each; use an explicit `Iterator` and `it.remove()`.
- **`subList(a, b)` is a VIEW** — writes pass through to the backing list, and a
  structural change to the backing list invalidates it.
- **`binarySearch` on a miss** returns `-(insertionPoint) - 1`, and the input
  **must be sorted**.
- **Comparator `a - b` overflow** for large/negative ints → `Integer.compare(a, b)`
  / `Long.compare`; chain with `thenComparing`.
- **`TreeMap`/`TreeSet` reject `null`** keys/elements; `ceiling`/`floor`/etc. return
  `null` when none exists.
- **`ArrayDeque` forbids `null`** elements (it uses `null` as the empty sentinel).
- **Legacy `Stack`** extends `Vector`, is synchronized (slower), and iterates
  **bottom-to-top**. Prefer `ArrayDeque`.
- **`String` is immutable** — build with `StringBuilder`; compare with `.equals`,
  not `==`.
- **`equals` without `hashCode` (or neither)** — a custom key needs *both* or
  `HashMap`/`HashSet` lookups silently miss. See *equals & hashCode* below.
- **Array as a hash key uses identity** — `int[]`/`Object[]` never match by content;
  use `List<Integer>` / `String`. See *equals & hashCode* below.
- **`TreeSet`/`TreeMap` equality is the comparator, not `equals`** — a comparator
  returning 0 drops the element as a duplicate. See *Custom comparison* below.
- **Sort stability**: primitives (`Arrays.sort(int[])`) are *not* stable; objects /
  `list.sort` are (TimSort). See *Custom comparison* below.

## Active recall

- Q: How do you delete the value `7` (not index 7) from a `List<Integer>`? → A:
  `list.remove(Integer.valueOf(7))` — bare `remove(7)` deletes index 7.
- Q: You need a map that keeps keys in the order inserted — which one? → A:
  `LinkedHashMap` (or `TreeMap` if you need *sorted* keys).
- Q: Sort an `int[]` descending — what's the catch? → A: `Arrays.sort` has no
  comparator for primitives; box to `Integer[]` and use
  `Collections.reverseOrder()`.
- Q: `Collections.binarySearch` returns `-5` — where would the element go? → A: at
  index `-(-5) - 1 = 4`.
- Q: Why prefer `ArrayDeque` over `Stack` and `LinkedList`? → A: faster
  (unsynchronized, cache-friendly), and it's the one class for stack/queue/deque.

## Practice (LeetCode, progressive: Easy → Medium → Hard)

- **HashMap / HashSet**
  - [Two Sum](https://leetcode.com/problems/two-sum/) (Easy)
  - [Group Anagrams](https://leetcode.com/problems/group-anagrams/) (Medium)
  - [LRU Cache](https://leetcode.com/problems/lru-cache/) (Medium, `LinkedHashMap`)
- **Stack**
  - [Valid Parentheses](https://leetcode.com/problems/valid-parentheses/) (Easy)
  - [Daily Temperatures](https://leetcode.com/problems/daily-temperatures/) (Medium, monotonic)
  - [Largest Rectangle in Histogram](https://leetcode.com/problems/largest-rectangle-in-histogram/) (Hard)
- **Queue / Deque**
  - [Number of Recent Calls](https://leetcode.com/problems/number-of-recent-calls/) (Easy)
  - [Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) (Hard, monotonic deque)
  - [Shortest Subarray with Sum at Least K](https://leetcode.com/problems/shortest-subarray-with-sum-at-least-k/) (Hard)
- **TreeMap / TreeSet**
  - [Two Sum III - Data structure design](https://leetcode.com/problems/two-sum-iii-data-structure-design/) (Easy)
  - [My Calendar I](https://leetcode.com/problems/my-calendar-i/) (Medium, `ceiling`/`floor`)
  - [Count of Range Sum](https://leetcode.com/problems/count-of-range-sum/) (Hard)
- **Arrays / Collections utilities**
  - [Merge Sorted Array](https://leetcode.com/problems/merge-sorted-array/) (Easy)
  - [Merge Intervals](https://leetcode.com/problems/merge-intervals/) (Medium, sort + comparator)
  - [The Skyline Problem](https://leetcode.com/problems/the-skyline-problem/) (Hard, `TreeMap`)

**Deeper (CSES):** no Collections-API section, but the
[Sorting and Searching](https://cses.fi/problemset/) set drills choosing the right
structure:
- [Sum of Two Values](https://cses.fi/problemset/task/1640) (HashMap / two pointers)
- [Towers](https://cses.fi/problemset/task/1073) (TreeMap / multiset)
- [Nested Ranges Count](https://cses.fi/problemset/task/2169) (sort + ordered structure)

## Custom comparison — `ComparatorSyntax.java`

**Comparable vs Comparator.** `Comparable<T>` defines a type's *one* natural order
(`int compareTo(T)`); `Comparator<T>` is an *external* order (`int compare(a, b)`)
you pass to `sort` / `PriorityQueue` / `TreeMap`. Both return `<0 / 0 / >0`.

**Three equivalent ways to write a `Comparator`:**
- **Named class** — `class ByAgeThenName implements Comparator<Person> { compare… }`.
  Prefer it for complex/multi-step logic, reuse across call sites/containers, and
  unit-testable, self-documenting ordering.
- **Anonymous class** — `new Comparator<Person>() { compare… }` (the pre-lambda idiom).
- **Lambda / fluent** — `Comparator.comparingInt(...).thenComparing(...)`. Best for
  quick inline orders.

**Fluent API (the modern way to build comparators):**
- `Comparator.comparingInt(p -> p.age)` / `comparing(p -> p.name)`
- `.thenComparing(p -> p.name)` — tie-break
- `.reversed()` — flip the whole order
- `Comparator.naturalOrder()` / `reverseOrder()`
- `Comparator.nullsFirst(naturalOrder())` — tolerate nulls

**Apply it across containers:** `list.sort(cmp)` / `Collections.sort(list, cmp)`;
`Arrays.sort(Integer[], cmp)`; `new PriorityQueue<>(cmp)`; `new TreeMap<>(cmp)` /
`new TreeSet<>(cmp)`.

**Gotchas.**
- **No comparator for primitives**: `Arrays.sort(int[])` is ascending-only — box to
  `Integer[]` to use a comparator (e.g. descending).
- **`a - b` overflow** → use `Integer.compare(a, b)` / `Long.compare`.
- **Stability**: `Arrays.sort(int[])` is dual-pivot quicksort (**not stable**);
  `Arrays.sort(Object[])` and `list.sort` are TimSort (**stable** — equal elements
  keep input order). Sort by multiple keys with `thenComparing`, not by relying on
  stability across two sorts.
- **"Comparison method violates its general contract"**: thrown (once n is large)
  when a comparator isn't a consistent total order (e.g. mixing `>` without a
  symmetric `<`). Keep it transitive; prefer the fluent builders.

**Active recall.**
- Q: Sort an `int[]` descending — what's the catch? → A: primitives take no
  comparator; box to `Integer[]` + `Comparator.reverseOrder()`.
- Q: Why `Integer.compare(a,b)` over `a-b`? → A: `a-b` overflows for large/negative
  values and returns the wrong sign.
- Q: You sorted by city, then by name, expecting both to hold — why might city
  order break? → A: a second full sort only respects the last key; combine keys in
  one comparator via `thenComparing`.

**Interviewer follow-up.**
- "Now sort by a third key too" → extend the `thenComparing` chain; this is why
  the fluent/named-class forms are preferred over an ad-hoc inline lambda for
  anything beyond a single key.

**Practice.**
- [Sort Array By Parity](https://leetcode.com/problems/sort-array-by-parity/) (Easy)
- [Sort the People](https://leetcode.com/problems/sort-the-people/) (Easy)
- [Queue Reconstruction by Height](https://leetcode.com/problems/queue-reconstruction-by-height/) (Medium)
- [Largest Number](https://leetcode.com/problems/largest-number/) (Medium, comparator on concatenation)
- [Maximum Performance of a Team](https://leetcode.com/problems/maximum-performance-of-a-team/) (Hard, sort + heap)

**Deeper (CSES):**
- [Restaurant Customers](https://cses.fi/problemset/task/1619)
- [Movie Festival](https://cses.fi/problemset/task/1629)

## equals & hashCode — `EqualsHashCode.java`

**The contract.** If `a.equals(b)` then `a.hashCode() == b.hashCode()`. Override
**both or neither**. Hash collisions (equal hash, non-equal objects) are fine —
`equals` is the tiebreaker. Use `Objects.equals(...)` / `Objects.hash(...)`; a Java
16+ `record` auto-generates both (and `toString`).

**Why it's an OA correctness trap.**
- A custom object with **no override** uses identity (`==`) equality, so two
  equal-content instances are different `HashSet`/`HashMap` keys.
- **`int[]` (any array) as a key uses identity** → a fresh equal-content array never
  matches. Use `List<Integer>` or a `String` key (both have content equality).
- **`TreeSet`/`TreeMap` decide equality by `compareTo`/comparator, NOT `equals`** →
  a comparator that returns 0 for two different objects silently treats them as
  duplicates and drops one.
- **Mutating a key** after insertion changes its hash bucket / sort position and
  corrupts the structure — keys should be effectively immutable.

**Active recall.**
- Q: You put a custom `Point` in a `HashSet` and `contains(new Point(1,2))` returns
  false — why? → A: no `equals`/`hashCode` override → identity equality.
- Q: Why does `map.get(new int[]{1,2})` miss after `put(new int[]{1,2}, …)`? → A:
  arrays use identity equals/hashCode; use `List<Integer>`/`String` keys.
- Q: A `TreeSet` with `comparingInt(p -> p.x)` drops `(1,9)` after `(1,2)` — why? →
  A: tree equality is the comparator (returns 0), not `equals`.

**Interviewer follow-up.**
- "Use this object as a `HashMap` key in a service that mutates it" → the key
  must be **effectively immutable** (no setters touching hashed fields), or a
  later mutation moves it to the wrong bucket and `get` silently misses — the
  same root cause as the "mutating a key" gotcha above.

**Practice.**
- [Design HashSet](https://leetcode.com/problems/design-hashset/) (Easy)
- [Group Anagrams](https://leetcode.com/problems/group-anagrams/) (Medium, sorted-string key)
- [Insert Delete GetRandom O(1)](https://leetcode.com/problems/insert-delete-getrandom-o1/) (Medium)
- [Max Points on a Line](https://leetcode.com/problems/max-points-on-a-line/) (Hard, slope as a map key)

## TODO — future additions

- `EnumMap` / `BitSet` (compact integer/enum-keyed structures).
- `Map.Entry` + Stream pipelines (`entrySet().stream()...`).
- Immutable-collection nuances (`Collectors.toUnmodifiableList`, `copyOf`).

## Cross-cutting reminders

- Default `PriorityQueue` is a min-heap → see `../heaps/NOTES.md`.
- `remove(int)` vs `remove(Object)`, `Arrays.asList(int[])`, and `Integer ==` are
  the three traps most likely to cost you an OA — internalize them.
- Use `offer`/`poll`/`peek` (return `null`/`false`) over `add`/`remove`/`element`
  (which throw) unless you *want* the exception.
