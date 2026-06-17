package library.trie;

public class Trie {

    private static class Node {
        Node[] links = new Node[26];
        boolean isEnd;

        boolean containsKey(char ch) { return links[ch - 'a'] != null; }
        Node    get(char ch)         { return links[ch - 'a']; }
        void    put(char ch, Node n) { links[ch - 'a'] = n; }
    }

    private final Node root;

    public Trie() {
        root = new Node();
    }

    public void insert(String word) {
        Node node = root;
        for (char ch : word.toCharArray()) {
            if (!node.containsKey(ch)) node.put(ch, new Node());
            node = node.get(ch);
        }
        node.isEnd = true;
    }

    // Returns true iff word was previously inserted exactly.
    public boolean search(String word) {
        Node node = root;
        for (char ch : word.toCharArray()) {
            if (!node.containsKey(ch)) return false;
            node = node.get(ch);
        }
        return node.isEnd;
    }

    // Returns true iff any inserted word starts with prefix.
    public boolean startsWith(String prefix) {
        Node node = root;
        for (char ch : prefix.toCharArray()) {
            if (!node.containsKey(ch)) return false;
            node = node.get(ch);
        }
        return true;
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        System.out.println(trie.search("apple"));      // expect: true
        System.out.println(trie.search("app"));        // expect: false
        System.out.println(trie.startsWith("app"));    // expect: true
        trie.insert("app");
        System.out.println(trie.search("app"));        // expect: true
        System.out.println(trie.startsWith("b"));      // expect: false
        System.out.println(trie.search(""));            // expect: false
    }
}
