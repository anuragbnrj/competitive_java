package library.math;

import java.util.Arrays;

public class Sieve {

    private static boolean[] getSieve(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Input must be non-negative");
        }

        // Initialize array with true values
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);

        // 0 and 1 are not prime numbers
        if (n >= 0) isPrime[0] = false;
        if (n >= 1) isPrime[1] = false;

        // O(N log log N) - Sieve of Eratosthenes algorithm
        for (long i = 2; i * i <= n; i++) {
            if (isPrime[(int)i]) {
                for (long j = i * i; j <= n; j += i) {
                    isPrime[(int)j] = false;
                }
            }
        }

        return isPrime;
    }

    public static void main(String[] args) {
        int n = 20;
        boolean[] primes = getSieve(n);

        System.out.println("Prime numbers up to " + n + ":");
        for (int i = 0; i <= n; i++) {
            if (primes[i]) {
                System.out.print(i + " ");
            }
        }
    }
}
