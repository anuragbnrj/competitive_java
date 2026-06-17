package library.graphs;

import java.util.*;

public class Bfs {

    // ---------------------------------------------------------------
    // Multi-source BFS on a 2D grid (e.g. "Rotting Oranges", "01
    // Matrix" - distance from nearest 0). Every cell in `sources`
    // starts at distance 0; everything else expands outward in BFS
    // layers. Unreachable cells stay -1.
    //
    // Mark a cell visited (set dist) WHEN ENQUEUED, not when dequeued
    // - otherwise the same cell can be pushed many times.
    // ---------------------------------------------------------------
    public static int[][] multiSourceGrid(int rows, int cols, List<int[]> sources, boolean[][] blocked) {
        int[][] dist = new int[rows][cols];
        for (int[] row : dist) Arrays.fill(row, -1);

        Queue<int[]> queue = new ArrayDeque<>();
        for (int[] s : sources) {
            dist[s[0]][s[1]] = 0;
            queue.add(s);
        }

        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            for (int d = 0; d < 4; d++) {
                int nr = cur[0] + dr[d];
                int nc = cur[1] + dc[d];
                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
                if (blocked[nr][nc] || dist[nr][nc] != -1) continue;

                dist[nr][nc] = dist[cur[0]][cur[1]] + 1;
                queue.add(new int[]{nr, nc});
            }
        }

        return dist;
    }

    // ---------------------------------------------------------------
    // BFS shortest path (edge count) on an unweighted adjacency list,
    // from a single source. dist[v] == -1 means v is unreachable.
    // ---------------------------------------------------------------
    public static int[] shortestPath(List<List<Integer>> adj, int n, int src) {
        int[] dist = new int[n];
        Arrays.fill(dist, -1);
        dist[src] = 0;

        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(src);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (int v : adj.get(u)) {
                if (dist[v] == -1) {
                    dist[v] = dist[u] + 1;
                    queue.add(v);
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        // 3x3 grid, source at (0,0), wall in the center
        int rows = 3, cols = 3;
        boolean[][] blocked = new boolean[rows][cols];
        blocked[1][1] = true;
        List<int[]> sources = List.of(new int[]{0, 0});

        int[][] dist = multiSourceGrid(rows, cols, sources, blocked);
        System.out.println("Grid distances from (0,0), center blocked:");
        for (int[] row : dist) System.out.println(Arrays.toString(row));
        // Expected: [0, 1, 2] / [1, -1, 3] / [2, 3, 4]

        // Adjacency list: 0-1-2-3 chain plus a 0-3 shortcut edge
        int n = 4;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
        int[][] edges = {{0, 1}, {1, 2}, {2, 3}, {0, 3}};
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
            adj.get(e[1]).add(e[0]);
        }

        System.out.println("BFS distances from node 0: " + Arrays.toString(shortestPath(adj, n, 0)));
        // Expected: [0, 1, 2, 1]
    }
}
