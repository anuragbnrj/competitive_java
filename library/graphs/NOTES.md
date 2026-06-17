# Graphs — revision notes

Pre-OA cheat sheet for `library/graphs/`. Each template is a self-contained,
runnable class (`main` has a worked self-test). Verify any one with:

```
javac library/graphs/Dijkstra.java && java library.graphs.Dijkstra
```

## Decision guide — which algorithm?

| Problem signal | Use | File |
| --- | --- | --- |
| Shortest path, **unweighted** (fewest steps/edges) | BFS | `Bfs.java` |
| Shortest path on a **grid**, possibly many starts | Multi-source BFS | `Bfs.java` |
| Shortest path, **weights are only 0/1** | 0-1 BFS (deque) | `ZeroOneBfs.java` |
| Shortest path, **non-negative** weights | Dijkstra | `Dijkstra.java` |
| Shortest path **with negative** edges / detect neg cycle | Bellman-Ford | `BellmanFord.java` |
| **All-pairs** shortest path, small n (≤ ~400) | Floyd-Warshall | `FloydWarshall.java` |
| Order tasks with prerequisites / detect dependency cycle | Topological sort | `TopoSort.java` |
| "Can we finish?" / is there a cycle (directed or undirected) | Cycle detection | `CycleDetection.java` |
| Count groups / "are X and Y connected" / dynamic merges | Union-Find (DSU) | `UnionFind.java` |
| Count islands / regions / connected components | DFS/BFS flood fill | `Dfs.java`, `Bfs.java` |
| Min cost to connect all nodes | MST (Kruskal/Prim) | `Mst.java` |
| 2-color / "can split into two teams" / odd cycle check | Bipartite | `Bipartite.java` |
| Mutual reachability groups in a **directed** graph | Tarjan SCC | `TarjanScc.java` |
| Critical edges/nodes whose removal disconnects graph | Bridges / articulation | `BridgesArticulation.java` |

Representations used here: **unweighted** = `List<List<Integer>>`; **weighted** =
`List<List<int[]>>` with `{neighbor, weight}`; **edge list** = `int[][]` of
`{u, v}` or `{u, v, w}`. All graphs are 0-indexed.

---

## BFS — `Bfs.java`

**What & when.** Shortest path in *edges* on an unweighted graph; level-order
traversal. Multi-source variant seeds the queue with every source at distance 0
(rotting oranges, distance-to-nearest).
**Complexity.** O(V + E) time, O(V) space.
**Edge cases & gotchas.**
- Set `dist`/visited **when enqueuing**, not when dequeuing — otherwise a node
  is pushed multiple times and work blows up.
- BFS gives shortest path **only when all edge weights are equal**. Mixed
  weights → Dijkstra / 0-1 BFS.
- Grid: bounds-check *before* indexing; the 4-direction `dr/dc` arrays are the
  standard idiom (add 4 diagonals for 8-direction).

**Active recall.**
- Q: Why mark visited on enqueue rather than dequeue? → A: A node can be reached
  by several neighbors before it's polled; marking on dequeue lets it enter the
  queue once per in-edge → O(V·E) blowup and wrong layering.
- Q: When does BFS stop being a valid shortest-path algorithm? → A: As soon as
  edge weights differ.

**Practice.**
- [Rotting Oranges](https://leetcode.com/problems/rotting-oranges/)
- [01 Matrix](https://leetcode.com/problems/01-matrix/)
- [Word Ladder](https://leetcode.com/problems/word-ladder/)
- [Shortest Path in Binary Matrix](https://leetcode.com/problems/shortest-path-in-binary-matrix/)
- Deeper (CSES):
  - [Counting Rooms](https://cses.fi/problemset/task/1192)
  - [Labyrinth](https://cses.fi/problemset/task/1193)
  - [Message Route](https://cses.fi/problemset/task/1667)

---

## DFS — `Dfs.java`

**What & when.** Traversal, flood fill, connected components; the backbone of
topo sort, cycle detection, SCC, bridges.
**Complexity.** O(V + E) time. Recursive depth O(V).
**Edge cases & gotchas.**
- **Recursion depth**: a chain/path of ~10^4–10^5 nodes can `StackOverflowError`
  on the default JVM stack. Fixes: the iterative (explicit-stack) version here,
  or run the work on a `new Thread(null, task, "dfs", 1 << 26)` with a bigger
  stack.
- Iterative DFS visits a node when **popped**, and may push the same node more
  than once → keep the `if (visited[u]) continue;` guard. Visit order differs
  from recursion (siblings reversed); fine when only the *set* of reached nodes
  matters.

**Active recall.**
- Q: Recursive DFS dies with StackOverflow on a 10^5-node line graph — two fixes?
  → A: Switch to the explicit-stack version, or spawn a thread with a larger
  stack size.

**Practice.**
- [Number of Islands](https://leetcode.com/problems/number-of-islands/)
- [Number of Provinces](https://leetcode.com/problems/number-of-provinces/)
- [Flood Fill](https://leetcode.com/problems/flood-fill/)
- [Max Area of Island](https://leetcode.com/problems/max-area-of-island/)
- Deeper (CSES):
  - [Counting Rooms](https://cses.fi/problemset/task/1192)
  - [Building Roads](https://cses.fi/problemset/task/1666)

---

## Topological sort — `TopoSort.java`

**What & when.** Linear order of a **DAG** so every edge points forward
(build/task ordering). Two implementations: Kahn (BFS/indegree) and DFS
post-order reversed.
**Complexity.** O(V + E).
**Edge cases & gotchas.**
- A topo order exists **iff** the graph is a DAG. Kahn naturally detects a cycle:
  if the output has fewer than `n` nodes, a cycle exists (here → empty list).
- DFS version needs the **3-state** coloring to distinguish a back edge (cycle)
  from a cross/forward edge to an already-finished node.
- Order is **not unique**; for the lexicographically smallest order use a
  `PriorityQueue` instead of a plain queue in Kahn.

**Active recall.**
- Q: Kahn vs DFS — when prefer which? → A: Kahn when you also want cycle
  detection "for free" or a lexicographic order (swap in a heap); DFS when you're
  already doing a DFS pass or recursion is natural. Both O(V+E).
- Q: How does Kahn signal a cycle? → A: It can't process all n nodes — output
  size < n.

**Practice.**
- [Course Schedule](https://leetcode.com/problems/course-schedule/)
- [Course Schedule II](https://leetcode.com/problems/course-schedule-ii/)
- [Alien Dictionary](https://leetcode.com/problems/alien-dictionary/)
- [Parallel Courses](https://leetcode.com/problems/parallel-courses/)
- Deeper (CSES):
  - [Course Schedule](https://cses.fi/problemset/task/1679)
  - [Game Routes](https://cses.fi/problemset/task/1681)

---

## Cycle detection — `CycleDetection.java`

**What & when.** Is there a cycle? Directed (3-color DFS) and undirected
(parent-tracking DFS).
**Complexity.** O(V + E).
**Edge cases & gotchas.**
- **Directed** needs 3 states, not a plain `visited[]`: a 2-state check
  false-positives on a DAG "diamond" (0→1→2, 0→2) where 2 is revisited but isn't
  on the stack.
- **Undirected** must skip the *immediate parent*, else the edge you just came
  down looks like a cycle. This breaks with **parallel edges / self-loops** — two
  edges u–v *is* a cycle but the parent-skip hides it. For those, prefer the DSU
  approach: iterate edges, and if `union(u, v)` returns false the endpoints were
  already connected → cycle (`UnionFind.java`).

**Active recall.**
- Q: Why isn't a 2-state `visited[]` enough for directed cycle detection? → A: It
  can't tell a back edge (to a node still on the recursion stack = real cycle)
  from a cross/forward edge to a finished node.
- Q: When does parent-skip undirected detection fail, and the fix? → A: Parallel
  edges / self-loops; use DSU (a `union` that finds them already merged ⇒ cycle).

**Practice.**
- [Course Schedule](https://leetcode.com/problems/course-schedule/)
- [Graph Valid Tree](https://leetcode.com/problems/graph-valid-tree/)
- [Redundant Connection](https://leetcode.com/problems/redundant-connection/)
- [Find Eventual Safe States](https://leetcode.com/problems/find-eventual-safe-states/)
- Deeper (CSES):
  - [Round Trip](https://cses.fi/problemset/task/1669)
  - [Cycle Finding](https://cses.fi/problemset/task/1197)

---

## Union-Find / DSU — `UnionFind.java`

**What & when.** Dynamic connectivity: merge sets, query "same set?", count
components. Backbone of Kruskal and many "connected after adding edges" problems.
**Complexity.** Near O(1) amortized (inverse-Ackermann) per op with path
compression + union by size.
**Edge cases & gotchas.**
- **Both** optimizations matter: without path compression *and* union by
  size/rank a chain degrades to O(n) per `find`.
- `find` here is iterative (path-halving) — no recursion-depth risk.
- `union` returns `false` when already connected — handy for cycle detection and
  "is this edge redundant".
- Need DSU over non-integer labels (strings, coordinates)? Map them to indices
  first.

**Active recall.**
- Q: Drop union-by-size but keep path compression — complexity? → A: Still very
  fast amortized (O(log n) worst single op); but dropping *both* gives O(n).
- Q: How do you use DSU to detect a cycle while adding edges? → A: If
  `union(u,v)` returns false, u and v were already connected ⇒ this edge closes a
  cycle.

**Practice.**
- [Number of Provinces](https://leetcode.com/problems/number-of-provinces/)
- [Redundant Connection](https://leetcode.com/problems/redundant-connection/)
- [Accounts Merge](https://leetcode.com/problems/accounts-merge/)
- [Number of Operations to Make Network Connected](https://leetcode.com/problems/number-of-operations-to-make-network-connected/)
- Deeper (CSES):
  - [Building Roads](https://cses.fi/problemset/task/1666)
  - [Road Construction](https://cses.fi/problemset/task/1676)

---

## Dijkstra — `Dijkstra.java`

**What & when.** Single-source shortest path with **non-negative** weights.
**Complexity.** O((V + E) log V) with a binary heap.
**Edge cases & gotchas.**
- **Negative edges break it** — a node finalized early can later be reached more
  cheaply, but Dijkstra never revisits it. Use Bellman-Ford / SPFA.
- This uses **lazy deletion**: push duplicates, and `if (d > dist[u]) continue;`
  skips stale heap entries (Java's `PriorityQueue` has no decrease-key).
- Use `long` for distances if `sum of weights` can exceed `Integer.MAX_VALUE`.
- **Path reconstruction**: keep `parent[v]` and set it whenever you relax v, then
  walk parents from target back to source and reverse.

**Active recall.**
- Q: Why does Dijkstra fail with negative edges? → A: It greedily *finalizes* the
  popped min and never reopens it; a later negative edge could have lowered it.
- Q: What is the stale-entry check for? → A: No decrease-key, so we push updated
  copies; `d > dist[u]` skips the outdated ones.

**Interviewer follow-up.**
- "Also return the path, not just the cost" → keep `parent[v]`, walk back from
  the target (see the path-reconstruction gotcha above), then reverse.
- "What if there's a cap on the number of edges/stops?" → Dijkstra's greedy
  finalization ignores hop count; use Bellman-Ford relaxed K+1 times
  (`BellmanFord.java`) or DP over `(node, stops)`.

**Practice.**
- [Network Delay Time](https://leetcode.com/problems/network-delay-time/)
- [Path With Minimum Effort](https://leetcode.com/problems/path-with-minimum-effort/)
- [Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/)
- [Swim in Rising Water](https://leetcode.com/problems/swim-in-rising-water/)
- Deeper (CSES):
  - [Shortest Routes I](https://cses.fi/problemset/task/1671)
  - [Flight Discount](https://cses.fi/problemset/task/1195)

---

## 0-1 BFS — `ZeroOneBfs.java`

**What & when.** Shortest path when every edge weight is **0 or 1**. A deque
replaces Dijkstra's heap → O(V + E).
**Complexity.** O(V + E).
**Edge cases & gotchas.**
- Weight-0 relaxations go to the **front**, weight-1 to the **back**; this keeps
  the deque ordered across just two distance layers.
- Only valid for {0,1} weights. {0,1,2,…,k} → use a small "dial"/bucket BFS;
  arbitrary non-negative → Dijkstra.
- A node may be popped more than once — keep the `dist[u] + w < dist[v]` guard.

**Active recall.**
- Q: Why a deque instead of a heap here? → A: With only two possible tentative
  distances at any time (d and d+1), front/back insertion keeps order without the
  log-factor of a heap.

**Practice.**
- [Minimum Obstacle Removal to Reach Corner](https://leetcode.com/problems/minimum-obstacle-removal-to-reach-corner/)
- [Minimum Cost to Make at Least One Valid Path in a Grid](https://leetcode.com/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/)

---

## Bellman-Ford — `BellmanFord.java`

**What & when.** Single-source shortest path that **tolerates negative edges**
and **detects negative cycles**.
**Complexity.** O(V · E).
**Edge cases & gotchas.**
- Relax all edges **V−1** times (longest simple path has ≤ V−1 edges). If any
  edge still relaxes on a **V-th** pass, a negative cycle is reachable.
- Guard with `dist[u] != INF` before relaxing, or `INF + negativeWeight`
  overflows and corrupts `dist`.
- Detects a negative cycle only if it's **reachable from the source**. For "any
  negative cycle anywhere," add a virtual source with 0-edges to all nodes.

**Active recall.**
- Q: Why exactly V−1 relaxation rounds? → A: A shortest simple path uses at most
  V−1 edges; each round fixes at least one more edge of every shortest path.
- Q: How is a negative cycle detected? → A: A successful relaxation on one extra
  (V-th) round.

**Practice.**
- [Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/) (Bellman-Ford with ≤ K+1 rounds)
- [Negative Weight Cycle (GFG)](https://www.geeksforgeeks.org/problems/negative-weight-cycle3504/1)
- Deeper (CSES):
  - [High Score](https://cses.fi/problemset/task/1673)
  - [Cycle Finding](https://cses.fi/problemset/task/1197)

---

## Floyd-Warshall — `FloydWarshall.java`

**What & when.** **All-pairs** shortest paths; works with negative edges (no
negative cycle). Best when n is small.
**Complexity.** O(n³) time, O(n²) space.
**Edge cases & gotchas.**
- The loop order must be **k (intermediate) outermost**, then i, j.
- Use a "big but not overflowing" INF (here `1e9`), so `INF + INF` doesn't wrap;
  the current file initializes direct edges and is **undirected** — drop the
  symmetric assignment for directed graphs.
- A negative cycle shows up as some `dist[i][i] < 0` after the run.

**Active recall.**
- Q: Why must k be the outer loop? → A: `dist[i][j]` using intermediates `{0..k}`
  is built from results that already allow `{0..k−1}`; k outermost guarantees that
  ordering.

**Practice.**
- [Find the City With the Smallest Number of Neighbors at a Threshold Distance](https://leetcode.com/problems/find-the-city-with-the-smallest-number-of-neighbors-at-a-threshold-distance/)
- Deeper (CSES):
  - [Shortest Routes II](https://cses.fi/problemset/task/1672)

---

## MST: Kruskal & Prim — `Mst.java`

**What & when.** Minimum-weight set of edges connecting all nodes. Kruskal =
sort edges + DSU (great for edge lists / sparse). Prim = grow from a node via a
heap (fine for dense / adjacency).
**Complexity.** Kruskal O(E log E); Prim O(E log V) with a heap.
**Edge cases & gotchas.**
- **Disconnected** graph has no spanning tree: Kruskal uses < n−1 edges; Prim
  here returns −1. Always check.
- Kruskal relies on DSU to reject edges that would form a cycle (`union` returns
  false).
- "Connect all points" with Manhattan distance → build the complete edge list
  then Kruskal/Prim (or Prim directly to avoid materializing O(n²) edges).

**Active recall.**
- Q: Kruskal vs Prim — when each? → A: Kruskal when edges are given as a list /
  graph is sparse; Prim when the graph is dense or already an adjacency list.
- Q: How do you know an MST doesn't exist? → A: Graph is disconnected — fewer
  than n−1 edges accepted (Kruskal) / not all nodes reached (Prim).

**Practice.**
- [Min Cost to Connect All Points](https://leetcode.com/problems/min-cost-to-connect-all-points/)
- [Connecting Cities With Minimum Cost](https://leetcode.com/problems/connecting-cities-with-minimum-cost/)
- [Optimize Water Distribution (virtual node trick)](https://leetcode.com/problems/optimize-water-distribution-in-a-village/)
- Deeper (CSES):
  - [Road Reparation](https://cses.fi/problemset/task/1675)

---

## Bipartite check — `Bipartite.java`

**What & when.** Can nodes be 2-colored so no edge joins same colors? Equivalent
to "no odd-length cycle" / "split into two groups."
**Complexity.** O(V + E).
**Edge cases & gotchas.**
- Restart from **every** uncolored node — the graph is bipartite iff **all**
  components are.
- A self-loop makes a graph non-bipartite immediately.
- DFS coloring works too; BFS is used here.

**Active recall.**
- Q: Bipartite is equivalent to the absence of what? → A: Odd-length cycles.

**Practice.**
- [Is Graph Bipartite?](https://leetcode.com/problems/is-graph-bipartite/)
- [Possible Bipartition](https://leetcode.com/problems/possible-bipartition/)
- Deeper (CSES):
  - [Building Teams](https://cses.fi/problemset/task/1668)

---

## Tarjan SCC — `TarjanScc.java`

**What & when.** Strongly connected components of a **directed** graph (every
pair mutually reachable). Components come out in reverse topological order of the
condensation DAG.
**Complexity.** O(V + E).
**Edge cases & gotchas.**
- `low[u]` may only be updated from a back/cross edge to a node **still on the
  stack** (`onStack[v]`) — using `tin[v]` of an already-popped node is wrong.
- An SCC root satisfies `low[u] == tin[u]`; pop the stack down to it.
- Recursive — same large-graph stack caveat as DFS (iterative version or bigger
  thread stack). Alternative: **Kosaraju** (two passes, often easier to recall).
- Single-node SCCs are normal (a node with no cycle is its own SCC).

**Active recall.**
- Q: Why gate the `low` update on `onStack[v]`? → A: An edge to a finished node in
  another (already-output) SCC must not pull this component's low down.
- Q: What does `low[u] == tin[u]` mean? → A: u is the entry/root of its SCC; the
  stack above it (inclusive) is exactly that component.

**Practice.**
- [Critical Connections in a Network](https://leetcode.com/problems/critical-connections-in-a-network/) (bridges, sibling concept)
- [Strongly Connected Components (GFG)](https://www.geeksforgeeks.org/problems/strongly-connected-components-kosarajus-algo/1)
- Deeper (CSES):
  - [Planets and Kingdoms](https://cses.fi/problemset/task/1683)
  - [Flight Routes Check](https://cses.fi/problemset/task/1682)

---

## Bridges & articulation points — `BridgesArticulation.java`

**What & when.** Edges (**bridges**) or nodes (**articulation points**) whose
removal increases the number of connected components, in an **undirected** graph.
The general-graph version of `Tarjan2DGrid.java` (which applies this to a grid).
**Complexity.** O(V + E).
**Edge cases & gotchas.**
- **Bridge** iff `low[v] > tin[u]` (strict). **Articulation** (non-root) iff
  `low[v] >= tin[u]` (non-strict). Don't swap the `>` / `>=`.
- The DFS **root** is special: it's an articulation point iff it has **≥ 2 DFS
  children** (it has no parent edge to lean on).
- Track the **edge you came in on by edge id**, not by parent node — otherwise
  **parallel edges** (two edges u–v) get mis-skipped. A parallel edge means
  *no* bridge between u and v.

**Active recall.**
- Q: Bridge vs articulation condition — what's the one-character difference and
  why? → A: `>` for bridges, `>=` for articulation. When `low[v] == tin[u]`, v's
  subtree can climb back to u (via a back edge) but no higher: removing the *node*
  u still isolates that subtree (articulation), yet the *edge* u–v isn't a bridge
  because the back edge gives an alternate route to u.
- Q: Why id-based parent skip instead of node-based? → A: Parallel edges; skipping
  the parent *node* would ignore a genuine second edge that forms a cycle.

**Practice.**
- [Critical Connections in a Network](https://leetcode.com/problems/critical-connections-in-a-network/) (bridges)
- [Articulation Point (GFG)](https://www.geeksforgeeks.org/problems/articulation-point-1/1)

---

## TODO — future additions

Advanced topics not yet in this folder. Rarely needed for SDE2 OAs, but worth a
later pass (same template + notes treatment):

- **Eulerian path / circuit** (Hierholzer's) — when you must use *every edge* once
  (reconstruct itinerary, valid-arrangement-of-pairs problems).
- **Max-flow / min-cut** (Dinic or Edmonds-Karp) — bipartite matching, "max
  disjoint paths", assignment/capacity problems.
- **LCA / binary lifting** — repeated lowest-common-ancestor or k-th ancestor
  queries on a tree; also distance between two tree nodes.
- **Heavy-light decomposition** — path queries/updates on a tree (overkill for
  most OAs; listed for completeness).

## Cross-cutting reminders

- **Recursion depth**: any recursive DFS (cycle, topo, SCC, bridges) can
  overflow on ~10^5-deep graphs. Convert to iterative or use a big-stack thread.
- **Overflow**: sum-of-weights distances may need `long`; guard `INF + w` in
  Bellman-Ford / Floyd-Warshall.
- **0- vs 1-indexed**: templates here are 0-indexed; many OA inputs are 1-indexed
  — either subtract 1 on input or size arrays `n + 1`.
- **Disconnected graphs**: loop over all start nodes for traversals/components;
  check reachability for MST and shortest paths.
- **Build the adjacency list once** from the edge list; remember to add both
  directions for undirected graphs.
