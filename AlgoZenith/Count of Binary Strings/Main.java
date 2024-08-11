//  Count of Binary Strings
// 2024-07-22 21:51

import java.io.*;
import java.util.*;

public class Main {

    static Reader in;
    static Writer out;

    static int n;
    static int[][] dp = new int[1000001][5];
    static int MOD = 1_000_000_007;

//    private static long rec(int idx, int seen) {
//        //    System.out.println("idx: " + idx + ", seen: " + seen);
//
//        if (seen == 4) {
//            return 0;
//        }
//
//        if (idx == n) {
//            return 1;
//        }
//
//        if (dp[idx][seen] != -1) {
//            return dp[idx][seen];
//        }
//
//        long ans = 0;
//        if (seen == 0) {
//            ans = (ans + rec(idx + 1, 1)) % MOD;
//            ans = (ans + rec(idx + 1, 0)) % MOD;
//        } else if (seen == 1) {
//            ans = (ans + rec(idx + 1, 1)) % MOD;
//            ans = (ans + rec(idx + 1, 2)) % MOD;
//        } else if (seen == 2) {
//            ans = (ans + rec(idx + 1, 3)) % MOD;
//            ans = (ans + rec(idx + 1, 0)) % MOD;
//        } else if (seen == 3) {
//            ans = (ans + rec(idx + 1, 2)) % MOD;
//        }
//
//        dp[idx][seen] = (int)ans;
//
//        return dp[idx][seen];
//    }

    public static void solve() {
        n = in.nextInt();
        long ans = dp[1000000 - n][0]; // rec(100000 - n, 0);

        // int ans = dp[0][0];

        out.println(ans);
    }

    public static void main(String[] args) {
        // Start writing your solution here. -------------------------------------
        in = new Reader();
        out = new Writer();

        for (int i = 0; i <= 1000000; i++) {
            Arrays.fill(dp[i], -1);
        }

        for (int idx = 1000000; idx >= 0; idx--) {
            for (int seen = 4; seen >= 0; seen--) {
                if (seen == 4) {
                    dp[idx][seen] = 0;
                    continue;
                }

                if (idx == 1000000) {
                    dp[idx][seen] = 1;
                    continue;
                }

                long ans = 0;
                if (seen == 0) {
                    ans = (ans + dp[idx + 1][1]) % MOD;
                    ans = (ans + dp[idx + 1][0]) % MOD;
                } else if (seen == 1) {
                    ans = (ans + dp[idx + 1][1]) % MOD;
                    ans = (ans + dp[idx + 1][2]) % MOD;
                } else if (seen == 2) {
                    ans = (ans + dp[idx + 1][3]) % MOD;
                    ans = (ans + dp[idx + 1][0]) % MOD;
                } else if (seen == 3) {
                    ans = (ans + dp[idx + 1][2]) % MOD;
                }

                dp[idx][seen] = (int)ans;
            }
        }

        int t = 1;
        t= in.nextInt();
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
         * int result = 3*n;
         * out.println(result); // print via PrintWriter
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
            for (int i = 0; i < a.length; i++) print(a[i] + " ");
        }

        public void printlnArray(int[] a) {
            for (int i = 0; i < a.length; i++) print(a[i] + " ");
            pw.println();
        }

        public void printArray(long[] a) {
            for (int i = 0; i < a.length; i++) print(a[i] + " ");
        }

        public void printlnArray(long[] a) {
            for (int i = 0; i < a.length; i++) print(a[i] + " ");
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
