package library.graphs;

import java.util.*;

public class TopoSort {

    // ---------------------------------------------------------------
    // Kahn's algorithm (BFS on indegrees). Returns an empty list if
    // the graph has a cycle (fewer than n nodes get processed).
    // ---------------------------------------------------------------
    public static List<Integer> kahn(List<List<Integer>> adj, int n) {
        int[] indegree = new int[n];
        for (int u = 0; u < n; u++) {
            for (int v : adj.get(u)) indegree[v]++;
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) queue.add(i);
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                if (--indegree[v] == 0) queue.add(v);
            }
        }

        return order.size() == n ? order : new ArrayList<>(); // empty => cycle
    }

    // ---------------------------------------------------------------
    // DFS-based topological sort: a node is appended only after all
    // its descendants are processed, then the result is reversed.
    // Uses a 3-state array (0=unvisited, 1=on stack, 2=done) so a
    // back edge to an in-progress node (cycle) can be told apart from
    // a cross edge to an already-finished node. Returns an empty list
    // if a cycle is detected.
    // ---------------------------------------------------------------
    public static List<Integer> dfsTopoSort(List<List<Integer>> adj, int n) {
        int[] state = new int[n];
        Deque<Integer> order = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (state[i] == 0 && !dfs(adj, i, state, order)) {
                return new ArrayList<>(); // cycle
            }
        }

        return new ArrayList<>(order);
    }

    private static boolean dfs(List<List<Integer>> adj, int u, int[] state, Deque<Integer> order) {
        state[u] = 1;
        for (int v : adj.get(u)) {
            if (state[v] == 1) return false; // back edge -> cycle
            if (state[v] == 0 && !dfs(adj, v, state, order)) return false;
        }
        state[u] = 2;
        order.push(u); // push to front -> reversed post-order
        return true;
    }

    public static void main(String[] args) {
        // DAG: 5->0, 5->2, 4->0, 4->1, 2->3, 3->1
        int n = 6;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[][] edges = {{5, 0}, {5, 2}, {4, 0}, {4, 1}, {2, 3}, {3, 1}};
        for (int[] e : edges) adj.get(e[0]).add(e[1]);

        System.out.println("Kahn order: " + kahn(adj, n));     // a valid topo order
        System.out.println("DFS order: " + dfsTopoSort(adj, n)); // a valid topo order

        // Cyclic graph: 0->1->2->0
        List<List<Integer>> cyclic = new ArrayList<>();
        for (int i = 0; i < 3; i++) cyclic.add(new ArrayList<>());
        cyclic.get(0).add(1);
        cyclic.get(1).add(2);
        cyclic.get(2).add(0);

        System.out.println("Kahn on cycle (expect []): " + kahn(cyclic, 3));
        System.out.println("DFS topo on cycle (expect []): " + dfsTopoSort(cyclic, 3));
    }
}
