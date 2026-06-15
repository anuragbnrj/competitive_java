import java.util.HashSet;

public class TestLC {

    public int colorful(int A) {
        char[] arr = Integer.toString(A).toCharArray();

        int n = arr.length;
        HashSet<Integer> occurred = new HashSet<>();

        for (int i = 0; i < n; i++) {
            int currPr = 1;
            for (int j = i; j < n; j++) {
                currPr *= (arr[j] - '0');
                System.out.println("currPr: " + currPr);

                if (occurred.contains(currPr)) {
                    return 0;
                }

                occurred.add(currPr);
            }
        }

        return 1;
    }

    public static void main(String[] args) {
        TestLC obj = new TestLC();

        int ans = obj.colorful(123);

        System.out.println("ans: " + ans);
    }
}
