//  Minimum Penalty
// 2024-08-03 14:32

// Problem Link:

import java.io.*;
import java.util.*;

public class Main {

    static Reader in;
    static Writer out;

    static int INF = 1_000_000_000;
    static long INFL = (long) 1e18;
    static int MOD = 1_000_000_007;
    static int[] freq = new int[1000006];

    public static void solve() {
        int n, d;
        n = in.nextInt();
        d = in.nextInt();

        int[] a = new int[n];

        for (int i = 0; i < n; i++) {
            a[i] = in.nextInt();
        }

        int tail = 0, head = -1;
        int distinctCount = 0;
        int ans = (int) 1e9;
        while (tail < n) {
            while (head + 1 < n && (head + 1 - tail + 1) <= d) {
                head += 1;

                if (freq[a[head]] == 0) {
                    distinctCount += 1;
                }
                freq[a[head]] += 1;
            }

            if ((head - tail + 1) == d) {
                ans = Math.min(ans, distinctCount);
            }

            if (head < tail) {
                tail += 1;
                head = tail - 1;
            } else {
                freq[a[tail]] -= 1;
                if (freq[a[tail]] == 0) {
                    distinctCount -= 1;
                }

                tail += 1;
            }
        }

        out.println(ans);
    }

    public static void main(String[] args) {
        // Start writing your solution here. -------------------------------------
        in = new Reader();
        out = new Writer();

        int t = 1;
         t = in.nextInt();
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
