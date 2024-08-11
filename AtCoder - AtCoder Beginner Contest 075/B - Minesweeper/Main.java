// AtCoder Beginner Contest 075 B - Minesweeper
// 2024-07-19 01:00

import java.io.*;
import java.util.*;

public class Main {

    static Reader in;
    static Writer out;

    public static void solve() {
        int rows, cols;
        rows = in.nextInt();
        cols = in.nextInt();

        char[][] grid = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            grid[r] = in.inpChaArr();
        }

        int[] dr = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dc = {-1, 0, 1, -1, 1, -1, 0, 1};
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char curr = grid[r][c];

                if (curr == '#') {
                    out.print(curr);
                } else {
                    int bombs = 0;
                    for (int i = 0; i < 8; i++) {
                        int nr = r + dr[i];
                        int nc = c + dc[i];

                        if (isValid(nr, nc, rows, cols) && grid[nr][nc] == '#') {
                            bombs += 1;
                        }
                    }
                    out.print(bombs);
                }
            }

            out.println();
        }

        return;
    }

    private static boolean isValid(int r, int c, int rows, int cols) {
        if (r < 0 || rows <= r) {
            return false;
        }

        if (c < 0 || cols <= c) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        // Start writing your solution here. -------------------------------------
        in = new Reader();
        out = new Writer();

        int t = 1;
        // t= in.nextInt();
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