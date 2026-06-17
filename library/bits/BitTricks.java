package library.bits;

import java.util.ArrayList;
import java.util.List;

public class BitTricks {

    // The lowest set bit of x, isolated (e.g. 12 = 1100 -> 0100 = 4).
    // x & -x works because -x is the two's-complement negation: flip all bits
    // and add 1, which turns everything below the lowest set bit into 1s while
    // the lowest set bit itself stays 1 - ANDing cancels everything else.
    public static int lowestSetBit(int x) {
        return x & -x;
    }

    // Number of 1 bits in x (Brian Kernighan's algorithm).
    // x & (x - 1) clears the lowest set bit, so the loop runs once per set bit.
    // (Integer.bitCount(x) does this in hardware - prefer it in real code; this
    // is the manual version interviewers sometimes ask for.)
    public static int countSetBits(int x) {
        int count = 0;
        while (x != 0) {
            x &= (x - 1);
            count++;
        }
        return count;
    }

    // All submasks of mask, including mask itself and 0. O(number of submasks):
    // a mask with k set bits has exactly 2^k submasks, and this loop visits
    // exactly those (not all 2^(bit width) values).
    public static List<Integer> subsetsOf(int mask) {
        List<Integer> subsets = new ArrayList<>();
        for (int sub = mask; ; sub = (sub - 1) & mask) {
            subsets.add(sub);
            if (sub == 0) break;
        }
        return subsets;
    }

    // Maximum XOR achievable by XOR-ing any subset of nums (linear/XOR basis via
    // Gaussian elimination over GF(2)). O(n * 32).
    public static int maxXorSubset(int[] nums) {
        int[] basis = new int[32]; // basis[i]'s highest set bit is at position i
        for (int num : nums) {
            for (int i = 31; i >= 0; i--) {
                if (((num >> i) & 1) == 0) continue;
                if (basis[i] == 0) {
                    basis[i] = num;
                    break;
                }
                num ^= basis[i];
            }
        }
        int result = 0;
        for (int i = 31; i >= 0; i--) {
            if ((result ^ basis[i]) > result) result ^= basis[i];
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(lowestSetBit(12));                  // expect: 4 (1100 -> 0100)
        System.out.println(countSetBits(11));                  // expect: 3 (1011 has three 1s)
        System.out.println(subsetsOf(5));                       // expect: [5, 4, 1, 0] (101 -> 101,100,001,000)
        System.out.println(maxXorSubset(new int[]{8, 1, 2}));   // expect: 11 (8^1^2)
    }
}
