// Problem Link: https://leetcode.com/problems/minimum-number-of-days-to-disconnect-island/

package library.graphs;

public class Tarjan2DGrid {

    public int minDays(int[][] grid) {
        int ans = ArticulationPoints.articulationPoints(grid);

        return ans;
    }

    class ArticulationPoints {
        private static int time;
        private static int[][] tin, low;
        private static boolean[][] visited;
        private static int INF = (int) 1e9;
        private static int rows, cols;

        public static int articulationPoints(int[][] graph) {
            rows = graph.length;
            cols = graph[0].length;

            time = -1;
            tin = new int[rows][cols];
            low = new int[rows][cols];
            visited = new boolean[rows][cols];

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    low[r][c] = INF;
                }
            }

            int[][] extraComps = new int[rows][cols];
            int initialComponents = 0;
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (!visited[r][c] && graph[r][c] == 1) {
                        initialComponents += 1;
                        apUtil(graph, extraComps, r, c, -1, -1);
                    }
                }
            }

            if (initialComponents > 1 || initialComponents == 0) {
                return 0;
            }

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (extraComps[r][c] >= 1 || (initialComponents + extraComps[r][c]) == 0) {
                        return 1;
                    }
                }
            }

            return 2;
        }

        private static void apUtil(int[][] g, int[][] extraComps, int r, int c, int pr, int pc) {
            visited[r][c] = true;
            tin[r][c] = low[r][c] = ++time;

            int childCount = 0;
            int dr[] = {-1, 0, 0, 1};
            int dc[] = {0, -1, 1, 0};

            int midNodeExtraComps = 0;
            for (int i = 0; i < 4; i++) {
                int nr = r + dr[i];
                int nc = c + dc[i];

                if (!isValid(nr, nc) || g[nr][nc] == 0) continue;
                if (nr == pr && nc == pc) continue;

                if (visited[nr][nc]) {
                    low[r][c] = Math.min(low[r][c], tin[nr][nc]);
                } else {
                    apUtil(g, extraComps, nr, nc, r, c);

                    low[r][c] = Math.min(low[r][c], low[nr][nc]);
                    childCount += 1;

                    if (pr != -1 && pc != -1 && low[nr][nc] >= tin[r][c]) {
                        midNodeExtraComps += 1;
                    }
                }
            }

            if (pr == -1 && pc == -1) {
                extraComps[r][c] = childCount - 1;
            } else {
                extraComps[r][c] = midNodeExtraComps;
            }
        }

        private static boolean isValid(int nr, int nc) {
            if (nr < 0 || rows <= nr || nc < 0 || cols <= nc) {
                return false;
            }

            return true;
        }

    }

}
