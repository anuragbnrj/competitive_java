import java.io.*;
import java.util.*;

public class Main {

    static Reader in;
    static Writer out;

    static int INF = 1_000_000_000;
    static int MOD = 1_000_000_007;

    static int n, m, k;
    static int[][] score;
    static int[][][] dp;

    private static boolean isValid(int r, int c) {
        return r >= 0 && r < n && c >= 0 && c < m;
    }

    private static int rec(int x, int y, int z) {
        if (x == 0) {
            if (score[x][y] % k == z) {
                return score[x][y];
            } else {
                return -1;
            }
        }

        if (dp[x][y][z] != -1) {
            return dp[x][y][z];
        }

        int ans = -1;

        int nr = x - 1;
        int rem = (z - score[x][y] % k + k) % k;

        // Moving to the left cell
        int nc = y - 1;
        if (isValid(nr, nc)) {
            int temp = rec(nr, nc, rem);
            if (temp != -1) {
                ans = Math.max(ans, score[x][y] + temp);
            }
        }

        // Moving to the right cell
        nc = y + 1;
        if (isValid(nr, nc)) {
            int temp = rec(nr, nc, rem);
            if (temp != -1) {
                ans = Math.max(ans, score[x][y] + temp);
            }
        }

        dp[x][y][z] = ans;
        return dp[x][y][z];
    }

    public static void solve() {
        n = in.nextInt();
        m = in.nextInt();
        k = in.nextInt();

        score = new int[n][m];
        for (int r = 0; r < n; r++) {
            String s = in.next();
            for (int c = 0; c < m; c++) {
                score[r][c] = s.charAt(c) - '0';
            }
        }

        dp = new int[n][m][k];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < m; y++) {
                Arrays.fill(dp[x][y], -1);
            }
        }

        int ans = -1;
        for (int y = 0; y < m; y++) {
            ans = Math.max(ans, rec(n - 1, y, 0));
        }

        out.println(ans);
    }

    public static void main(String[] args) {
        in = new Reader();
        out = new Writer();

        int t = in.nextInt();
        while (t-- > 0) {
            solve();
        }

        out.exit();
    }

    // -----------PrintWriter for faster output---------------------------------
    static class Writer {

        private PrintWriter pw;

        public Writer() {
            pw = new PrintWriter(System.out);
        }

        public Writer(String f) {
            try {
                pw = new PrintWriter(new FileWriter(f));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void println(Object o) {
            pw.println(o.toString());
        }

        public void exit() {
            pw.close();
        }
    }

    // -----------MyScanner class for faster input----------
    static class Reader {

        private BufferedReader br;
        private StringTokenizer st;

        public Reader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        public int nextInt() {
            ensureNext();
            return Integer.parseInt(st.nextToken());
        }

        public String next() {
            ensureNext();
            return st.nextToken();
        }

        private void ensureNext() {
            if (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // --------------------------------------------------------
}
