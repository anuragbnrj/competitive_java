package library.trie;

// Cost/count variant: tracks how many times each word was inserted, and the
// minimum cost seen across all insertions of a word. Useful for problems that
// ask "how many words share this prefix" or "cheapest word ending here".
// For the standard LC208 insert/search/startsWith interface, use Trie.java.
public class TrieCount {

    private static class Node {
        private Node[] links;
        private int countEndsWith;
        private int countPrefix;
        private int minCostEndAt;

        public Node() {
            links = new Node[26];
            countEndsWith = 0;
            countPrefix = 0;
            minCostEndAt = (int) 1E9;
        }

        public boolean containsKey(char ch) { return links[ch - 'a'] != null; }
        public Node    get(char ch)          { return links[ch - 'a']; }
        public void    put(char ch, Node n)  { links[ch - 'a'] = n; }

        public void increaseEnd(int cost) {
            countEndsWith++;
            minCostEndAt = Math.min(minCostEndAt, cost);
        }
        public void increasePrefix() { countPrefix++; }
        public void reduceEnd()      { countEndsWith--; }
        public void reducePrefix()   { countPrefix--; }
        public int  getEndsWith()    { return countEndsWith; }
        public int  getPrefix()      { return countPrefix; }
    }

    private final Node root;

    public TrieCount() {
        root = new Node();
    }

    public void insert(String word, int cost) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            if (!node.containsKey(word.charAt(i))) {
                node.put(word.charAt(i), new Node());
            }
            node = node.get(word.charAt(i));
            node.increasePrefix();
        }
        node.increaseEnd(cost);
    }

    public int countWordsEqualTo(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            if (node.containsKey(word.charAt(i))) {
                node = node.get(word.charAt(i));
            } else {
                return 0;
            }
        }
        return node.getEndsWith();
    }

    public int countWordsStartingWith(String prefix) {
        Node node = root;
        for (int i = 0; i < prefix.length(); i++) {
            if (node.containsKey(prefix.charAt(i))) {
                node = node.get(prefix.charAt(i));
            } else {
                return 0;
            }
        }
        return node.getPrefix();
    }

    // Decrements insertion counters. Does NOT unlink nodes (orphaned paths are a
    // memory concern, not a correctness concern, for OA-sized input).
    public void erase(String word) {
        Node node = root;
        for (int i = 0; i < word.length(); i++) {
            if (node.containsKey(word.charAt(i))) {
                node = node.get(word.charAt(i));
                node.reducePrefix();
            } else {
                return;
            }
        }
        node.reduceEnd();
    }

    public static void main(String[] args) {
        TrieCount tc = new TrieCount();
        tc.insert("apple", 5);
        tc.insert("apple", 3);
        tc.insert("app", 7);
        System.out.println(tc.countWordsEqualTo("apple"));      // expect: 2
        System.out.println(tc.countWordsEqualTo("app"));        // expect: 1
        System.out.println(tc.countWordsStartingWith("app"));   // expect: 3
        tc.erase("apple");
        System.out.println(tc.countWordsEqualTo("apple"));      // expect: 1
        System.out.println(tc.countWordsStartingWith("app"));   // expect: 2
        System.out.println(tc.countWordsEqualTo("xyz"));        // expect: 0
    }
}
