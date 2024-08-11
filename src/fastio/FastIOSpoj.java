package fastio;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FastIOSpoj {

    static class FastIO {
        InputStream inputStream;
        byte[] buffer = new byte[1 << 17];
        int pointer = 0;

        public FastIO(String fileName) throws IOException {
            inputStream = new FileInputStream(fileName);
        }

        public FastIO(InputStream is) {
            inputStream = is;
        }

        int nextInt() throws Exception {
            int ret = 0;
            byte b;
            do {
                b = nextByte();
            } while (b <= ' ');
            boolean negative = false;
            if (b == '-') {
                negative = true;
                b = nextByte();
            }
            while (b >= '0' && b <= '9') {
                ret = 10 * ret + b - '0';
                b = nextByte();
            }
            return (negative) ? -ret : ret;
        }

        long nextLong() throws Exception {
            long ret = 0;
            byte b;
            do {
                b = nextByte();
            } while (b <= ' ');
            boolean negative = false;
            if (b == '-') {
                negative = true;
                b = nextByte();
            }
            while (b >= '0' && b <= '9') {
                ret = 10 * ret + b - '0';
                b = nextByte();
            }
            return (negative) ? -ret : ret;
        }

        Integer[] readArray(int n) throws Exception {
            Integer[] a = new Integer[n];
            for (int i = 0; i < n; i++) a[i] = nextInt();
            return a;
        }

        byte nextByte() throws IOException {
            if (pointer == buffer.length) {
                inputStream.read(buffer, 0, buffer.length);
                pointer = 0;
            }
            return buffer[pointer++];
        }

        String next() throws Exception {
            StringBuilder ret = new StringBuilder();
            byte b;
            do {
                b = nextByte();
            } while (b <= ' ');
            while (b > ' ') {
                ret.appendCodePoint(b);
                b = nextByte();
            }
            return ret.toString();
        }

        public void close() throws Exception {
            if (inputStream == null) return;
            inputStream.close();
        }
    }
}
