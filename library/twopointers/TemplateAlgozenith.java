package library.twopointers;

public class TemplateAlgozenith {

    long getLessOrEqual(int k, int[] arr, int[] freq) {
        int len = freq.length;
        int distinct = 0;
        long ans = 0;

        // pointers
        int left = 0;
        int right = -1;

        // magic starts!
        while (left < len) {

            // eat as many elements as possible
            while (right + 1 < len && (distinct + ((freq[right + 1] == 0) ? 1 : 0) <= k)) {
                right++;
                // insert
                freq[arr[right]]++;
                if (freq[arr[right]] == 1)
                    distinct++;
            }

            // save the best for the left
            long currLen = (right - left + 1);
            ans += currLen * (currLen + 1) / 2;

            // left increment
            if (left <= right) {
                // erase
                freq[arr[left]]--;
                if (freq[arr[left]] == 0)
                    distinct--;
                left++;
            } else {
                left++;
                right = left - 1;
            }
        }
        return ans;
    }
}
