package library.strings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Kmp {

    // fail[i] = length of the longest PROPER prefix of p[0..i] that is also a
    // suffix of p[0..i]. Built in O(m).
    public static int[] buildFailure(String p) {
        int n = p.length();
        int[] fail = new int[n];
        int k = 0;
        for (int i = 1; i < n; i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) k = fail[k - 1];
            if (p.charAt(i) == p.charAt(k)) k++;
            fail[i] = k;
        }
        return fail;
    }

    // All (0-indexed) starting positions in `text` where `pattern` occurs. O(n + m).
    public static List<Integer> search(String text, String pattern) {
        List<Integer> result = new ArrayList<>();
        if (pattern.isEmpty()) return result;
        int[] fail = buildFailure(pattern);
        int k = 0;
        for (int i = 0; i < text.length(); i++) {
            while (k > 0 && text.charAt(i) != pattern.charAt(k)) k = fail[k - 1];
            if (text.charAt(i) == pattern.charAt(k)) k++;
            if (k == pattern.length()) {
                result.add(i - k + 1);
                k = fail[k - 1]; // continue searching - allows overlapping matches
            }
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(buildFailure("ababaca"))); // expect: [0, 0, 1, 2, 3, 0, 1]
        System.out.println(search("ababcabab", "abab")); // expect: [0, 5]
        System.out.println(search("aaaa", "aa"));         // expect: [0, 1, 2] (overlapping)
    }
}
