package library.graphs;

public class FloydWarshall {
    int INF = 1_000_000_000;

    // All-pairs shortest paths. Builds an UNDIRECTED graph (line 22's
    // symmetric assignment) - drop it for a directed graph. Valid for
    // non-negative weights here; a negative edge in an undirected
    // graph is itself a negative cycle. INF = 1e9 is overflow-safe:
    // the worst relaxation sum 2e9 stays under Integer.MAX_VALUE.
    public int[][] findAPSP(int n, int[][] edges) {
        int[][] dist = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    dist[i][j] = INF;
                }
            }
        }

        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            int wt = edge[2];

            dist[u][v] = Math.min(dist[u][v], wt);
            dist[v][u] = Math.min(dist[v][u], wt);
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }

        return dist;
    }

    public static void main(String[] args) {
        // Undirected: 0-1(3), 1-2(1), 0-2(5), 2-3(2)
        int n = 4;
        int[][] edges = {{0, 1, 3}, {1, 2, 1}, {0, 2, 5}, {2, 3, 2}};

        int[][] dist = new FloydWarshall().findAPSP(n, edges);
        System.out.println("All-pairs shortest paths:");
        for (int[] row : dist) System.out.println(java.util.Arrays.toString(row));

        // 0->3 best is 0->1->2->3 = 3+1+2 = 6 (not 0->2->3 = 5+2 = 7)
        System.out.println("dist[0][3] (expect 6): " + dist[0][3]);
        System.out.println("dist[0][2] (expect 4, via 0->1->2): " + dist[0][2]);
    }
}
