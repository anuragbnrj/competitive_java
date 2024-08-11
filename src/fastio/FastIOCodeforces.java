package fastio;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;

public final class FastIOCodeforces {
    private static final class Tokens extends LinkedList<String> {
        Tokens(String line) {
            super(new LinkedList<>());
            this.addAll(Arrays.asList(line.split(" ")));
        }

        String nextStr() {
            return remove();
        }

        int nextInt() {
            return Integer.parseInt(remove());
        }

        long nextLong() {
            return Long.parseLong(remove());
        }

        double nextDbl() {
            return Double.parseDouble(remove());
        }

        BigInteger nextBigInt() {
            return new BigInteger(remove());
        }
    }

    public static Tokens nextLine(BufferedReader bufferedReader) {
        String line = null;
        try {
            line = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Tokens(line);
    }

    private static byte[] toBytes(String s) {
        return s.getBytes();
    }

    private static byte[] toBytes(Integer x) {
        return toBytes(x.toString());
    }

    private static byte[] toBytes(Long x) {
        return toBytes(x.toString());
    }

    private static byte[] toBytes(String f, Double x) {
        return toBytes(String.format(f, x));
    }

    private static byte[] toBytes(BigInteger x) {
        return toBytes(x.toString());
    }

    private static final byte[] space = toBytes(" ");
    private static final byte[] endl = toBytes("\n");

    public static void main(String[] args) throws IOException {
        BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));

        final var out = new BufferedOutputStream(System.out);
        out.flush();
    }
}
