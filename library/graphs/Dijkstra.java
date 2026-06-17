package library.graphs;

import java.util.*;

public class Dijkstra {
    private static final int INF = Integer.MAX_VALUE;

    // ---------------------------------------------------------------
    // Single-source shortest distances on a graph with NON-NEGATIVE
    // edge weights. adj.get(u) is a list of {v, weight} pairs.
    // dist[v] == INF means v is unreachable.
    //
    // For path reconstruction, see NOTES.md (one extra parent[] array).
    // ---------------------------------------------------------------
    public static int[] shortestDistances(List<List<int[]>> adj, int n, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        // {distance, node}
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.add(new int[]{0, src});

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int d = top[0], u = top[1];
            if (d > dist[u]) continue; // stale entry (lazy deletion)

            for (int[] edge : adj.get(u)) {
                int v = edge[0], w = edge[1];
                if (dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    pq.add(new int[]{dist[v], v});
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        // 0-(4)->1, 0-(1)->2, 2-(2)->1, 1-(1)->3, 2-(5)->3
        int n = 4;
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        adj.get(0).add(new int[]{1, 4});
        adj.get(0).add(new int[]{2, 1});
        adj.get(2).add(new int[]{1, 2});
        adj.get(1).add(new int[]{3, 1});
        adj.get(2).add(new int[]{3, 5});

        System.out.println("Distances from 0: " + Arrays.toString(shortestDistances(adj, n, 0)));
        // Expected: [0, 3, 1, 4]  (0->2->1->3 = 1+2+1 = 4)
    }
}
