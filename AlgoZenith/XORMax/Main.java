//  XORMax
// 2024-08-09 16:26

// Problem Link:

import java.io.*;
import java.util.*;

public class Main {

    static Reader in;
    static Writer out;

    static int INF = 1_000_000_000;
    static long INFL = (long) 1e18;
    static int MOD = 1_000_000_007;

    static int bits = 30;
    static Node root;

    private static void solve() {
        int q;
        q = in.nextInt();

        root = new Node();
        insert(0);

        while (q-- > 0) {
            char ch = in.next().charAt(0);
            int num = in.nextInt();

            if (ch == '+') {
                insert(num);
            } else if (ch == '-') {
                delete(num);
            } else {
                out.println(computeMaxXor(num));
            }
        }

    }

    private static void insert(int n) {
        Node curr = root;
        for (int bit = bits; bit >= 0; bit--) {
            int val = (n >> bit) & 1;

            if (curr.child[val] == null) {
                curr.child[val] = new Node();
            }

            curr.child[val].count += 1;
            curr = curr.child[val];
        }
    }

    private static void delete(int n) {
        Node curr = root;
        for (int bit = bits; bit >= 0; bit--) {
            int val = (n >> bit) & 1;

            if (curr.child[val] == null) {
                curr.child[val] = new Node();
            }

            curr.child[val].count -= 1;
            curr = curr.child[val];
        }
    }

    private static int computeMaxXor(int n) {
        Node curr = root;
        int num = 0;
        for (int bit = bits; bit >= 0; bit--) {
            int val = ((n >> bit) & 1) ^ 1;

            if (curr.child == null || curr.child[val] == null || curr.child[val].count == 0) {
                val = val ^ 1;
            }

            num = num | (val << bit);
            if (curr.child == null) {
                curr.child = new Node[2];
            }
            curr = curr.child[val];
        }

        return num ^ n;
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



    static class Node {
        Node[] child;
        int count;

        public Node() {
            child = new Node[2];
            count = 0;
        }
    }

    static class Trie {
        Node root;

        public Trie() {
            root = new Node();
        }
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
