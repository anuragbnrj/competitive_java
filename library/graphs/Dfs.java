package library.graphs;

import java.util.*;

public class Dfs {

    // ---------------------------------------------------------------
    // Recursive DFS from `src`. Appends visited nodes in visit order
    // to `order`. Caller provides a fresh `visited[]`.
    //
    // NOTE: on a "stringy" graph (e.g. a chain of 10^5 nodes) this can
    // StackOverflowError - see NOTES.md for the workaround.
    // ---------------------------------------------------------------
    public static void dfsRecursive(List<List<Integer>> adj, int src, boolean[] visited, List<Integer> order) {
        visited[src] = true;
        order.add(src);
        for (int v : adj.get(src)) {
            if (!visited[v]) {
                dfsRecursive(adj, v, visited, order);
            }
        }
    }

    // ---------------------------------------------------------------
    // Iterative DFS using an explicit stack. Visits the same nodes as
    // the recursive version but NOT necessarily in the same order
    // (sibling order is reversed by the stack). The `if (visited[u])
    // continue;` check handles nodes pushed more than once.
    // ---------------------------------------------------------------
    public static List<Integer> dfsIterative(List<List<Integer>> adj, int src, boolean[] visited) {
        List<Integer> order = new ArrayList<>();
        Deque<Integer> stack = new ArrayDeque<>();
        stack.push(src);

        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (visited[u]) continue;
            visited[u] = true;
            order.add(u);

            for (int v : adj.get(u)) {
                if (!visited[v]) stack.push(v);
            }
        }

        return order;
    }

    // ---------------------------------------------------------------
    // Count connected components in an undirected graph.
    // ---------------------------------------------------------------
    public static int countComponents(List<List<Integer>> adj, int n) {
        boolean[] visited = new boolean[n];
        int components = 0;
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                components++;
                dfsRecursive(adj, i, visited, new ArrayList<>());
            }
        }
        return components;
    }

    public static void main(String[] args) {
        // Two components: {0,1,2} (path) and {3,4}
        int n = 5;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[][] edges = {{0, 1}, {1, 2}, {3, 4}};
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        List<Integer> order = new ArrayList<>();
        dfsRecursive(adj, 0, new boolean[n], order);
        System.out.println("Recursive DFS from 0: " + order); // [0, 1, 2]

        System.out.println("Iterative DFS from 0: " + dfsIterative(adj, 0, new boolean[n])); // [0, 1, 2] (or [0, 2, 1])

        System.out.println("Connected components (expect 2): " + countComponents(adj, n));
    }
}
