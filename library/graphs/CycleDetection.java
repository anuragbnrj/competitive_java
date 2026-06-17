package library.graphs;

import java.util.*;

public class CycleDetection {

    // ---------------------------------------------------------------
    // Directed graph: 3-color DFS. state: 0 = unvisited, 1 = on the
    // current DFS stack, 2 = fully processed. A back edge to a state
    // == 1 node means a cycle. (A 2-state visited[] would give false
    // positives on DAG "diamonds" - see NOTES.md.)
    // ---------------------------------------------------------------
    public static boolean hasCycleDirected(List<List<Integer>> adj, int n) {
        int[] state = new int[n];
        for (int i = 0; i < n; i++) {
            if (state[i] == 0 && dfsDirected(adj, i, state)) return true;
        }
        return false;
    }

    private static boolean dfsDirected(List<List<Integer>> adj, int u, int[] state) {
        state[u] = 1;
        for (int v : adj.get(u)) {
            if (state[v] == 1) return true;
            if (state[v] == 0 && dfsDirected(adj, v, state)) return true;
        }
        state[u] = 2;
        return false;
    }

    // ---------------------------------------------------------------
    // Undirected graph: DFS tracking the parent node. Any visited
    // neighbor that isn't the parent means a cycle. Handles
    // disconnected graphs by trying every node as a start.
    //
    // CAVEAT: this "skip the parent" check is wrong for graphs with
    // parallel edges or self-loops - use the DSU approach (NOTES.md)
    // when those are possible.
    // ---------------------------------------------------------------
    public static boolean hasCycleUndirected(List<List<Integer>> adj, int n) {
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            if (!visited[i] && dfsUndirected(adj, i, -1, visited)) return true;
        }
        return false;
    }

    private static boolean dfsUndirected(List<List<Integer>> adj, int u, int parent, boolean[] visited) {
        visited[u] = true;
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                if (dfsUndirected(adj, v, u, visited)) return true;
            } else if (v != parent) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        // Directed cycle: 0->1->2->0, plus an extra edge 3->1
        int n = 4;
        List<List<Integer>> directed = new ArrayList<>();
        for (int i = 0; i < n; i++) directed.add(new ArrayList<>());
        directed.get(0).add(1);
        directed.get(1).add(2);
        directed.get(2).add(0);
        directed.get(3).add(1);
        System.out.println("Directed has cycle (expect true): " + hasCycleDirected(directed, n));

        // Directed DAG "diamond": 0->1->2, 0->2
        List<List<Integer>> dag = new ArrayList<>();
        for (int i = 0; i < 3; i++) dag.add(new ArrayList<>());
        dag.get(0).add(1);
        dag.get(1).add(2);
        dag.get(0).add(2);
        System.out.println("DAG diamond has cycle (expect false): " + hasCycleDirected(dag, 3));

        // Undirected triangle: 0-1-2-0
        List<List<Integer>> undirected = new ArrayList<>();
        for (int i = 0; i < 3; i++) undirected.add(new ArrayList<>());
        int[][] edges = {{0, 1}, {1, 2}, {2, 0}};
        for (int[] e : edges) {
            undirected.get(e[0]).add(e[1]);
            undirected.get(e[1]).add(e[0]);
        }
        System.out.println("Undirected triangle has cycle (expect true): " + hasCycleUndirected(undirected, 3));

        // Undirected tree: 0-1, 1-2 (no cycle)
        List<List<Integer>> tree = new ArrayList<>();
        for (int i = 0; i < 3; i++) tree.add(new ArrayList<>());
        tree.get(0).add(1); tree.get(1).add(0);
        tree.get(1).add(2); tree.get(2).add(1);
        System.out.println("Undirected tree has cycle (expect false): " + hasCycleUndirected(tree, 3));
    }
}
