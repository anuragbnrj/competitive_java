package library.graphs;

import java.util.*;

public class ZeroOneBfs {
    private static final int INF = Integer.MAX_VALUE;

    // ---------------------------------------------------------------
    // Single-source shortest distances where every edge weight is
    // 0 or 1. O(V+E) via a deque: 0-weight relaxations go to the
    // FRONT, 1-weight relaxations go to the BACK - this keeps the
    // deque "sorted by distance" in two layers (d and d+1), giving
    // the same effect as Dijkstra's heap without the log factor.
    // adj.get(u) = {v, weight}.
    //
    // A node can be popped more than once, so re-check dist before
    // relaxing (the `dist[u] + w < dist[v]` guard handles it).
    // ---------------------------------------------------------------
    public static int[] shortestDistances(List<List<int[]>> adj, int n, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        Deque<Integer> deque = new ArrayDeque<>();
        deque.addFirst(src);

        while (!deque.isEmpty()) {
            int u = deque.pollFirst();
            for (int[] edge : adj.get(u)) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    if (w == 0) deque.addFirst(v);
                    else deque.addLast(v);
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        // 0-(1)->1, 0-(0)->2, 2-(1)->1, 1-(0)->3, 2-(1)->3
        int n = 4;
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        adj.get(0).add(new int[]{1, 1});
        adj.get(0).add(new int[]{2, 0});
        adj.get(2).add(new int[]{1, 1});
        adj.get(1).add(new int[]{3, 0});
        adj.get(2).add(new int[]{3, 1});

        System.out.println("Distances from 0: " + Arrays.toString(shortestDistances(adj, n, 0)));
        // Expected: [0, 1, 0, 1]  (0->2 costs 0; 2->1 and 2->3 each cost 1; 1->3 is free)
    }
}
