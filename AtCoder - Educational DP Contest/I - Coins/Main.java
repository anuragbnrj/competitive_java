// Educational DP Contest I - Coins
// 2024-09-21 16:47

// Problem Link:

import java.io.*;
import java.util.*;

public class Main {

    static Reader in;
    static Writer out;

    static int INF = 1_000_000_000;
    static long INFL = (long) 1e18;
    static int MOD = 1_000_000_007;

    public static void solve() {
        int n;
        n = in.nextInt();
        double[] prob = new double[n + 1];
        for (int i = 1; i <= n; i++) {
            prob[i] = in.nextDouble();
        }

        double[][] dp = new double[n + 2][n + 2];
        boolean[][] done = new boolean[n + 2][n + 2];
        double ans = 0.0;
        for (int heads = n; heads > (n - heads); heads--) {
            double temp = solve(n, heads, prob, dp, done);
//            out.println("heads: " + heads + ", tails: " + (n - heads) + ", ans: " + temp);
            ans += temp;
        }

        out.println(ans);
    }

    private static double solve(int idx, int headCount, double[] prob, double[][] dp, boolean[][] done) {
        if (headCount > idx) {
            return 0;
        }
        
        if (idx == 1) {
            if (headCount == 1) {
                return prob[idx];
            } else {
                return (1 - prob[idx]);
            }
        }

//        if (idx <= 0 || headCount <= 0) {
//            return 0;
//        }

        if (done[idx][headCount]) {
            return dp[idx][headCount];
        }

        double ans = 0.0;
        if (headCount == 0) {
            double temp = 1.0;
            for (int i = 1; i <= idx; i++) {
                temp *= (1 - prob[i]);
            }

            ans = temp;
        } else {
            // current head
            ans += solve(idx - 1, headCount - 1, prob, dp, done) * prob[idx];
            // current tails
            ans += solve(idx - 1, headCount, prob, dp, done) * (1 - prob[idx]);
        }

        dp[idx][headCount] = ans;
        done[idx][headCount] = true;

        return ans;
    }

    public static void main(String[] args) {
        // Start writing your solution here. -------------------------------------
        in = new Reader();
        out = new Writer();

        int t = 1;
        // t = in.nextInt();
        while (t-- > 0) {
            solve();
        }

        /*
         * int n = in.nextInt(); // read input as integer
         * long k = in.nextLong(); // read input as long
         * double d = in.nextDouble(); // read input as double
         * String str = in.next(); // read input as String
         * String s = in.nextLine(); // read whole line as String
         *
         * out.println("Hello"); // print via PrintWriter
         */

        // Stop writing your solution here. -------------------------------------
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

        public void yesNo(boolean condition) {
            println(condition ? "YES" : "NO");
        }

        public void printArray(int[] a) {
            for (int j : a) print(j + " ");
        }

        public void printlnArray(int[] a) {
            for (int j : a) print(j + " ");
            pw.println();
        }

        public void printArray(long[] a) {
            for (long l : a) print(l + " ");
        }

        public void printlnArray(long[] a) {
            for (long l : a) print(l + " ");
            pw.println();
        }

        public void print(Object o) {
            pw.print(o.toString());
        }

        public void println(Object o) {
            pw.println(o.toString());
        }

        public void println() {
            pw.println();
        }

        public void flush() {
            pw.flush();
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

        public Reader(String f) {
            try {
                br = new BufferedReader(new FileReader(f));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int[] inpIntArr(int n) {
            int[] a = new int[n];
            for (int i = 0; i < n; i++) a[i] = nextInt();
            return a;
        }

        public double[] inpDouArr(int n) {
            double[] a = new double[n];
            for (int i = 0; i < n; i++) a[i] = nextDouble();
            return a;
        }

        public long[] inpLonArr(int n) {
            long[] a = new long[n];
            for (int i = 0; i < n; i++) a[i] = nextLong();
            return a;
        }

        public char[] inpChaArr() {
            return next().toCharArray();
        }

        public String[] inpStrArr(int n) {
            String[] a = new String[n];
            for (int i = 0; i < n; i++) a[i] = next();
            return a;
        }

        public int nextInt() {
            ensureNext();
            return Integer.parseInt(st.nextToken());
        }

        public double nextDouble() {
            ensureNext();
            return Double.parseDouble(st.nextToken());
        }

        public Long nextLong() {
            ensureNext();
            return Long.parseLong(st.nextToken());
        }

        public String next() {
            ensureNext();
            return st.nextToken();
        }

        public String nextLine() {
            try {
                return br.readLine();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
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

/*
1. For cyclic shifts append original string / arr 2 times. eg: String s = abc, String duplicate = abcabc
 */
