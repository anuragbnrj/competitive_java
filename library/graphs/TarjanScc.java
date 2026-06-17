package library.graphs;

import java.util.*;

public class TarjanScc {
    private static int[] tin, low;
    private static boolean[] onStack;
    private static int timer;
    private static Deque<Integer> stack;
    private static List<List<Integer>> components;

    // ---------------------------------------------------------------
    // Strongly connected components of a directed graph, each
    // returned as a list of node ids. Components are emitted in
    // reverse topological order of the condensation DAG (a
    // component's edges only point to components emitted earlier).
    //
    // NOTE: recursive - see NOTES.md for the stack-overflow caveat on
    // large/stringy graphs.
    // ---------------------------------------------------------------
    public static List<List<Integer>> findSccs(List<List<Integer>> adj, int n) {
        tin = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        Arrays.fill(tin, -1);
        timer = 0;
        stack = new ArrayDeque<>();
        components = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (tin[i] == -1) dfs(adj, i);
        }

        return components;
    }

    private static void dfs(List<List<Integer>> adj, int u) {
        tin[u] = low[u] = timer++;
        stack.push(u);
        onStack[u] = true;

        for (int v : adj.get(u)) {
            if (tin[v] == -1) {
                dfs(adj, v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], tin[v]);
            }
        }

        // low[u] == tin[u] means u is the root of its SCC - pop the
        // stack until (and including) u to collect that component.
        if (low[u] == tin[u]) {
            List<Integer> component = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                component.add(v);
            } while (v != u);
            components.add(component);
        }
    }

    public static void main(String[] args) {
        // Two SCCs: {0,1,2} (cycle) and {3} (sink), plus edge 0->3
        int n = 4;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        adj.get(0).add(1);
        adj.get(1).add(2);
        adj.get(2).add(0);
        adj.get(0).add(3);

        System.out.println("SCCs: " + findSccs(adj, n));
        // Expected: [[3], [2, 1, 0]] - {3} emitted before {0,1,2} (order within may vary)
    }
}
