package library.math;

public class MathUtils {

    public static final long MOD = 1_000_000_007L;

    // Euclidean algorithm. O(log(min(a,b))).
    public static long gcd(long a, long b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // Divide before multiplying - a/gcd(a,b)*b avoids overflowing past the
    // final LCM even when a*b would overflow long.
    public static long lcm(long a, long b) {
        return a / gcd(a, b) * b;
    }

    // (base^exp) % mod via binary exponentiation. O(log exp).
    public static long modPow(long base, long exp, long mod) {
        base %= mod;
        if (base < 0) base += mod;
        long result = 1;
        while (exp > 0) {
            if ((exp & 1) == 1) result = result * base % mod;
            base = base * base % mod;
            exp >>= 1;
        }
        return result;
    }

    // Modular inverse of a, mod p. Requires p PRIME (Fermat's little theorem:
    // a^(p-1) === 1 (mod p)  =>  a^(p-2) === a^-1 (mod p)).
    public static long modInverse(long a, long primeMod) {
        return modPow(a, primeMod - 2, primeMod);
    }

    // n choose r, mod a prime. O(n) per call (recomputes factorials) - fine
    // for a single query; precompute factorial/inverse-factorial arrays once
    // if called many times with the same n.
    public static long nCrMod(int n, int r, long primeMod) {
        if (r < 0 || r > n) return 0;
        long[] fact = new long[n + 1];
        fact[0] = 1;
        for (int i = 1; i <= n; i++) fact[i] = fact[i - 1] * i % primeMod;
        long denom = fact[r] * fact[n - r] % primeMod;
        return fact[n] * modInverse(denom, primeMod) % primeMod;
    }

    public static void main(String[] args) {
        System.out.println(gcd(12, 18));                   // expect: 6
        System.out.println(lcm(4, 6));                     // expect: 12
        System.out.println(modPow(2, 10, MOD));            // expect: 1024
        System.out.println(modPow(3, MOD - 1, MOD));       // expect: 1 (Fermat: a^(p-1) == 1 mod p)
        System.out.println(modInverse(2, MOD));            // expect: 500000004 (2 * x == 1 mod MOD)
        System.out.println(nCrMod(5, 2, MOD));             // expect: 10
        System.out.println(nCrMod(10, 0, MOD));            // expect: 1
    }
}
