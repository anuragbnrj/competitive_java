package library.graphs;

import java.util.*;

public class Mst {

    // ---------------------------------------------------------------
    // Kruskal: sort edges by weight, add via DSU if endpoints are in
    // different components. edges[i] = {u, v, weight}. Reuses
    // UnionFind from this package.
    // Returns {totalWeight, edgesUsed}; edgesUsed < n-1 means the
    // graph is disconnected (no spanning tree exists).
    // ---------------------------------------------------------------
    public static int[] kruskal(int n, int[][] edges) {
        int[][] sorted = edges.clone();
        Arrays.sort(sorted, (a, b) -> a[2] - b[2]);

        UnionFind uf = new UnionFind(n);
        int totalWeight = 0, edgesUsed = 0;

        for (int[] e : sorted) {
            if (uf.union(e[0], e[1])) {
                totalWeight += e[2];
                edgesUsed++;
            }
        }

        return new int[]{totalWeight, edgesUsed};
    }

    // ---------------------------------------------------------------
    // Prim: grow a tree from node 0 using a min-heap of frontier
    // edges. adj.get(u) = {v, weight}. Returns -1 if the graph is
    // disconnected (fewer than n nodes reached from node 0).
    // ---------------------------------------------------------------
    public static int prim(List<List<int[]>> adj, int n) {
        boolean[] visited = new boolean[n];
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]); // {weight, node}
        pq.add(new int[]{0, 0});

        int totalWeight = 0, visitedCount = 0;
        while (!pq.isEmpty() && visitedCount < n) {
            int[] top = pq.poll();
            int w = top[0], u = top[1];
            if (visited[u]) continue;

            visited[u] = true;
            totalWeight += w;
            visitedCount++;

            for (int[] edge : adj.get(u)) {
                if (!visited[edge[0]]) {
                    pq.add(new int[]{edge[1], edge[0]});
                }
            }
        }

        return visitedCount == n ? totalWeight : -1;
    }

    public static void main(String[] args) {
        // Square with a diagonal: 0-1(1), 1-2(2), 2-3(3), 3-0(4), 0-2(5)
        int n = 4;
        int[][] edges = {{0, 1, 1}, {1, 2, 2}, {2, 3, 3}, {3, 0, 4}, {0, 2, 5}};

        int[] kruskalResult = kruskal(n, edges);
        System.out.println("Kruskal weight (expect 6), edges used (expect 3): "
                + kruskalResult[0] + ", " + kruskalResult[1]);

        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(new int[]{e[1], e[2]});
            adj.get(e[1]).add(new int[]{e[0], e[2]});
        }
        System.out.println("Prim weight (expect 6): " + prim(adj, n));
    }
}
