// AtCoder Beginner Contest 104 B - AcCepted
// 2024-07-18 00:56

import java.io.*;
import java.util.*;

public class Main {

    static Reader in;
    static Writer out;

    public static void solve() {
        String s;
        s = in.next();

        char[] sarr = s.toCharArray();
        if (sarr[0] != 'A') {
            out.println("WA");
            return;
        }

        int count = 0;
        for (int i = 2; i < sarr.length - 1; i++) {
            if (sarr[i] == 'C') {
                count += 1;
            }
        }
        if (count != 1) {
            out.println("WA");
            return;
        }

        int lowerCount = 0;
        for (int i = 0; i < sarr.length; i++) {
            if (Character.isLowerCase(sarr[i])) {
                lowerCount += 1;
            }
        }
        if (lowerCount != (sarr.length - 2)) {
            out.println("WA");
            return;
        }

        out.println("AC");
        return;
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