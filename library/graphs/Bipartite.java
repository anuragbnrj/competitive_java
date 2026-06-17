package library.graphs;

import java.util.*;

public class Bipartite {

    // ---------------------------------------------------------------
    // 2-colors the graph via BFS. Returns false if any edge connects
    // two nodes of the same color (not bipartite). Restarts from
    // every uncolored node, so disconnected graphs are handled - the
    // whole graph is bipartite iff every component is.
    // ---------------------------------------------------------------
    public static boolean isBipartite(List<List<Integer>> adj, int n) {
        int[] color = new int[n];
        Arrays.fill(color, -1);

        for (int start = 0; start < n; start++) {
            if (color[start] != -1) continue;

            color[start] = 0;
            Queue<Integer> queue = new ArrayDeque<>();
            queue.add(start);

            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (int v : adj.get(u)) {
                    if (color[v] == -1) {
                        color[v] = 1 - color[u];
                        queue.add(v);
                    } else if (color[v] == color[u]) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        // Bipartite: 4-cycle 0-1-2-3-0
        int n = 4;
        List<List<Integer>> evenCycle = new ArrayList<>();
        for (int i = 0; i < n; i++) evenCycle.add(new ArrayList<>());
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {3, 0}};
        for (int[] e : edges) {
            evenCycle.get(e[0]).add(e[1]);
            evenCycle.get(e[1]).add(e[0]);
        }
        System.out.println("4-cycle is bipartite (expect true): " + isBipartite(evenCycle, n));

        // Not bipartite: triangle 0-1-2-0
        List<List<Integer>> triangle = new ArrayList<>();
        for (int i = 0; i < 3; i++) triangle.add(new ArrayList<>());
        int[][] triEdges = {{0, 1}, {1, 2}, {2, 0}};
        for (int[] e : triEdges) {
            triangle.get(e[0]).add(e[1]);
            triangle.get(e[1]).add(e[0]);
        }
        System.out.println("Triangle is bipartite (expect false): " + isBipartite(triangle, 3));
    }
}
