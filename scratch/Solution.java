public class Solution {

    public static long getMaximumEvenSum(int[] val) {
        long ans = rec(0, 0, val);

        return ans;
    }

    private static long rec(int flag, int idx, int[] val) {
        if (idx >= val.length) {
            return 0;
        }

        long ans = 0;
        if (val[idx] % 2 == 0) {
            long ch1, ch2;

            if (flag == 0) {
                ch1 = val[idx] + rec(0, idx + 1, val);
                ch2 = rec(0, idx + 1, val);

                ans = Math.max(ch1, ch2);
            } else {
                ch1 = val[idx] + rec(1, idx + 1, val);
                ch2 = rec(1, idx + 1, val);

                ans = Math.max(ch1, ch2);
            }

        } else {
            long ch1, ch2;

            if (flag == 0) {
                ch1 = val[idx] + rec(1, idx + 1, val);
                ch2 = rec(0, idx + 1, val);

                ans = Math.max(ch1, ch2);
            } else {
                ch1 = val[idx] + rec(0, idx + 1, val);
                ch2 = rec(1, idx + 1, val);

                ans = Math.max(ch1, ch2);
            }

        }

        return ans;
    }

    public static void main(String[] args) {
//        int[] val = {-1, -2, -3, 8, 7};

         int[] val = {6, 3, 4, -1, 9, 17};

        long ans = getMaximumEvenSum(val);

        System.out.println("ans: " + ans);
    }
}
