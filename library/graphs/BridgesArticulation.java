package library.graphs;

import java.util.*;

public class BridgesArticulation {
    private static int[] tin, low;
    private static boolean[] visited, isArticulation;
    private static int timer;
    private static List<int[]> bridges;

    public static class Result {
        public final List<int[]> bridges;
        public final List<Integer> articulationPoints;

        public Result(List<int[]> bridges, List<Integer> articulationPoints) {
            this.bridges = bridges;
            this.articulationPoints = articulationPoints;
        }
    }

    // ---------------------------------------------------------------
    // Finds bridges (edges whose removal disconnects the graph) and
    // articulation points (nodes whose removal disconnects the graph)
    // in an undirected graph, via a single DFS with discovery times
    // (tin) and low-link values (low). adj.get(u) = {v, edgeId}.
    //
    //   - Tree edge (u -> v) is a BRIDGE iff  low[v] > tin[u] (strict).
    //   - Non-root u is an ARTICULATION POINT iff some tree child v
    //     has  low[v] >= tin[u]  (non-strict).
    //   - The DFS root is an articulation point iff it has >= 2
    //     children in the DFS tree (separate check - it has no parent
    //     to compare against).
    //
    // Edges are identified by an index (`edgeId`), not by the parent
    // NODE, so PARALLEL EDGES between the same pair are handled
    // correctly: skipping "the parent node" would wrongly ignore a
    // second edge back to the parent (which forms a real cycle).
    // ---------------------------------------------------------------
    public static Result findBridgesAndArticulationPoints(List<List<int[]>> adj, int n) {
        tin = new int[n];
        low = new int[n];
        visited = new boolean[n];
        isArticulation = new boolean[n];
        timer = 0;
        bridges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) dfs(adj, i, -1);
        }

        List<Integer> articulationPoints = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (isArticulation[i]) articulationPoints.add(i);
        }

        return new Result(bridges, articulationPoints);
    }

    private static void dfs(List<List<int[]>> adj, int u, int parentEdgeId) {
        visited[u] = true;
        tin[u] = low[u] = timer++;
        int children = 0;

        for (int[] edge : adj.get(u)) {
            int v = edge[0], edgeId = edge[1];
            if (edgeId == parentEdgeId) continue; // skip the edge we arrived on

            if (visited[v]) {
                low[u] = Math.min(low[u], tin[v]);
            } else {
                children++;
                dfs(adj, v, edgeId);
                low[u] = Math.min(low[u], low[v]);

                if (low[v] > tin[u]) {
                    bridges.add(new int[]{u, v});
                }
                if (parentEdgeId != -1 && low[v] >= tin[u]) {
                    isArticulation[u] = true;
                }
            }
        }

        if (parentEdgeId == -1 && children >= 2) {
            isArticulation[u] = true; // root special case
        }
    }

    public static void main(String[] args) {
        // Two triangles {0,1,2} and {2,3,4} sharing node 2, plus a
        // pendant edge 2-5: 0-1, 1-2, 2-0, 2-3, 3-4, 4-2, 2-5
        int n = 6;
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}, {2, 3}, {3, 4}, {4, 2}, {2, 5}};

        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        for (int id = 0; id < edges.length; id++) {
            int u = edges[id][0], v = edges[id][1];
            adj.get(u).add(new int[]{v, id});
            adj.get(v).add(new int[]{u, id});
        }

        Result result = findBridgesAndArticulationPoints(adj, n);

        System.out.print("Bridges (expect [2, 5]): ");
        for (int[] bridge : result.bridges) System.out.print(Arrays.toString(bridge) + " ");
        System.out.println();

        System.out.println("Articulation points (expect [2]): " + result.articulationPoints);
    }
}
