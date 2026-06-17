package library.graphs;

import java.util.*;

public class BellmanFord {
    private static final int INF = Integer.MAX_VALUE;

    // ---------------------------------------------------------------
    // Single-source shortest distances, allows negative edge weights.
    // edges[i] = {u, v, weight} (directed). Relaxes all edges (n-1)
    // times; a successful relaxation on one extra pass means a
    // negative cycle is reachable from src (sets hasNegativeCycle[0]).
    // When that flag is true, dist[] is not meaningful for nodes
    // reachable from the cycle.
    //
    // The `dist[u] != INF` guard avoids INF + (negative) overflowing
    // and wrongly relaxing from an unreached node.
    // ---------------------------------------------------------------
    public static int[] shortestDistances(int n, int[][] edges, int src, boolean[] hasNegativeCycle) {
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                }
            }
        }

        hasNegativeCycle[0] = false;
        for (int[] e : edges) {
            int u = e[0], v = e[1], w = e[2];
            if (dist[u] != INF && dist[u] + w < dist[v]) {
                hasNegativeCycle[0] = true;
                break;
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        // 0-(4)->1, 0-(5)->2, 1-(-2)->2, 2-(1)->3, 1-(3)->3
        int n = 4;
        int[][] edges = {{0, 1, 4}, {0, 2, 5}, {1, 2, -2}, {2, 3, 1}, {1, 3, 3}};

        boolean[] hasNegCycle = new boolean[1];
        int[] dist = shortestDistances(n, edges, 0, hasNegCycle);
        System.out.println("Distances from 0: " + Arrays.toString(dist) + ", negative cycle: " + hasNegCycle[0]);
        // Expected: [0, 4, 2, 3], negative cycle: false

        // Negative cycle: 0-(1)->1, 1-(-3)->0
        int[][] negCycleEdges = {{0, 1, 1}, {1, 0, -3}};
        shortestDistances(2, negCycleEdges, 0, hasNegCycle);
        System.out.println("Negative cycle detected (expect true): " + hasNegCycle[0]);
    }
}
