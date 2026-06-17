package library.strings;

import java.util.ArrayList;
import java.util.List;

public class RabinKarp {

    private static final long BASE = 131;
    private static final long MOD = 1_000_000_007L;

    // All (0-indexed) starting positions in `text` where `pattern` occurs.
    // A rolling hash narrows candidates to O(n); on a hash match we verify
    // with a direct compare to guard against the rare collision.
    public static List<Integer> search(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        int n = text.length(), m = pattern.length();
        if (m == 0 || m > n) return result;

        long patHash = 0, curHash = 0, pow = 1; // pow ends as BASE^(m-1) mod MOD
        for (int i = 0; i < m; i++) {
            patHash = (patHash * BASE + pattern.charAt(i)) % MOD;
            curHash = (curHash * BASE + text.charAt(i)) % MOD;
            if (i > 0) pow = pow * BASE % MOD;
        }

        for (int i = 0; ; i++) {
            if (curHash == patHash && text.regionMatches(i, pattern, 0, m)) {
                result.add(i);
            }
            if (i + m >= n) break;
            long leading = (text.charAt(i) * pow) % MOD;
            curHash = ((curHash - leading + MOD) % MOD * BASE + text.charAt(i + m)) % MOD;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(search("ababcabab", "abab")); // expect: [0, 5]
        System.out.println(search("aaaa", "aa"));         // expect: [0, 1, 2]
        System.out.println(search("abc", "d"));           // expect: []
    }
}
